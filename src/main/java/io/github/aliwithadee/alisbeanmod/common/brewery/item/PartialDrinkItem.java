package io.github.aliwithadee.alisbeanmod.common.brewery.item;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.brewery.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.brewery.ModDrinks;
import io.github.aliwithadee.alisbeanmod.core.brewery.PartialDrink;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PartialDrinkItem extends Item {
    public PartialDrinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return DrinkUtils.createPartialDrinkItem(PartialDrink.EMPTY);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return "drink." + AlisBeanMod.MOD_ID + "." + DrinkUtils.getPartialDrink(stack).getName();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        DrinkUtils.addPartialDrinkTooltip(stack, tooltips, 1.0f);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)) {
            ModDrinks.getPartialDrinks().forEach((name, drink) -> {
                stacks.add(DrinkUtils.createPartialDrinkItem(drink));
            });
        }
    }
}
