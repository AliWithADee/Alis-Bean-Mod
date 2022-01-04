package io.github.aliwithadee.alisbeanmod.core.world.feature.ore;

import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModOrePlacements {

    // Placements
    public static final PlacedFeature TIN_ORE_UPPER = PlacementUtils.register("tin_ore_upper",
            ModOreFeatures.TIN_ORE.placed(commonOrePlacement(70,
                    HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(216)))));

    public static final PlacedFeature TIN_ORE_MIDDLE = PlacementUtils.register("tin_ore_middle",
            ModOreFeatures.TIN_ORE.placed(commonOrePlacement(9,
                    HeightRangePlacement.triangle(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(48)))));

    public static final PlacedFeature TIN_ORE_SMALL = PlacementUtils.register("tin_ore_small",
            ModOreFeatures.TIN_ORE_SMALL.placed(commonOrePlacement(9,
                    HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));

    // Placement methods
    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int veinsPerChunk, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(veinsPerChunk), placementModifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int veinsPerChunk, PlacementModifier placementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(veinsPerChunk), placementModifier);
    }
}
