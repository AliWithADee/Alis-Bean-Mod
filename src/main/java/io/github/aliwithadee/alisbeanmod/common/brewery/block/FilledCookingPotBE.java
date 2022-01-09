package io.github.aliwithadee.alisbeanmod.common.brewery.block;

import io.github.aliwithadee.alisbeanmod.core.brewery.DrinkRecipe;
import io.github.aliwithadee.alisbeanmod.core.brewery.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.brewery.ModDrinks;
import io.github.aliwithadee.alisbeanmod.core.brewery.PartialDrink;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class FilledCookingPotBE extends BlockEntity {

    private final NonNullList<ItemStack> stacksInPot = NonNullList.create();
    private boolean cooking = false;
    private int ticks = 0;
    private int minutes = 0;

    public FilledCookingPotBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILLED_COOKING_POT_BE.get(), pos, state);
    }

    private boolean stackInList(ItemStack stack, NonNullList<ItemStack> list) {
        for (ItemStack itemStack : list) {
            if (stack.getItem() == itemStack.getItem()) {
                return true;
            }
        }
        return false;
    }

    private boolean canMakeRecipe(DrinkRecipe recipe) {
        for (ItemStack ingredient : recipe.getIngredients()) {
            if (!stackInList(ingredient, stacksInPot)) {
                return false;
            }
        }
        return true;
    }

    // Integer showing how far the recipe ingredients are away from the stacks the pot
    private int getRecipeDifference(DrinkRecipe recipe) {
        int diff = 0;
        for (ItemStack stack : stacksInPot) {
            int d = 0;
            if (stackInList(stack, recipe.getIngredients())) {
                d = Math.abs(stack.getCount() - recipe.getIngredientCount(stack));
            } else {
                d = stack.getCount();
            }
            diff += d;
        }
        return diff;
    }

    public ItemStack getResult() {
        if (stacksInPot.isEmpty()) return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);

        int DIFF_FOR_SCUFFED = 25;
        Map<String, DrinkRecipe> recipes = ModDrinks.getRecipes();

        DrinkRecipe result = null;
        int bestDiff = 0;
        for (DrinkRecipe recipe : recipes.values()) {
            if (canMakeRecipe(recipe)) {
                int thisDiff = getRecipeDifference(recipe);
                if (result != null) {
                    if (thisDiff < bestDiff) {
                        result = recipe;
                        bestDiff = thisDiff;
                    }
                } else if (thisDiff < DIFF_FOR_SCUFFED) {
                    result = recipe;
                    bestDiff = thisDiff;
                }
            }
        }
        if (result == null) return DrinkUtils.createDrinkItem(ModDrinks.SCUFFED);

        return DrinkUtils.createPartialDrinkItem(new PartialDrink(result, stacksInPot, minutes));
    }

    public void tickServer(FilledCookingPotBE blockEntity) {
        if (level.isClientSide())
            return;

        if (cooking) {
            ticks++;
            minutes = (ticks / 20) / 60;
        }
    }

    public void addIngredient(ItemStack stack) {
        ItemStack current = ItemStack.EMPTY;

        int index = -1;
        for (int i = 0; i < stacksInPot.size(); i++) {
            ItemStack other = stacksInPot.get(i);
            if (stack.sameItem(other)) {
                current = other;
                index = i;
            }
        }

        if (index == -1) {
            stacksInPot.add(stack);
        } else {
            stacksInPot.set(index, new ItemStack(stack.getItem(), current.getCount() + stack.getCount()));
        }
        System.out.println(stack);
        System.out.println(index);
        System.out.println(stacksInPot);
    }

    public boolean isCooking() {
        return cooking;
    }

    public void startCooking() {
        cooking = true;
        ticks = 0;
        minutes = 0;
    }
}
