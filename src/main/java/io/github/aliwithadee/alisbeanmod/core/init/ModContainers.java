package io.github.aliwithadee.alisbeanmod.core.init;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.cooking.menu.BrewingAlembicMenu;
import io.github.aliwithadee.alisbeanmod.common.general.menu.CanningMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
    public static DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, AlisBeanMod.MOD_ID);

    public static final RegistryObject<MenuType<CanningMachineMenu>> CANNING_MACHINE_MENU =
            ModContainers.CONTAINERS.register("canning_machine_menu",
                    () -> IForgeMenuType.create(((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        Level level = inv.player.getCommandSenderWorld();
                        return new CanningMachineMenu(windowId, level, pos, inv, inv.player);
                    })));

    public static final RegistryObject<MenuType<BrewingAlembicMenu>> BREWING_ALEMBIC_MENU =
            ModContainers.CONTAINERS.register("brewing_alembic_menu",
                    () -> IForgeMenuType.create(((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        Level level = inv.player.getCommandSenderWorld();
                        return new BrewingAlembicMenu(windowId, level, pos, inv, inv.player);
                    })));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
