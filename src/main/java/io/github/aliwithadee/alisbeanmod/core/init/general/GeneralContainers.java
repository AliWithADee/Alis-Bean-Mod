package io.github.aliwithadee.alisbeanmod.core.init.general;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.container.CanningMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GeneralContainers {

    public static DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, AlisBeanMod.MOD_ID);

    public static final RegistryObject<MenuType<CanningMachineContainer>> CANNING_MACHINE_CONTAINER =
            CONTAINERS.register("canning_machine_container",
                    () -> IForgeContainerType.create(((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        Level level = inv.player.getCommandSenderWorld();
                        return new CanningMachineContainer(windowId, level, pos, inv, inv.player);
                    })));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
