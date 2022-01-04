package io.github.aliwithadee.alisbeanmod.core.events;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.data.loot.ItemFromGrassModifier;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = AlisBeanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerModifierSerializers(
            @Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {

        event.getRegistry().registerAll(
                new ItemFromGrassModifier.Serializer().setRegistryName(
                        new ResourceLocation(AlisBeanMod.MOD_ID, "wild_haricot_beans_from_grass"))
        );
    }

    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, level, pos, tintIndex) ->
                        level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1,
                GeneralBlocks.BREWING_CAULDRON_WATER.get());
    }
}
