package io.github.aliwithadee.alisbeanmod.core.events;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol.CapabilityAlcohol;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.data.loot.ItemFromGrassModifier;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
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
                        new ResourceLocation(AlisBeanMod.MOD_ID, "wild_haricot_bean_from_grass"))
        );
    }

    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, level, pos, tintIndex) ->
                        level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1,
                ModBlocks.FILLED_COOKING_POT.get());
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, tintIndex) ->
                        tintIndex > 0 ? -1 : DrinkUtils.getDrinkColor(stack),
                ModItems.DRINK.get());
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        CapabilityAlcohol.register(event);
    }
}
