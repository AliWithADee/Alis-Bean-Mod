package io.github.aliwithadee.alisbeanmod.core.brewery.alcohol;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAlcoholCapability extends INBTSerializable<CompoundTag> {
    float getAlcohol();
    void setAlcohol(float alcohol);
    void addAlcohol(float alcohol);
    void removeAlcohol(float alcohol);
    Vec3 getIntoxicatedPosition();
    void setIntoxicatedPosition(Vec3 position);
}
