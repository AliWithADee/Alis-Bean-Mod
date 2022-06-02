package io.github.aliwithadee.alisbeanmod.core.cooking.drinks;

import io.github.aliwithadee.alisbeanmod.core.util.BeanModCommonConfig;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;
import java.util.Map;

public class BaseDrink {
    private final String name;
    private final int color;
    private final float alcoholStrength;
    private final Map<Integer, List<MobEffectInstance>> effects;

    public BaseDrink(String name, int colour) {
        this(name, colour, null);
    }

    public BaseDrink(String name, int colour, Map<Integer, List<MobEffectInstance>> effects) {
        this(name, colour, 0.0f, effects);
    }

    public BaseDrink(String name, int colour, float alcoholStrength) {
        this(name, colour, alcoholStrength, null);
    }

    public BaseDrink(String name, int color, float alcoholStrength, Map<Integer, List<MobEffectInstance>> effects) {
        this.name = name;
        this.color = color;
        this.alcoholStrength = (alcoholStrength >= BeanModCommonConfig.MIN_STRENGTH.get() && alcoholStrength <= BeanModCommonConfig.MAX_STRENGTH.get()) || alcoholStrength == 0.0f ? alcoholStrength : 1.0f;
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public float getAlcoholStrength() {
        return alcoholStrength;
    }

    public List<MobEffectInstance> getEffects(int rating) {
        return effects.get(rating);
    }
}
