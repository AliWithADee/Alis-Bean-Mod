package io.github.aliwithadee.alisbeanmod.core.brewery.drink;

import io.github.aliwithadee.alisbeanmod.core.util.BeanModConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Drink {
    private final String name;
    private final int color;
    private final int rating;
    private final DrinkRecipe recipe;
    private final NonNullList<ItemStack> ingredients;
    private final int cookTime;
    private final int distills;
    private final int barrelAge;

    // Create drink after cooking
    public Drink(DrinkRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime) {
        this(recipe.getCookingResult().getName(), recipe.getCookingResult().getColor(), 0, recipe, ingredients,
                cookTime, 0, 0);
    }

    // Create finished drink from base drink
    public Drink(BaseDrink base) {
        this(base, 1);
    }

    // Create finished drink from base drink with specified rating
    public Drink(BaseDrink base, int rating) {
        this(base.getName(), base.getColor(), rating, null, null, 0, 0, 0);
    }

    public Drink(String name, int color, int rating, DrinkRecipe recipe, NonNullList<ItemStack> ingredients,
                 int cookTime, int distills, int barrelAge) {
        this.name = name;
        this.color = color;
        this.rating = rating;
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.cookTime = cookTime;
        this.distills = distills;
        this.barrelAge = barrelAge;
    }

    public String getName() {
        return this.name;
    }

    public int getColor() {
        return this.color;
    }

    public int getRating() {
        return this.rating;
    }

    public float getStrength() {
        float baseStrength = ModDrinks.getDrink(this.name).getStrength();
        System.out.println("Base Strength: " + baseStrength);
        float ratingModifier = (float) this.rating / BeanModConfig.MAX_RATING;

        System.out.println("Rating modifier: " + ratingModifier);
        float strength = baseStrength * ratingModifier;
        System.out.println("Strength: " + strength);
        return strength;
    }

    public List<MobEffectInstance> getEffects() {
        List<MobEffectInstance> newList = new ArrayList<>();
        float ratingModifier = (float) this.rating / BeanModConfig.MAX_RATING;

        List<MobEffectInstance> list = ModDrinks.getDrink(this.name).getEffects();
        System.out.println("List before: " + list);
        for (MobEffectInstance effectInstance : list) {
            int duration = effectInstance.getDuration();
            newList.add(new MobEffectInstance(effectInstance.getEffect(), (int) (duration * ratingModifier)));
        }
        System.out.println("List After: " + newList);

        return newList;
    }

    public boolean inProgress() {
        return recipe != null;
    }

    public boolean isGraded() {
        return rating > 0;
    }

    public DrinkRecipe getRecipe() {
        return this.recipe;
    }

    public NonNullList<ItemStack> getIngredients() {
        return this.ingredients;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public int getDistills() {
        return this.distills;
    }

    public int getBarrelAge() {
        return this.barrelAge;
    }

    public int getBarrelYears() {
        return this.barrelAge / BeanModConfig.MINUTES_PER_BARREL_YEAR;
    }
}
