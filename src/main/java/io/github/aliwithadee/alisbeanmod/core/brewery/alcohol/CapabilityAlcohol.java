package io.github.aliwithadee.alisbeanmod.core.brewery.alcohol;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityAlcohol {
    public static final Capability<IAlcoholCapability> ALCOHOL_CAPABILITY = CapabilityManager.get(new CapabilityToken<IAlcoholCapability>() {
    });

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IAlcoholCapability.class);
    }
}
