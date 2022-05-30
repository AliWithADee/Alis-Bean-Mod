package io.github.aliwithadee.alisbeanmod.core.cooking.drinks;

import io.github.aliwithadee.alisbeanmod.core.cooking.CookingUtils;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrinkUtils {
    public static Drink getDrink(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return ModDrinks.DEFAULT;
        CompoundTag dataTag = tag.getCompound(CookingUtils.NBT_DATA);

        return new Drink(ModDrinks.getBaseDrink(tag.getString(CookingUtils.NBT_DRINK_NAME)),
                tag.getInt(CookingUtils.NBT_RATING),
                ModDrinks.getDrinkRecipe(dataTag.getString(CookingUtils.NBT_RECIPE)),
                CookingUtils.ingredientsFromTag(dataTag), dataTag.getInt(CookingUtils.NBT_COOK),
                dataTag.getInt(CookingUtils.NBT_DISTILLS), dataTag.getInt(CookingUtils.NBT_AGE));
    }

    public static ItemStack setDrink(ItemStack stack, Drink drink) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(CookingUtils.NBT_DRINK_NAME, drink.getName());
        tag.putInt(CookingUtils.NBT_RATING, drink.getRating());

        if (drink.inProgress()) {
            CompoundTag dataTag = new CompoundTag();

            dataTag.putString(CookingUtils.NBT_RECIPE, drink.getRecipe().getName());
            CookingUtils.ingredientsToTag(dataTag, drink.getIngredients());
            dataTag.putInt(CookingUtils.NBT_COOK, drink.getCookTime());
            dataTag.putInt(CookingUtils.NBT_DISTILLS, drink.getDistills());
            dataTag.putInt(CookingUtils.NBT_AGE, drink.getBarrelMinutes());

            tag.put(CookingUtils.NBT_DATA, dataTag);
        }

        stack.setTag(tag);
        return stack;
    }

    public static ItemStack createDrinkItem(Drink drink) {
        return setDrink(new ItemStack(ModItems.DRINK.get()), drink);
    }

    public static int getDrinkColor(ItemStack stack) {
        return getDrink(stack).getColor();
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

    public static void gradeDrink(Drink drink) {
        DrinkRecipe recipe = drink.getRecipe();

        int ing_rating;
        int ing_diff = CookingUtils.getIngredientError(recipe.getIngredients(), drink.getIngredients());
        if (ing_diff == 0) ing_rating = 5;
        else if (0 < ing_diff && ing_diff <= BeanModCommonConfig.ING_DIFF_GREAT.get()) ing_rating = 4;
        else if (BeanModCommonConfig.ING_DIFF_GREAT.get() < ing_diff && ing_diff <= BeanModCommonConfig.ING_DIFF_FINE.get()) ing_rating = 3;
        else if (BeanModCommonConfig.ING_DIFF_FINE.get() < ing_diff && ing_diff <= BeanModCommonConfig.ING_DIFF_POOR.get()) ing_rating = 2;
        else ing_rating = 1;

        int cook_rating;
        int cook_diff = Math.abs(recipe.getCookTime() - drink.getCookTime());
        if (cook_diff == 0) cook_rating = 5;
        else if (0 < cook_diff && cook_diff <= BeanModCommonConfig.COOK_DIFF_GREAT.get()) cook_rating = 4;
        else if (BeanModCommonConfig.COOK_DIFF_GREAT.get() < cook_diff && cook_diff <= BeanModCommonConfig.COOK_DIFF_FINE.get()) cook_rating = 3;
        else if (BeanModCommonConfig.COOK_DIFF_FINE.get() < cook_diff && cook_diff <= BeanModCommonConfig.COOK_DIFF_POOR.get()) cook_rating = 2;
        else cook_rating = 1;

        int wrongStepsTaken = 0;

        int distill_rating;
        int distill_diff = Math.abs(recipe.getDistills() - drink.getDistills());
        if (recipe.getDistills() == 0 && distill_diff > 0) wrongStepsTaken++;

        if (distill_diff == 0) distill_rating = 5;
        else if (0 < distill_diff && distill_diff <= BeanModCommonConfig.DISTILL_DIFF_GREAT.get()) distill_rating = 4;
        else if (BeanModCommonConfig.DISTILL_DIFF_GREAT.get() < distill_diff && distill_diff <= BeanModCommonConfig.DISTILL_DIFF_FINE.get()) distill_rating = 3;
        else if (BeanModCommonConfig.DISTILL_DIFF_FINE.get() < distill_diff && distill_diff <= BeanModCommonConfig.DISTILL_DIFF_POOR.get()) distill_rating = 2;
        else distill_rating = 1;

        int age_rating;
        int age_diff = Math.abs(recipe.getBarrelYears() - drink.getBarrelYears());
        if (recipe.getBarrelYears() == 0 && age_diff > 0) wrongStepsTaken++;

        if (age_diff == 0) age_rating = 5;
        else if (0 < age_diff && age_diff <= BeanModCommonConfig.AGE_DIFF_GREAT.get()) age_rating = 4;
        else if (BeanModCommonConfig.AGE_DIFF_GREAT.get() < age_diff && age_diff <= BeanModCommonConfig.AGE_DIFF_FINE.get()) age_rating = 3;
        else if (BeanModCommonConfig.AGE_DIFF_FINE.get() < age_diff && age_diff <= BeanModCommonConfig.AGE_DIFF_POOR.get()) age_rating = 2;
        else age_rating = 1;

        float ratio = (ing_rating + cook_rating + distill_rating + age_rating) / (BeanModCommonConfig.MAX_RATING.get() * 4.0f);
        int rating = (int)(ratio * BeanModCommonConfig.MAX_RATING.get());

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
