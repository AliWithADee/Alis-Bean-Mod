package io.github.aliwithadee.alisbeanmod.core.brewery;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class PartialDrink {
    public static final PartialDrink EMPTY = new PartialDrink(null, null, 0, 0, 0, true);

    private final DrinkRecipe recipe;
    private final NonNullList<ItemStack> ingredients;
    private final int cookTime;
    private final int distills;
    private final int barrelAge;
    private final boolean finished;

    public PartialDrink(DrinkRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime) {
        this(recipe, ingredients, cookTime, 0, 0, false);
    }

    public PartialDrink(DrinkRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime, int distills) {
        this(recipe, ingredients, cookTime, distills, 0, false);
    }

    public PartialDrink(DrinkRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime, int distills, int barrelAge) {
        this(recipe, ingredients, cookTime, distills, barrelAge, true);
    }

    public PartialDrink(DrinkRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime, int distills, int barrelAge,
                        boolean finished) {
        this.recipe = recipe;
        this.cookTime = cookTime;
        this.distills = distills;
        this.barrelAge = barrelAge;
        this.ingredients = ingredients;
        this.finished = finished;
    }

    public static PartialDrink with(CompoundTag tag) {
        return new PartialDrink(ModDrinks.getRecipes().get(tag.getString("Recipe")), DrinkUtils.getIngredients(tag),
                tag.getInt("CookTime"), tag.getInt("Distills"), tag.getInt("Age"),
                tag.getBoolean("Finished"));
    }

    public boolean isEmpty() {
        return recipe == null;
    }

    public DrinkRecipe getRecipe() {
        return recipe;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getDistills() {
        return distills;
    }

    public int getBarrelAge() {
        return barrelAge;
    }

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
    }

    public boolean isFinished() {
        return finished;
    }
}
