package io.github.aliwithadee.alisbeanmod.core.cooking.dish.stats;

import io.github.aliwithadee.alisbeanmod.common.cooking.item.DishItem;
import io.github.aliwithadee.alisbeanmod.core.cooking.CookingUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.DishUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.recipe.CookingPotDishRecipe;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.recipe.DishRecipe;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CookingPotDishStats extends DishStats {
    public static final String TYPE = "cooking";

    private final int cookTime;
    private final NonNullList<ItemStack> ingredients;

    public CookingPotDishStats(DishRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime) {
        this(0, recipe, ingredients, cookTime);
    }

    public CookingPotDishStats(int rating, DishRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime) {
        super(rating, recipe);
        this.ingredients = ingredients;
        this.cookTime = cookTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
    }

    @Override
    public void computeRating() {
        CookingPotDishRecipe cookingRecipe = (CookingPotDishRecipe) this.recipe;

        // ===== Ingredient Quantity =====

        int ing_quantity;
        int ing_diff = CookingUtils.getIngredientError(cookingRecipe.getIngredients(), ingredients);

        if (ing_diff == 0) ing_quantity = 5;
        else if (0 < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_GREAT) ing_quantity = 4;
        else if (BeanModConfig.ING_DIFF_GREAT < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_FINE) ing_quantity = 3;
        else if (BeanModConfig.ING_DIFF_FINE < ing_diff && ing_diff <= BeanModConfig.ING_DIFF_POOR) ing_quantity = 2;
        else ing_quantity = 1;

        // ===== Ingredient Quality =====

        int ing_rating_total = 0;
        int num_dishes = 0;
        for (ItemStack ingredient : ingredients) {
            if (ingredient.getItem() instanceof DishItem) {
                DishStats dishStats = DishUtils.getDishStats(ingredient);
                System.out.println(dishStats.getRating());
                ing_rating_total += dishStats.getRating();
                num_dishes++;
            }
        }
        System.out.println("================");
        System.out.println(ing_rating_total);
        System.out.println(num_dishes);

        float ing_rating_ratio;
        if (num_dishes == 0) ing_rating_ratio = 0;
        else ing_rating_ratio = ing_rating_total / (BeanModConfig.MAX_RATING * (float)num_dishes);
        System.out.println(ing_rating_ratio);

        int ing_quality = (int)(ing_rating_ratio * BeanModConfig.MAX_RATING);
        System.out.println(ing_quality);

        // ===== Cooking Quality =====

        int cook_quality;
        int cook_diff = Math.abs(cookingRecipe.getCookTime() - cookTime);

        if (cook_diff == 0) cook_quality = 5;
        else if (0 < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_GREAT) cook_quality = 4;
        else if (BeanModConfig.COOK_DIFF_GREAT < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_FINE) cook_quality = 3;
        else if (BeanModConfig.COOK_DIFF_FINE < cook_diff && cook_diff <= BeanModConfig.COOK_DIFF_POOR) cook_quality = 2;
        else cook_quality = 1;

        // ===== Final Rating =====

        float ratio;
        if (ing_quality == 0) ratio = (ing_quantity + cook_quality) / (BeanModConfig.MAX_RATING * 2.0f);
        else ratio = (ing_quantity + ing_quality + cook_quality) / (BeanModConfig.MAX_RATING * 3.0f);

        int rating = (int)(ratio * BeanModConfig.MAX_RATING);

        // TODO: Remove debug print statements
        System.out.println("-------------");
        System.out.println("ing quantity: " + ing_quantity);
        System.out.println("ing quality: " + ing_quality);
        System.out.println("cook quality: " + cook_quality);
        System.out.println("ratio: " + ratio);
        System.out.println("rating: " + rating);

        this.rating = rating;
    }

    public void addTooltip(ItemStack stack, List<Component> tooltips) {
        super.addTooltip(stack, tooltips);
        CookingPotDishStats cookingStats = (CookingPotDishStats) DishUtils.getDishStats(stack);

        if (cookingStats.inProgress()) {
            NonNullList<ItemStack> ingredients = cookingStats.getIngredients();
            if (Screen.hasShiftDown()) {
                tooltips.add(new TextComponent("Ingredients:").withStyle(ChatFormatting.GRAY));
                for (ItemStack ingredient : ingredients) {
                    tooltips.add(new TextComponent("  -  " + ingredient.getDisplayName().getString() + " x" + ingredient.getCount())
                            .withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltips.add(new TextComponent("Ingredients: x" + ingredients.size()).withStyle(ChatFormatting.GRAY));
            }

            int cookTime = cookingStats.getCookTime();
            if (cookTime == 1) tooltips.add(new TextComponent("Cooked for 1 minute").withStyle(ChatFormatting.GRAY));
            else if (cookTime > 1) tooltips.add(new TextComponent("Cooked for " + cookTime + " minutes").withStyle(ChatFormatting.GRAY));
        }
    }
}
