package io.github.aliwithadee.alisbeanmod.common.cooking.item;

import io.github.aliwithadee.alisbeanmod.core.cooking.dishes.DishStats;
import io.github.aliwithadee.alisbeanmod.core.cooking.dishes.DishUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.dishes.ModDishes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DishItem extends Item {
    private final Item.Properties itemProperties;
    private final Map<Integer, FoodProperties> foodPropertiesMap;

    public DishItem(Item.Properties itemProperties, Map<Integer, FoodProperties> foodPropertiesMap) {
        super(itemProperties);
        this.itemProperties = itemProperties;
        this.foodPropertiesMap = foodPropertiesMap;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return DishUtils.setDishStats(super.getDefaultInstance(), ModDishes.DEFAULT_STATS);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            System.out.println(player.getItemInHand(hand).getTag()); // TODO: Remove debug print statements
        }

        ItemStack stack = player.getItemInHand(hand);
        if (player.canEat(false)) {
            player.startUsingItem(hand);
            System.out.println("Start");
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        DishStats dish = DishUtils.getDishStats(stack);
        FoodProperties foodProperties = getFoodProperties(dish.getRating());
        return foodProperties.isFastFood() ? 16 : 32;
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    public FoodProperties getFoodProperties(int rating) {
        if (!foodPropertiesMap.containsKey(rating)) return null;
        return foodPropertiesMap.get(rating);
    }

    // Returns an item stack with the same item properties, plus the specified food properties
    public ItemStack createEdibleStack(FoodProperties foodProperties, int count) {
        Item.Properties newProperties = this.itemProperties.food(foodProperties);
        return new ItemStack(new DishItem(newProperties, this.foodPropertiesMap), count);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        System.out.println("Finished");
        DishStats dish = DishUtils.getDishStats(stack);
        FoodProperties foodProperties = getFoodProperties(dish.getRating());

        ItemStack edibleStack = createEdibleStack(foodProperties, stack.getCount());
        System.out.println(edibleStack);
        System.out.println(livingEntity.eat(level, edibleStack));
        System.out.println(stack);
        stack.setCount(edibleStack.getCount());
        System.out.println(stack);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        DishUtils.addDishTooltip(stack, tooltips);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)) {
            stacks.add(DishUtils.createDishItem(this));
        }
    }
}
