package io.github.aliwithadee.alisbeanmod.common.cooking.item;

import io.github.aliwithadee.alisbeanmod.core.cooking.dish.stats.DishStats;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.DishUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.ModDishes;
import io.github.aliwithadee.alisbeanmod.core.init.ModFoods;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
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

    public DishItem(Item.Properties itemProperties) {
        this(itemProperties, null);
    }

    public DishItem(Item.Properties itemProperties, Map<Integer, FoodProperties> foodPropertiesMap) {
        super(itemProperties.tab(ModItems.COOKING_TAB));
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

        if (isEdible()) {
            ItemStack stack = player.getItemInHand(hand);
            if (player.canEat(false)) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        DishStats dish = DishUtils.getDishStats(stack);
        FoodProperties foodProperties = getFoodProperties(dish.getRating());
        if (foodProperties != null) return foodProperties.isFastFood() ? 16 : 32;
        return 0;
    }

    @Override
    public boolean isEdible() {
        return this.foodPropertiesMap != null;
    }

    public FoodProperties getFoodProperties(int rating) {
        if (isEdible()) {
            if (!foodPropertiesMap.containsKey(rating)) return ModFoods.DEFAULT_DISH_PROPERTIES;
            return foodPropertiesMap.get(rating);
        }
        return null;
    }

    // Returns an item stack with the same item properties, plus the specified food properties
    public ItemStack createEdibleStack(FoodProperties foodProperties, int count) {
        Item.Properties newProperties = this.itemProperties.food(foodProperties);
        return new ItemStack(new DishItem(newProperties, this.foodPropertiesMap), count);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (isEdible()) {
            DishStats dishStats = DishUtils.getDishStats(stack);
            FoodProperties foodProperties = getFoodProperties(dishStats.getRating());

            ItemStack edibleStack = createEdibleStack(foodProperties, stack.getCount());
            livingEntity.eat(level, edibleStack);

            if (stack.getCount() <= 1 && stack.hasContainerItem()) return stack.getContainerItem();
            stack.setCount(edibleStack.getCount());
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        DishUtils.getDishStats(stack).addTooltip(stack, tooltips);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)) {
            stacks.add(DishUtils.createDishItem(this));
        }
    }
}
