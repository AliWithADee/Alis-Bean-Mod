package io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol;

import net.minecraft.nbt.CompoundTag;

public class AlcoholCapability implements IAlcoholCapability {
    // TODO: Rename capability classes etc

    private float alcohol = 0;

    @Override
    public float getAlcohol() {
        return this.alcohol;
    }

    @Override
    public void setAlcohol(float alcohol) {
        this.alcohol = alcohol;
    }

    @Override
    public void addAlcohol(float alcohol) {
        this.alcohol += alcohol;
    }

    @Override
    public void removeAlcohol(float alcohol) {
        this.alcohol -= alcohol;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putFloat("alcohol", this.alcohol);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.alcohol = tag.getFloat("alcohol");
    }
}
