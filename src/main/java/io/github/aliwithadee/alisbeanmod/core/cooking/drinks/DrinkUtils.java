package io.github.aliwithadee.alisbeanmod.core.cooking.drinks;

import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
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

public class DrinkUtils {
    private static final String NBT_NAME = "name";
    private static final String NBT_RATING = "rating";
    private static final String NBT_DATA = "data";
    private static final String NBT_RESULT = "result";
    private static final String NBT_ING = "ingredients";
    private static final String NBT_COOK = "minutes";
    private static final String NBT_DISTILLS = "distills";
    private static final String NBT_AGE = "years";

    public static Drink getDrink(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return ModDrinks.DEFAULT;
        CompoundTag dataTag = tag.getCompound(NBT_DATA);

        return new Drink(tag.getString(NBT_NAME), tag.getInt(NBT_RATING), ModDrinks.getDrinkRecipe(dataTag.getString(NBT_RESULT)),
                getIngredients(dataTag), dataTag.getInt(NBT_COOK), dataTag.getInt(NBT_DISTILLS), dataTag.getInt(NBT_AGE));
    }

    public static ItemStack setDrink(ItemStack stack, Drink drink) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(NBT_NAME, drink.getName());
        tag.putInt(NBT_RATING, drink.getRating());

        if (drink.inProgress()) {
            CompoundTag dataTag = new CompoundTag();

            dataTag.putString(NBT_RESULT, drink.getRecipe().getResult().getName());
            saveIngredients(dataTag, drink.getIngredients());
            dataTag.putInt(NBT_COOK, drink.getCookTime());
            dataTag.putInt(NBT_DISTILLS, drink.getDistills());
            dataTag.putInt(NBT_AGE, drink.getBarrelMinutes());

            tag.put(NBT_DATA, dataTag);
        }

