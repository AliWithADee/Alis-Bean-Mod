package io.github.aliwithadee.alisbeanmod.common.general.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class MultiuseItem extends Item {

    public MultiuseItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        if(copy.hurt(1, new Random(), null)) {
            return ItemStack.EMPTY;
        } else {
            return copy;
        }
        // TODO: Break sound? Use events?
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}
