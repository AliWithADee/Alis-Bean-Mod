package io.github.aliwithadee.alisbeanmod.core.brewery;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class Drink {
    private final String name;
    private final int colour;
    private final float strength;
    private final ImmutableList<MobEffectInstance> effects;
    private final int rating;

    public Drink(String name, int colour, float strength, MobEffectInstance... effects) {
        this(name, colour, strength, 5, List.of(effects));
    }

    private Drink(String name, int colour, float strength, int rating, List<MobEffectInstance> effects) {
        this.name = name;
        this.colour = colour;
        this.strength = strength;
        this.effects = ImmutableList.copyOf(effects);
        this.rating = rating > 0 && rating <= 5 ? rating : 1;
    }

    public Drink with(CompoundTag tag) {
        return new Drink(name, colour, strength, tag.getInt("Rating"), effects);
    }

    public String getName() {
        return name;
    }

    public int getColour() {
        return colour;
    }

    public float getStrength() {
        return strength;
    }

    public List<MobEffectInstance> getEffects() {
        return effects;
    }

    public int getRating() {
        return rating;
    }
}
