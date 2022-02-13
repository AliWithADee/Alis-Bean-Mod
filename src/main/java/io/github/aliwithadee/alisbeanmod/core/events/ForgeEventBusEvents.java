package io.github.aliwithadee.alisbeanmod.core.events;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol.AlcoholCapabilityAttacher;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol.CapabilityAlcohol;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.alcohol.IAlcoholCapability;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModConfig;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlisBeanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {
    public static int playerTicks = 0;

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        AlcoholCapabilityAttacher.attach(event);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            if (event.phase == TickEvent.Phase.END) {
                playerTicks++;
            } else if (event.phase == TickEvent.Phase.START) {
                LazyOptional<IAlcoholCapability> cap = event.player.getCapability(CapabilityAlcohol.ALCOHOL_CAPABILITY);
                cap.ifPresent((alcoholCap) -> {
                    if (alcoholCap.getAlcohol() > 0) {
                        if (playerTicks >= BeanModConfig.ALCOHOL_DECREASE_TICKS) {
                            playerTicks = 0;
                            alcoholCap.removeAlcohol(1f);
                            System.out.println("Alcohol decreased to: " + alcoholCap.getAlcohol());
                        }
                    }
                });
            }
        }
    }
}
