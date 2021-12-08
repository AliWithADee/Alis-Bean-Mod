package io.github.aliwithadee.alisbeanmod.core.init.general;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.block.CanningMachineBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GeneralBlockEntities {

    public static DeferredRegister<BlockEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, AlisBeanMod.MOD_ID);

    public static RegistryObject<BlockEntityType<CanningMachineBE>> CANNING_MACHINE_BE =
            TILE_ENTITIES.register("canning_machine_tile", () -> BlockEntityType.Builder.of(
                    CanningMachineBE::new, GeneralBlocks.CANNING_MACHINE.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
