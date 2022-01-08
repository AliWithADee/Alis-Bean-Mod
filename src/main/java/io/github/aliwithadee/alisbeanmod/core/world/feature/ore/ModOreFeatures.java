package io.github.aliwithadee.alisbeanmod.core.world.feature.ore;

import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.List;

public class ModOreFeatures {

    public static final ConfiguredFeature<?, ?> TIN_ORE = FeatureUtils.register("tin_ore",
            Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.TIN_ORE.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.TIN_ORE.get().defaultBlockState())),
                    8)));

    public static final ConfiguredFeature<?, ?> TIN_ORE_SMALL = FeatureUtils.register("tin_ore_small",
            Feature.ORE.configured(new OreConfiguration(List.of(
                    OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.TIN_ORE.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.TIN_ORE.get().defaultBlockState())),
                    4)));
}
