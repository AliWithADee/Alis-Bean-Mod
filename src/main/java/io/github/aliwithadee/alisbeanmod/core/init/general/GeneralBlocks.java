package io.github.aliwithadee.alisbeanmod.core.init.general;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.block.CanningMachineBlock;
import io.github.aliwithadee.alisbeanmod.core.init.ModItemGroups;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class GeneralBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, AlisBeanMod.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> TIN_ORE = registerBlock("tin_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(3f).requiresCorrectToolForDrops()), false);

    public static final RegistryObject<Block> TIN_BLOCK = registerBlock("tin_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops()), false);

    // Machines
    public static final RegistryObject<Block> CANNING_MACHINE = registerBlock("canning_machine",
            CanningMachineBlock::new, true);


    // ----------- Helper Methods -----------


    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean machine) {
        RegistryObject<T> regObject = BLOCKS.register(name, block);

        if (machine) registerMachineItem(name, regObject);
        else registerBlockItem(name, regObject);

        return regObject;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        GeneralItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GENERAL)));
    }

    private static <T extends Block> void registerMachineItem(String name, RegistryObject<T> block) {
        GeneralItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GENERAL).stacksTo(1))); // TODO: Machines Tab?
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
