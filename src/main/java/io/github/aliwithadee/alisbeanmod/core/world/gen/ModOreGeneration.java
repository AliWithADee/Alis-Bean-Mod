package io.github.aliwithadee.alisbeanmod.core.world.gen;

import io.github.aliwithadee.alisbeanmod.core.world.feature.ore.ModOrePlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.function.Supplier;

public class ModOreGeneration {
    public static void generateOres(final BiomeLoadingEvent event) {
        List<Supplier<PlacedFeature>> base =
                event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        // Tin Ore
        base.add(() -> ModOrePlacements.TIN_ORE_UPPER);
        base.add(() -> ModOrePlacements.TIN_ORE_MIDDLE);
        base.add(() -> ModOrePlacements.TIN_ORE_SMALL);
    }
}
