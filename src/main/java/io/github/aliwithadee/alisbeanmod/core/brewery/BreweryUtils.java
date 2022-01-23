package io.github.aliwithadee.alisbeanmod.core.brewery;

import io.github.aliwithadee.alisbeanmod.core.brewery.drink.Drink;
import io.github.aliwithadee.alisbeanmod.core.brewery.drink.DrinkRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class BreweryUtils {
    // 1440 minecraft minutes  =  1 minecraft day
    // 1 minecraft day         =  1 barrel age year
    public static final int MINUTES_PER_BARREL_YEAR = 20; // TODO: Don't forget to set this to actual value
    public static final int MAX_RECIPE_DIFFERENCE = 25;
    public static final int MAX_RATING = 5;

    public static final int ING_DIFF_GREAT = 2;
    public static final int ING_DIFF_FINE = 3;
    public static final int ING_DIFF_POOR = 4;

    public static final int COOK_DIFF_GREAT = 1;
    public static final int COOK_DIFF_FINE = 2;
    public static final int COOK_DIFF_POOR = 3;

    public static final int DISTILL_DIFF_GREAT = 1;
    public static final int DISTILL_DIFF_FINE = 2;
    public static final int DISTILL_DIFF_POOR = 3;

    public static final int AGE_DIFF_GREAT = 2;
    public static final int AGE_DIFF_FINE = 3;
    public static final int AGE_DIFF_POOR = 4;

    public static boolean stackInList(ItemStack stack, NonNullList<ItemStack> list) {
        for (ItemStack itemStack : list) {
            if (stack.getItem() == itemStack.getItem()) {
                return true;
            }
        }
        return false;
    }

    // Integer showing how far the recipe ingredients are away from the stacks the pot
    public static int getIngredientDifference(DrinkRecipe recipe, NonNullList<ItemStack> ingredients) {
        int diff = 0;
        for (ItemStack stack : ingredients) {
            int d;
            if (stackInList(stack, recipe.getIngredients())) {
                d = Math.abs(stack.getCount() - recipe.getIngredientCount(stack));
            } else {
                d = stack.getCount();
            }
            diff += d;
        }
        return diff;
    }

    public static int gradeDrink(Drink drink) {
        DrinkRecipe recipe = drink.getRecipe();

        int ing_rating;
        int ing_diff = getIngredientDifference(recipe, drink.getIngredients());
        if (ing_diff == 0) ing_rating = 5;
        else if (0 < ing_diff && ing_diff <= ING_DIFF_GREAT) ing_rating = 4;
        else if (ING_DIFF_GREAT < ing_diff && ing_diff <= ING_DIFF_FINE) ing_rating = 3;
        else if (ING_DIFF_FINE < ing_diff && ing_diff <= ING_DIFF_POOR) ing_rating = 2;
        else ing_rating = 1;

        int cook_rating;
        int cook_diff = Math.abs(recipe.getCookTime() - drink.getCookTime());
        if (cook_diff == 0) cook_rating = 5;
        else if (0 < cook_diff && cook_diff <= COOK_DIFF_GREAT) cook_rating = 4;
        else if (COOK_DIFF_GREAT < cook_diff && cook_diff <= COOK_DIFF_FINE) cook_rating = 3;
        else if (COOK_DIFF_FINE < cook_diff && cook_diff <= COOK_DIFF_POOR) cook_rating = 2;
        else cook_rating = 1;

        int wrongStepsTaken = 0;

        int distill_rating;
        int distill_diff = Math.abs(recipe.getDistills() - drink.getDistills());
        if (recipe.getDistills() == 0 && distill_diff > 0) wrongStepsTaken++;

        if (distill_diff == 0) distill_rating = 5;
        else if (0 < distill_diff && distill_diff <= DISTILL_DIFF_GREAT) distill_rating = 4;
        else if (DISTILL_DIFF_GREAT < distill_diff && distill_diff <= DISTILL_DIFF_FINE) distill_rating = 3;
        else if (DISTILL_DIFF_FINE < distill_diff && distill_diff <= DISTILL_DIFF_POOR) distill_rating = 2;
        else distill_rating = 1;

        int age_rating;
        int age_diff = Math.abs(recipe.getBarrelYears() - drink.getBarrelYears());
        if (recipe.getBarrelYears() == 0 && age_diff > 0) wrongStepsTaken++;

        if (age_diff == 0) age_rating = 5;
        else if (0 < age_diff && age_diff <= AGE_DIFF_GREAT) age_rating = 4;
        else if (AGE_DIFF_GREAT < age_diff && age_diff <= AGE_DIFF_FINE) age_rating = 3;
        else if (AGE_DIFF_FINE < age_diff && age_diff <= AGE_DIFF_POOR) age_rating = 2;
        else age_rating = 1;

        float ratio = (ing_rating + cook_rating + distill_rating + age_rating) / (MAX_RATING * 4.0f);
        int rating = (int)(ratio * BreweryUtils.MAX_RATING);

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

        return rating;
    }
}
