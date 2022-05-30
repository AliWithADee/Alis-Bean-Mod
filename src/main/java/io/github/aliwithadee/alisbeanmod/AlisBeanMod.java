package io.github.aliwithadee.alisbeanmod;

import io.github.aliwithadee.alisbeanmod.client.ClientSetup;
import io.github.aliwithadee.alisbeanmod.core.init.*;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModCommonConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AlisBeanMod.MOD_ID)
public class AlisBeanMod {
    public static final String MOD_ID = "alisbeanmod";

    private static final Logger LOGGER = LogManager.getLogger();

    public AlisBeanMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registries
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModContainers.register(eventBus);
        ModRecipeTypes.register(eventBus);

        // Register the setup method for mod loading
        eventBus.addListener(this::setup);
        eventBus.addListener(ClientSetup::setup);

        // Config file
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BeanModCommonConfig.SPEC, MOD_ID + ".toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("IT'S BEAN TIME!");
    }
}
