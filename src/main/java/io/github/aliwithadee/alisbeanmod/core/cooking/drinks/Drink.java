package io.github.aliwithadee.alisbeanmod.core.cooking.drinks;

import io.github.aliwithadee.alisbeanmod.core.util.BeanModConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Drink {
    private String name;
    private int rating;
    private final DrinkRecipe recipe;
    private final NonNullList<ItemStack> ingredients;
    private final int cookTime;
    private int distills;
    private int barrelAge;

    // Create drink after cooking
    public Drink(DrinkRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime) {
        this(recipe.getCookingResult().getName(), 0, recipe, ingredients, cookTime, 0, 0);
    }

    // Create blank drink from a base drink
    public Drink(BaseDrink base) {
        this(base, 1);
    }

    // Create blank drink from a base drink with a specified rating
    public Drink(BaseDrink base, int rating) {
        this(base.getName(), rating, null, null, 0, 0, 0);
    }

    public Drink(String name, int rating, DrinkRecipe recipe, NonNullList<ItemStack> ingredients, int cookTime,
                 int distills, int barrelAge) {
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public void setDistills(int distills) {
        this.distills = distills;
    }

    public int getBarrelMinutes() {
        return this.barrelAge;
    }

    public void setBarrelMinutes(int age) {
        this.barrelAge = age;
    }

    public int getBarrelYears() {
        return this.barrelAge / BeanModConfig.MINUTES_PER_BARREL_YEAR;
    }

    public int getColor() {
        return ModDrinks.getBaseDrink(this.name).getColor();
    }

    public float getStrength() {
        // TODO: Remove debug
        float baseStrength = ModDrinks.getBaseDrink(this.name).getStrength();
        System.out.println("Base Strength: " + baseStrength);
        float ratingModifier = (float) this.rating / BeanModConfig.MAX_RATING;

        System.out.println("Rating modifier: " + ratingModifier);
        float strength = baseStrength * ratingModifier;
        System.out.println("Strength: " + strength);
        return strength;
    }

    public List<MobEffectInstance> getEffects() {
        // TODO: Remove debug
        List<MobEffectInstance> newList = new ArrayList<>();
        float ratingModifier = (float) this.rating / BeanModConfig.MAX_RATING;

        List<MobEffectInstance> list = ModDrinks.getBaseDrink(this.name).getEffects();
        System.out.println("List before: " + list);
        for (MobEffectInstance effectInstance : list) {
            int duration = effectInstance.getDuration();
            newList.add(new MobEffectInstance(effectInstance.getEffect(), (int) (duration * ratingModifier)));
        }
        System.out.println("List After: " + newList);

        return newList;
    }
}
