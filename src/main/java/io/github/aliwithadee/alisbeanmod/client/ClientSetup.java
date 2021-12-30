package io.github.aliwithadee.alisbeanmod.client;

import io.github.aliwithadee.alisbeanmod.client.gui.general.CanningMachineScreen;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralBlocks;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void setup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register containers with their corresponding screens
            MenuScreens.register(GeneralContainers.CANNING_MACHINE_CONTAINER.get(), CanningMachineScreen::new);

            // Set crops and other plant blocks to render layer cut out
            ItemBlockRenderTypes.setRenderLayer(GeneralBlocks.HARICOT_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(GeneralBlocks.WILD_HARICOT_CROP.get(), RenderType.cutout());
        });
    }
}
