package io.github.aliwithadee.alisbeanmod.client;

import io.github.aliwithadee.alisbeanmod.client.gui.general.CanningMachineScreen;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void setup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register containers with their corresponding screens
            MenuScreens.register(GeneralContainers.CANNING_MACHINE_CONTAINER.get(), CanningMachineScreen::new);
        });
    }
}
