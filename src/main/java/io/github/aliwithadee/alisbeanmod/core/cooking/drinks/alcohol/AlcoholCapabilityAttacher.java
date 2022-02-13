package io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AlcoholCapabilityAttacher {

    private static class AlcoholCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation IDENTIFIER = new ResourceLocation(AlisBeanMod.MOD_ID + "alcohol_capability");

        private final IAlcoholCapability alcohol = new AlcoholCapability();
        private final LazyOptional<IAlcoholCapability> optionalAlcohol = LazyOptional.of(() -> alcohol);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CapabilityAlcohol.ALCOHOL_CAPABILITY.orEmpty(cap, this.optionalAlcohol);
        }

        void invalidate() {
            this.optionalAlcohol.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.alcohol.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            this.alcohol.deserializeNBT(tag);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player)) return;
        final AlcoholCapabilityProvider provider = new AlcoholCapabilityProvider();

        event.addCapability(AlcoholCapabilityProvider.IDENTIFIER, provider);
    }
}
