package io.github.aliwithadee.alisbeanmod.core.brewery.drink;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class BaseDrink {
    private final String name;
    private final int color;
    private final float strength;
    private final ImmutableList<MobEffectInstance> effects;

    public BaseDrink(String name, int colour, MobEffectInstance... effects) {
        this(name, colour, 0.0f, List.of(effects));
    }

    public BaseDrink(String name, int colour, float strength, MobEffectInstance... effects) {
        this(name, colour, strength, List.of(effects));
    }

    private BaseDrink(String name, int color, float strength, List<MobEffectInstance> effects) {
        this.name = name;
        this.color = color;
        this.strength = strength;
        this.effects = ImmutableList.copyOf(effects);
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public float getStrength() {
        return strength;
    }

    public List<MobEffectInstance> getEffects() {
        return effects;
    }
}
