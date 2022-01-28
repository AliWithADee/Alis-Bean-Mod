package io.github.aliwithadee.alisbeanmod.core.brewery.alcohol;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class AlcoholCapability implements IAlcoholCapability {
    private static final String NBT_ALCOHOL = "alcohol";

    // TODO: Rename capability classes etc

    private float alcohol = 0;
    private Vec3 intoxicatedPosition = null;

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
    public Vec3 getIntoxicatedPosition() {
        return this.intoxicatedPosition;
    }

    @Override
    public void setIntoxicatedPosition(Vec3 position) {
        this.intoxicatedPosition = position;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putFloat(NBT_ALCOHOL, this.alcohol);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.alcohol = tag.getFloat(NBT_ALCOHOL);
    }
}
