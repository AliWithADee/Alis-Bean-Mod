package io.github.aliwithadee.alisbeanmod.core.init.general;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.block.*;
import io.github.aliwithadee.alisbeanmod.core.init.ModItemGroups;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class GeneralBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS, AlisBeanMod.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> TIN_ORE = registerBlock("tin_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .requiresCorrectToolForDrops().strength(2.0f)));

    public static final RegistryObject<Block> TIN_BLOCK = registerBlock("tin_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .requiresCorrectToolForDrops().strength(4.0f)));

    // Crops
    public static final RegistryObject<Block> HARICOT_CROP = registerBlockNoItem("haricot_crop",
            () -> new HaricotCropBlock(BlockBehaviour.Properties.of(Material.PLANT)
                    .noCollission().randomTicks().instabreak().sound(SoundType.CROP)));

    public static final RegistryObject<Block> WILD_HARICOT_CROP = registerBlockNoItem("wild_haricot_crop",
            () -> new WildHaricotCropBlock(BlockBehaviour.Properties.of(Material.PLANT)
                    .noCollission().randomTicks().instabreak().sound(SoundType.CROP)));

    // Machines
    public static final RegistryObject<Block> CANNING_MACHINE = registerMachine(
            "canning_machine", CanningMachineBlock::new);

    // Test
    public static final RegistryObject<Block> TEST_TELEPORT_BLOCK = registerBlock(
            "test_teleport_block", TestTeleportBlock::new);

    // Brewing
    public static final RegistryObject<Block> BREWING_CAULDRON = registerBlock(
            "brewing_cauldron", BrewingCauldronBlock::new);

    public static final RegistryObject<Block> BREWING_CAULDRON_WATER = registerBlockNoItem(
            "brewing_cauldron_water", BrewingCauldronWaterBlock::new);


    // ----------- Block registry Helper Methods -----------


    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> regObject = BLOCKS.register(name, block);
        registerBlockItem(name, regObject);

        return regObject;
    }

    private static <T extends Block>RegistryObject<T> registerMachine(String name, Supplier<T> block) {
        RegistryObject<T> regObject = BLOCKS.register(name, block);
        registerMachineItem(name, regObject);

        return regObject;
    }

    private static <T extends Block>RegistryObject<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    // ----------- Block Item registry Helper Methods -----------

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
