package io.github.aliwithadee.alisbeanmod.core.init;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.brewery.item.DrinkItem;
import io.github.aliwithadee.alisbeanmod.common.general.item.CanFoodItem;
import io.github.aliwithadee.alisbeanmod.common.general.item.MultiuseItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AlisBeanMod.MOD_ID);

    // ---------- Tabs ----------

    public static final CreativeModeTab GENERAL_TAB = new CreativeModeTab("alis_bean_mod_general") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(HARICOT_BEANS.get());
        }
    };

    public static final CreativeModeTab BREWERY_TAB = new CreativeModeTab("alis_bean_mod_brewery") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.COOKING_POT.get());
        }
    };

    // ---------- Items ----------

    public static final RegistryObject<Item> WILD_HARICOT_BEANS = ITEMS.register("wild_haricot_beans",
            () -> new ItemNameBlockItem(ModBlocks.WILD_HARICOT_CROP.get(), new Item.Properties()
                    .tab(GENERAL_TAB).food(ModFoods.HARICOT_BEANS)));

    public static final RegistryObject<Item> HARICOT_BEANS = ITEMS.register("haricot_beans",
            () -> new ItemNameBlockItem(ModBlocks.HARICOT_CROP.get(), new Item.Properties()
                    .tab(GENERAL_TAB).food(ModFoods.HARICOT_BEANS)));

    public static final RegistryObject<Item> BOWL_OF_HARICOT_BEANS = ITEMS.register("bowl_of_haricot_beans",
            () -> new Item(new Item.Properties().tab(GENERAL_TAB)
                    .stacksTo(1).craftRemainder(Items.BOWL)));

    public static final RegistryObject<Item> BOILING_HARICOT_BEANS = ITEMS.register("boiling_haricot_beans",
            () -> new Item(new Item.Properties().tab(GENERAL_TAB)
                    .stacksTo(1).craftRemainder(Items.BOWL)));

    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
            () -> new Item(new Item.Properties().tab(GENERAL_TAB).food(ModFoods.TOMATO)));

    public static final RegistryObject<Item> CORN = ITEMS.register("corn",
            () -> new Item(new Item.Properties().tab(GENERAL_TAB).food(ModFoods.CORN)));

    public static final RegistryObject<Item> MORTAR_AND_PESTLE = ITEMS.register("mortar_and_pestle",
            () -> new MultiuseItem(new Item.Properties().tab(GENERAL_TAB)
                    .stacksTo(1).defaultDurability(64)));

    public static final RegistryObject<Item> CORN_FLOUR = ITEMS.register("corn_flour",
            () -> new Item(new Item.Properties().tab(GENERAL_TAB)));

    public static final RegistryObject<Item> TOMATO_PASTE = ITEMS.register("tomato_paste",
            () -> new Item(new Item.Properties().tab(GENERAL_TAB)));

    public static final RegistryObject<Item> BOWL_OF_BAKED_BEANS = ITEMS.register("bowl_of_baked_beans",
            () -> new BowlFoodItem(new Item.Properties().tab(GENERAL_TAB)
                    .food(ModFoods.BOWL_OF_BAKED_BEANS).stacksTo(1).craftRemainder(Items.BOWL)));

    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot",
            () -> new Item(new Item.Properties().tab(GENERAL_TAB)));

    public static final RegistryObject<Item> TIN_CAN = ITEMS.register("tin_can",
            () -> new Item(new Item.Properties().tab(GENERAL_TAB).stacksTo(16)));

    public static final RegistryObject<Item> CAN_OF_BAKED_BEANS = ITEMS.register("can_of_baked_beans",
            () -> new CanFoodItem(new Item.Properties().tab(GENERAL_TAB)
                    .food(ModFoods.CAN_OF_BAKED_BEANS).stacksTo(1).craftRemainder(TIN_CAN.get())));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
