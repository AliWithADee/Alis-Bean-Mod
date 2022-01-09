package io.github.aliwithadee.alisbeanmod.common.brewery.item;

import io.github.aliwithadee.alisbeanmod.core.brewery.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.brewery.PartialDrink;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PartialDrinkItem extends Item {
    private static final int USE_DURATION = 32;
    private final int color;

    public PartialDrinkItem(Properties properties, int color) {
        super(properties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return DrinkUtils.setPartialDrink(super.getDefaultInstance(), PartialDrink.EMPTY);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        DrinkUtils.addPartialDrinkTooltip(stack, tooltips, 1.0f);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
    }
}
