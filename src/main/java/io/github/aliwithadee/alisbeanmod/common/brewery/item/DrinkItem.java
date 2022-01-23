package io.github.aliwithadee.alisbeanmod.common.brewery.item;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.brewery.alcohol.CapabilityAlcohol;
import io.github.aliwithadee.alisbeanmod.core.brewery.alcohol.IAlcoholCapability;
import io.github.aliwithadee.alisbeanmod.core.brewery.drink.Drink;
import io.github.aliwithadee.alisbeanmod.core.brewery.drink.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.brewery.drink.ModDrinks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrinkItem extends Item {
    private static final int USE_DURATION = 32;

    public DrinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return DrinkUtils.setDrink(super.getDefaultInstance(), ModDrinks.DEFAULT);
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
        System.out.println(player.getItemInHand(hand).getTag()); // TODO: Remove debug print statements

        // TODO: Remove debug
        System.out.println("\n");
        LazyOptional<IAlcoholCapability> cap = player.getCapability(CapabilityAlcohol.ALCOHOL_CAPABILITY);
        cap.ifPresent((alcohol) -> {
            System.out.println("Alcohol: " + alcohol.getAlcohol());
            alcohol.addAlcohol(1);
        });

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Player player = entity instanceof Player ? (Player)entity : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
        }

        if (!level.isClientSide) {
            for(MobEffectInstance effectInstance : DrinkUtils.getDrink(stack).getEffects()) {
                if (effectInstance.getEffect().isInstantenous()) {
                    effectInstance.getEffect().applyInstantenousEffect(player, player, entity, effectInstance.getAmplifier(), 1.0D);
                } else {
                    entity.addEffect(new MobEffectInstance(effectInstance));
                }
            }
        }

        // TODO: Alcohol level system with Capabilities

        if (player != null && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        if (player == null || !player.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if (player != null) {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        level.gameEvent(entity, GameEvent.DRINKING_FINISH, entity.eyeBlockPosition());
        return stack;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return "drink." + AlisBeanMod.MOD_ID + "." + DrinkUtils.getDrink(stack).getName();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        DrinkUtils.addDrinkTooltip(stack, tooltips);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)) {
            ModDrinks.DRINKS.forEach((name, baseDrink) -> {
                stacks.add(DrinkUtils.createDrinkItem(new Drink(baseDrink)));
            });
        }
    }
}