        stack.setTag(tag);
        return stack;
    }

    public static ItemStack createDrinkItem(Drink drink) {
        return setDrink(new ItemStack(ModItems.DRINK.get()), drink);
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

    public static void addDrinkTooltip(ItemStack stack, List<Component> tooltips) {
        Drink drink = getDrink(stack);

        int rating = drink.getRating();
        if (drink.isGraded()) tooltips.add(new TextComponent("Rating: " + rating).withStyle(ChatFormatting.GRAY));

        if (drink.inProgress()) {
            NonNullList<ItemStack> ingredients = drink.getIngredients();
            if (Screen.hasShiftDown()) {
                tooltips.add(new TextComponent("Ingredients:").withStyle(ChatFormatting.GRAY));
                for (ItemStack ingredient : ingredients) {
                    tooltips.add(new TextComponent("  -  " + ingredient.getDisplayName().getString() + " x" + ingredient.getCount()).withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltips.add(new TextComponent("Ingredients: x" + ingredients.size()).withStyle(ChatFormatting.GRAY));
            }

            int cookTime = drink.getCookTime();
            if (cookTime == 1) tooltips.add(new TextComponent("Cooked for 1 minute").withStyle(ChatFormatting.GRAY));
            else if (cookTime > 1) tooltips.add(new TextComponent("Cooked for " + cookTime + " minutes").withStyle(ChatFormatting.GRAY));

            int distills = drink.getDistills();
            if (distills == 1) tooltips.add(new TextComponent("Distilled once").withStyle(ChatFormatting.GRAY));
            else if (distills > 1) tooltips.add(new TextComponent("Distilled " + distills + " times").withStyle(ChatFormatting.GRAY));

            int years = drink.getBarrelYears();
            if (years == 1) tooltips.add(new TextComponent("Aged for 1 year").withStyle(ChatFormatting.GRAY));
            else if (years > 1) tooltips.add(new TextComponent("Aged for " + years + " years").withStyle(ChatFormatting.GRAY));
        }
    }

    public static int getDrinkColor(ItemStack stack) {
        return getDrink(stack).getColor();
    }

    // Integer showing how far the recipe ingredients are away from the stacks the pot
    public static int getIngredientError(DrinkRecipe recipe, NonNullList<ItemStack> ingredients) {
        int diff = 0;
        for (ItemStack stack : ingredients) {
            int d;
            if (recipe.getIngredients().contains(stack)) {
                d = Math.abs(stack.getCount() - recipe.getIngredientCount(stack));
            } else {
                d = stack.getCount();
            }
            diff += d;
        }
        return diff;
    }

    public static void gradeDrink(Drink drink) {
        DrinkRecipe recipe = drink.getRecipe();

        int ing_rating;
        int ing_diff = getIngredientError(recipe, drink.getIngredients());
        if (ing_diff == 0) ing_rating = 5;
        else if (0 < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_GREAT) ing_rating = 4;
        else if (BeanModConfig.ING_DIFF_GREAT < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_FINE) ing_rating = 3;
        else if (BeanModConfig.ING_DIFF_FINE < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_POOR) ing_rating = 2;
        else ing_rating = 1;

        int cook_rating;
        int cook_diff = Math.abs(recipe.getCookTime() - drink.getCookTime());
        if (cook_diff == 0) cook_rating = 5;
        else if (0 < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_GREAT) cook_rating = 4;
        else if (BeanModConfig.COOK_DIFF_GREAT < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_FINE) cook_rating = 3;
        else if (BeanModConfig.COOK_DIFF_FINE < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_POOR) cook_rating = 2;
        else cook_rating = 1;

        int wrongStepsTaken = 0;

        int distill_rating;
        int distill_diff = Math.abs(recipe.getDistills() - drink.getDistills());
        if (recipe.getDistills() == 0 && distill_diff > 0) wrongStepsTaken++;

        if (distill_diff == 0) distill_rating = 5;
        else if (0 < distill_diff && distill_diff <= BeanModConfig.DISTILL_DIFF_GREAT) distill_rating = 4;
        else if (BeanModConfig.DISTILL_DIFF_GREAT < distill_diff && distill_diff <= BeanModConfig.DISTILL_DIFF_FINE) distill_rating = 3;
        else if (BeanModConfig.DISTILL_DIFF_FINE < distill_diff && distill_diff <= BeanModConfig.DISTILL_DIFF_POOR) distill_rating = 2;
        else distill_rating = 1;

        int age_rating;
        int age_diff = Math.abs(recipe.getBarrelYears() - drink.getBarrelYears());
        if (recipe.getBarrelYears() == 0 && age_diff > 0) wrongStepsTaken++;

        if (age_diff == 0) age_rating = 5;
        else if (0 < age_diff && age_diff <= BeanModConfig.AGE_DIFF_GREAT) age_rating = 4;
        else if (BeanModConfig.AGE_DIFF_GREAT < age_diff && age_diff <= BeanModConfig.AGE_DIFF_FINE) age_rating = 3;
        else if (BeanModConfig.AGE_DIFF_FINE < age_diff && age_diff <= BeanModConfig.AGE_DIFF_POOR) age_rating = 2;
        else age_rating = 1;

        float ratio = (ing_rating + cook_rating + distill_rating + age_rating) / (BeanModConfig.MAX_RATING * 4.0f);
        int rating = (int)(ratio * BeanModConfig.MAX_RATING);

        // TODO: Remove debug print statements

        System.out.println("---------");
        System.out.println("ing: " + ing_rating);
        System.out.println("cook: " + cook_rating);
        System.out.println("distill: " + distill_rating);
        System.out.println("age: " + age_rating);
        System.out.println("ratio: " + ratio);
        System.out.println("rating: " + rating);

        for (int i = 0; i < wrongStepsTaken; i++) {
            System.out.println("rating lowered");
            if (rating > 2) rating = 2;
            else if (rating > 1) rating = 1;
        }

        drink.setRating(rating);
    }
}
