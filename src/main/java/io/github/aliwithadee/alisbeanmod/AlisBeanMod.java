package io.github.aliwithadee.alisbeanmod;

import io.github.aliwithadee.alisbeanmod.client.ClientSetup;
import io.github.aliwithadee.alisbeanmod.core.init.general.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AlisBeanMod.MOD_ID)
public class AlisBeanMod {

    public static final String MOD_ID = "alisbeanmod";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public AlisBeanMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // General Module
        GeneralItems.register(eventBus);
        GeneralBlocks.register(eventBus);
        GeneralBlockEntities.register(eventBus);
        GeneralContainers.register(eventBus);
        GeneralRecipeTypes.register(eventBus);

        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        eventBus.addListener(ClientSetup::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("IT'S BEAN TIME!");
    }
}
