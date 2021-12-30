package io.github.aliwithadee.alisbeanmod.core.world.dimension;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ModDimensions {

    // The Beaniverse
    public static ResourceKey<Level> Beaniverse = ResourceKey.create(Registry.DIMENSION_REGISTRY,
            new ResourceLocation(AlisBeanMod.MOD_ID, "beaniverse"));
}
