package io.github.aliwithadee.alisbeanmod.core.cooking.drinks;

import com.google.common.collect.ImmutableList;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModCommonConfig;
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
        this.strength = (strength >= BeanModCommonConfig.MIN_STRENGTH.get() && strength <= BeanModCommonConfig.MAX_STRENGTH.get()) || strength == 0.0f ? strength : 1.0f;
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
