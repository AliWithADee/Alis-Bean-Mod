package io.github.aliwithadee.alisbeanmod.core.init.general;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.item.CanFoodItem;
import io.github.aliwithadee.alisbeanmod.common.general.item.MultiuseItem;
import io.github.aliwithadee.alisbeanmod.core.init.ModFoods;
import io.github.aliwithadee.alisbeanmod.core.init.ModItemGroups;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GeneralItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AlisBeanMod.MOD_ID);

    public static final RegistryObject<Item> WILD_HARICOT_BEANS = ITEMS.register("wild_haricot_beans",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)
                    .food(ModFoods.HARICOT_BEANS)));

    public static final RegistryObject<Item> HARICOT_BEANS = ITEMS.register("haricot_beans",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)
                    .food(ModFoods.HARICOT_BEANS)));

    public static final RegistryObject<Item> BOWL_OF_HARICOT_BEANS = ITEMS.register("bowl_of_haricot_beans",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)
                    .stacksTo(1).craftRemainder(Items.BOWL)));

    public static final RegistryObject<Item> BOILING_HARICOT_BEANS = ITEMS.register("boiling_haricot_beans",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)
                    .stacksTo(1).craftRemainder(Items.BOWL)));

    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP).food(ModFoods.TOMATO)));

    public static final RegistryObject<Item> CORN = ITEMS.register("corn",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP).food(ModFoods.CORN)));

    public static final RegistryObject<Item> MORTAR_AND_PESTLE = ITEMS.register("mortar_and_pestle",
            () -> new MultiuseItem(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)
                    .stacksTo(1).defaultDurability(64)));

    public static final RegistryObject<Item> CORN_FLOUR = ITEMS.register("corn_flour",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)));

    public static final RegistryObject<Item> TOMATO_PASTE = ITEMS.register("tomato_paste",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)));

    public static final RegistryObject<Item> BOWL_OF_BAKED_BEANS = ITEMS.register("bowl_of_baked_beans",
            () -> new BowlFoodItem(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)
                    .food(ModFoods.BOWL_OF_BAKED_BEANS).stacksTo(1).craftRemainder(Items.BOWL)));

    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)));

    public static final RegistryObject<Item> TIN_CAN = ITEMS.register("tin_can",
            () -> new Item(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP).stacksTo(16)));

    public static final RegistryObject<Item> CAN_OF_BAKED_BEANS = ITEMS.register("can_of_baked_beans",
            () -> new CanFoodItem(new Item.Properties().tab(ModItemGroups.ALIS_BEAN_MOD_GROUP)
                    .food(ModFoods.CAN_OF_BAKED_BEANS).stacksTo(1).craftRemainder(TIN_CAN.get())));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
