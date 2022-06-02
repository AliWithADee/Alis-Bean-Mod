package io.github.aliwithadee.alisbeanmod.core.init;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.cooking.block.*;
import io.github.aliwithadee.alisbeanmod.common.general.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
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

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS, AlisBeanMod.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> TIN_ORE = registerBlock("tin_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .requiresCorrectToolForDrops().strength(2.0f)), ModItems.GENERAL_TAB);

    public static final RegistryObject<Block> TIN_BLOCK = registerBlock("tin_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .requiresCorrectToolForDrops().strength(4.0f)), ModItems.GENERAL_TAB);

    // Crops
    public static final RegistryObject<Block> HARICOT_CROP = registerBlockNoItem("haricot_crop",
            () -> new HaricotCropBlock(BlockBehaviour.Properties.of(Material.PLANT)
                    .noCollission().randomTicks().instabreak().sound(SoundType.CROP)));

    public static final RegistryObject<Block> WILD_HARICOT_CROP = registerBlockNoItem("wild_haricot_crop",
            () -> new WildHaricotCropBlock(BlockBehaviour.Properties.of(Material.PLANT)
                    .noCollission().randomTicks().instabreak().sound(SoundType.CROP)));

    // Obelisks
    public static final RegistryObject<Block> OBELISK = registerBlock(
            "obelisk", ObeliskBlock::new, ModItems.GENERAL_TAB);

    public static final RegistryObject<PlantedObeliskBlock> OBELISK_BLACK_BEANS = registerBlockNoItem(
            "obelisk_black_beans", PlantedObeliskBlock::new);

    public static final RegistryObject<PlantedObeliskBlock> OBELISK_BLACK_EYED_BEANS = registerBlockNoItem(
            "obelisk_black_eyed_beans", PlantedObeliskBlock::new);

    public static final RegistryObject<PlantedObeliskBlock> OBELISK_KIDNEY_BEANS = registerBlockNoItem(
            "obelisk_kidney_beans", PlantedObeliskBlock::new);

    public static final RegistryObject<PlantedObeliskBlock> OBELISK_RATTLESNAKE_BEANS = registerBlockNoItem(
            "obelisk_rattlesnake_beans", PlantedObeliskBlock::new);

    public static final RegistryObject<PlantedObeliskBlock> OBELISK_RUNNER_BEANS = registerBlockNoItem(
            "obelisk_runner_beans", PlantedObeliskBlock::new);

    // Machines
    public static final RegistryObject<Block> CANNING_MACHINE = registerNoStackBlock(
            "canning_machine", CanningMachineBlock::new, ModItems.GENERAL_TAB);

    // Brewing
    public static final RegistryObject<Block> COOKING_POT = registerBlock(
            "cooking_pot", CookingPotBlock::new, ModItems.COOKING_TAB);

    public static final RegistryObject<Block> FILLED_COOKING_POT = registerBlockNoItem(
            "filled_cooking_pot", FilledCookingPotBlock::new);

    public static final RegistryObject<Block> BREWING_ALEMBIC = registerBlock(
            "brewing_alembic", BrewingAlembicBlock::new, ModItems.COOKING_TAB);

    public static final RegistryObject<Block> AGEING_BARREL = registerBlock(
            "ageing_barrel", AgeingBarrelBlock::new, ModItems.COOKING_TAB);

    public static final RegistryObject<Block> SEALING_TABLE = registerBlock(
            "sealing_table", SealingTableBlock::new, ModItems.COOKING_TAB);

    // -------- Blocks --------

    public static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> regObject = BLOCKS.register(name, block);
        registerBlockItem(name, regObject, tab);
        return regObject;
    }

    public static <T extends Block>RegistryObject<T> registerNoStackBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> regObject = BLOCKS.register(name, block);
        registerNoStackItem(name, regObject, tab);
        return regObject;
    }

    public static <T extends Block>RegistryObject<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    // -------- Block Items --------

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    private static <T extends Block> void registerNoStackItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab).stacksTo(1)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
