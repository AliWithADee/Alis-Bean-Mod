package io.github.aliwithadee.alisbeanmod.core.cooking.dishes;

import io.github.aliwithadee.alisbeanmod.common.cooking.item.DishItem;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DishUtils {
    private static final String NBT_RATING = "rating";
    private static final String NBT_DATA = "data"; // TODO: stats?
    private static final String NBT_RECIPE = "recipe";
    private static final String NBT_ING = "ingredients";
    private static final String NBT_COOK = "minutes";

    public static DishStats getDishStats(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return ModDishes.DEFAULT_STATS;

        CompoundTag dataTag = tag.getCompound(NBT_DATA);

        return new DishStats(tag.getInt(NBT_RATING), ModDishes.getDishRecipe(dataTag.getString(NBT_RECIPE)),
                getIngredients(dataTag), dataTag.getInt(NBT_COOK));
    }

    public static ItemStack setDishStats(ItemStack stack, DishStats dishStats) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(NBT_RATING, dishStats.getRating());

        if (dishStats.inProgress()) {
            CompoundTag dataTag = new CompoundTag();

            dataTag.putString(NBT_RECIPE, dishStats.getRecipe().getName());
            saveIngredients(dataTag, dishStats.getIngredients());
            dataTag.putInt(NBT_COOK, dishStats.getCookTime());
            tag.put(NBT_DATA, dataTag);
        }

        stack.setTag(tag);
        return stack;
    }

    public static ItemStack createDishItem(DishItem dish) {
        return createDishItem(dish, ModDishes.DEFAULT_STATS);
    }

    public static ItemStack createDishItem(DishItem dish, DishStats dishStats) {
        return setDishStats(new ItemStack(dish), dishStats);
    }

    public static void addDishTooltip(ItemStack stack, List<Component> tooltips) {
        DishStats dishStats = getDishStats(stack);

        int rating = dishStats.getRating();
        if (dishStats.isGraded()) tooltips.add(new TextComponent("Rating: " + rating).withStyle(ChatFormatting.GRAY));

        if (dishStats.inProgress()) {
            NonNullList<ItemStack> ingredients = dishStats.getIngredients();
            if (Screen.hasShiftDown()) {
                tooltips.add(new TextComponent("Ingredients:").withStyle(ChatFormatting.GRAY));
                for (ItemStack ingredient : ingredients) {
                    tooltips.add(new TextComponent("  -  " + ingredient.getDisplayName().getString() + " x" + ingredient.getCount()).withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltips.add(new TextComponent("Ingredients: x" + ingredients.size()).withStyle(ChatFormatting.GRAY));
            }

            int cookTime = dishStats.getCookTime();
            if (cookTime == 1) tooltips.add(new TextComponent("Cooked for 1 minute").withStyle(ChatFormatting.GRAY));
            else if (cookTime > 1) tooltips.add(new TextComponent("Cooked for " + cookTime + " minutes").withStyle(ChatFormatting.GRAY));
        }
    }

    private static void saveIngredients(CompoundTag tag, NonNullList<ItemStack> ingredients) {
        ListTag listTag = new ListTag();
        for (ItemStack stack : ingredients) {
            if (!stack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                stack.save(compoundTag);
                listTag.add(compoundTag);
            }
        }
        tag.put(NBT_ING, listTag);
    }

    private static NonNullList<ItemStack> getIngredients(CompoundTag tag) {
        NonNullList<ItemStack> ingredients = NonNullList.create();
        ListTag listTag = tag.getList(NBT_ING, 10);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            ingredients.add(ItemStack.of(compoundTag));
        }
        return ingredients;
    }

    public static boolean stackInList(ItemStack stack, NonNullList<ItemStack> list) {
        for (ItemStack itemStack : list) {
            if (stack.getItem() == itemStack.getItem()) {
                return true;
            }
        }
        return false;
    }

    // Integer showing how far the recipe ingredients are away from the stacks the pot
    public static int getIngredientError(DishRecipe recipe, NonNullList<ItemStack> ingredients) {
        int error = 0;
        for (ItemStack stack : ingredients) {
            int diff;
            if (stackInList(stack, recipe.getIngredients())) {
                diff = Math.abs(stack.getCount() - recipe.getIngredientCount(stack));
            } else {
                diff = stack.getCount();
            }
            error += diff;
        }
        return error;
    }

    public static void gradeDish(DishStats dishStats) {
        DishRecipe recipe = dishStats.getRecipe();

        int ing_rating;
        int ing_diff = getIngredientError(recipe, dishStats.getIngredients());
        if (ing_diff == 0) ing_rating = 5;
        else if (0 < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_GREAT) ing_rating = 4;
        else if (BeanModConfig.ING_DIFF_GREAT < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_FINE) ing_rating = 3;
        else if (BeanModConfig.ING_DIFF_FINE < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_POOR) ing_rating = 2;
        else ing_rating = 1;

        int cook_rating;
        int cook_diff = Math.abs(recipe.getCookTime() - dishStats.getCookTime());
        if (cook_diff == 0) cook_rating = 5;
        else if (0 < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_GREAT) cook_rating = 4;
        else if (BeanModConfig.COOK_DIFF_GREAT < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_FINE) cook_rating = 3;
        else if (BeanModConfig.COOK_DIFF_FINE < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_POOR) cook_rating = 2;
        else cook_rating = 1;

        //int wrongStepsTaken = 0;

        float ratio = (ing_rating + cook_rating) / (BeanModConfig.MAX_RATING * 2.0f);
        int rating = (int)(ratio * BeanModConfig.MAX_RATING);

        // TODO: Remove debug print statements

        System.out.println("---------");
        System.out.println("ing: " + ing_rating);
        System.out.println("cook: " + cook_rating);
        System.out.println("ratio: " + ratio);
        System.out.println("rating: " + rating);

//        for (int i = 0; i < wrongStepsTaken; i++) {
//            System.out.println("rating lowered");
//            if (rating > 2) rating = 2;
//            else if (rating > 1) rating = 1;
//        }

        dishStats.setRating(rating);
    }
}
