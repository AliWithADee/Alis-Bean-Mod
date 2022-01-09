package io.github.aliwithadee.alisbeanmod.core.brewery;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class Drink {
    public static final Drink EMPTY = new Drink("empty", 78953176, 0.0f);

    private final String name;
    private final int color;
    private final float strength;
    private final ImmutableList<MobEffectInstance> effects;
    private final int rating;

    public Drink(String name, int colour, float strength, MobEffectInstance... effects) {
        this(name, colour, strength, 0, List.of(effects));
    }

    private Drink(String name, int color, float strength, int rating, List<MobEffectInstance> effects) {
        this.name = name;
        this.color = color;
        this.strength = strength;
        this.effects = ImmutableList.copyOf(effects);
        this.rating = rating > 0 && rating <= 5 ? rating : 1;
    }

    public Drink with(CompoundTag tag) {
        return new Drink(name, color, strength, tag.getInt("Rating"), effects);
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

    public int getRating() {
        return rating;
    }
}
