package io.github.aliwithadee.alisbeanmod.core.init;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties HARICOT_BEANS = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties COFFEE_BEANS = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties TOMATO = new FoodProperties.Builder().nutrition(3).saturationMod(0.6f).build();
    public static final FoodProperties CORN = new FoodProperties.Builder().nutrition(3).saturationMod(0.6f).build();
    public static final FoodProperties BOWL_OF_BAKED_BEANS = new FoodProperties.Builder().nutrition(6).saturationMod(0.6f).build();
    public static final FoodProperties CAN_OF_BAKED_BEANS = new FoodProperties.Builder().nutrition(8).saturationMod(0.9f).build();
}
