package io.github.aliwithadee.alisbeanmod.core.init;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.brewery.block.FilledCookingPotBE;
import io.github.aliwithadee.alisbeanmod.common.general.block.CanningMachineBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, AlisBeanMod.MOD_ID);

    public static RegistryObject<BlockEntityType<CanningMachineBE>> CANNING_MACHINE_BE =
            BLOCK_ENTITIES.register("canning_machine_entity", () -> BlockEntityType.Builder.of(
                    CanningMachineBE::new, ModBlocks.CANNING_MACHINE.get()).build(null));

    public static RegistryObject<BlockEntityType<FilledCookingPotBE>> FILLED_COOKING_POT_BE =
            BLOCK_ENTITIES.register("filled_cooking_pot_entity", () -> BlockEntityType.Builder.of(
                    FilledCookingPotBE::new, ModBlocks.FILLED_COOKING_POT.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
