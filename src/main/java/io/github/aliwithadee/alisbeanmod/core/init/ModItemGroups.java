package io.github.aliwithadee.alisbeanmod.core.init;

import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

    public static final CreativeModeTab ALIS_BEAN_MOD_GROUP = new CreativeModeTab("alis_bean_mod_tab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GeneralItems.HARICOT_BEANS.get());
        }
    };
}
