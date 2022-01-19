package io.github.aliwithadee.alisbeanmod.client;

import io.github.aliwithadee.alisbeanmod.client.gui.brewery.BrewingAlembicScreen;
import io.github.aliwithadee.alisbeanmod.client.gui.general.CanningMachineScreen;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import io.github.aliwithadee.alisbeanmod.core.init.ModContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void setup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register containers with their corresponding screens
            MenuScreens.register(ModContainers.CANNING_MACHINE_MENU.get(), CanningMachineScreen::new);
            MenuScreens.register(ModContainers.BREWING_ALEMBIC_MENU.get(), BrewingAlembicScreen::new);

            // Set crops and other plant blocks to render layer cut out
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.HARICOT_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_HARICOT_CROP.get(), RenderType.cutout());
        });
    }
}
