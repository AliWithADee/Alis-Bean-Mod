package io.github.aliwithadee.alisbeanmod.core.cooking.dishes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class DishStats {
    private int rating;
    private final DishRecipe recipe;
    private final NonNullList<ItemStack> ingredients;
    private final int cookTime;

    public DishStats(int rating) {
        this(rating, null, null, 0);
    }

    public DishStats(int rating, DishRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime) {
        this.rating = rating;
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.cookTime = cookTime;
    }

    public static DishStats fromCooking(DishRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime) {
        return new DishStats(0, recipe, ingredients, cookTime);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public DishRecipe getRecipe() {
        return recipe;
    }

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
    }

    public int getCookTime() {
        return cookTime;
    }

    // TODO: On dish item or on dish stats?
    public boolean inProgress() {
        return recipe != null;
    }

    public boolean isGraded() {
        return rating > 0;
    }
}
