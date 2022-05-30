package io.github.aliwithadee.alisbeanmod.core.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

import java.util.Map;

public class ModFoods {
    // TODO: Finish setting up foods
    public static final FoodProperties BLACK_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties BLACK_EYED_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties BROAD_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties COFFEE_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties HARICOT_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties KIDNEY_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties RATTLESNAKE_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();
    public static final FoodProperties RUNNER_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).fast().build();

    public static final FoodProperties TOMATO = new FoodProperties.Builder().nutrition(3).saturationMod(0.6f).build();
    public static final FoodProperties CORN = new FoodProperties.Builder().nutrition(3).saturationMod(0.6f).build();
    public static final FoodProperties BOWL_OF_BAKED_BEANS = new FoodProperties.Builder().nutrition(6).saturationMod(0.6f).build();
    public static final FoodProperties CAN_OF_BAKED_BEANS = new FoodProperties.Builder().nutrition(8).saturationMod(0.9f).build();

    // Dishes
    public static final FoodProperties DEFAULT_DISH_PROPERTIES = new FoodProperties.Builder().nutrition(2).saturationMod(0.1f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 500), 1.0f).build();

    public static final Map<Integer, FoodProperties> SCUFFED_CUISINE = Map.of(
            1, DEFAULT_DISH_PROPERTIES);

    public static final Map<Integer, FoodProperties> CHILI_CON_CARNE = Map.of(
            1, DEFAULT_DISH_PROPERTIES,
            2, new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 500), 1.0f).build(),
            3, new FoodProperties.Builder().nutrition(6).saturationMod(0.3f)
                    .effect(() -> new MobEffectInstance(MobEffects.JUMP, 500), 1.0f).build(),
            4, new FoodProperties.Builder().nutrition(8).saturationMod(0.4f)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 500), 1.0f).build(),
            5, new FoodProperties.Builder().nutrition(10).saturationMod(0.5f)
                    .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 500), 1.0f).build());
}
