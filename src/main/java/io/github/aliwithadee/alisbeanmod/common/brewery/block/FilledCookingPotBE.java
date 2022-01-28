package io.github.aliwithadee.alisbeanmod.common.brewery.block;

import io.github.aliwithadee.alisbeanmod.core.brewery.*;
import io.github.aliwithadee.alisbeanmod.core.brewery.drink.Drink;
import io.github.aliwithadee.alisbeanmod.core.brewery.drink.DrinkRecipe;
import io.github.aliwithadee.alisbeanmod.core.brewery.drink.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.brewery.drink.ModDrinks;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FilledCookingPotBE extends BlockEntity {
    private final NonNullList<ItemStack> stacksInPot = NonNullList.create();
    private boolean cooking = false;
    private int ticks = 0;
    private int minutes = 0;

    public FilledCookingPotBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILLED_COOKING_POT_BE.get(), pos, state);
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
        System.out.println(stacksInPot); // TODO: Remove debug print statements
    }

    private boolean canMakeRecipe(DrinkRecipe recipe) {
        for (ItemStack ingredient : recipe.getIngredients()) {
            if (!BreweryUtils.stackInList(ingredient, stacksInPot)) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getResult() {
        if (stacksInPot.isEmpty()) return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
        if (minutes == 0) return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE);

        DrinkRecipe resultRecipe = null;
        int bestDiff = 0;
        for (DrinkRecipe recipe : ModDrinks.RECIPES.values()) {
            if (canMakeRecipe(recipe)) {
                int thisDiff = BreweryUtils.getIngredientDifference(recipe, stacksInPot);
                if (resultRecipe != null) {
                    if (thisDiff < bestDiff) {
                        resultRecipe = recipe;
                        bestDiff = thisDiff;
                    }
                } else if (thisDiff <= BeanModConfig.MAX_RECIPE_DIFFERENCE) {
                    resultRecipe = recipe;
                    bestDiff = thisDiff;
                }
            }
        }
        if (resultRecipe == null) return DrinkUtils.createDrinkItem(new Drink(ModDrinks.SCUFFED));

        return DrinkUtils.createDrinkItem(new Drink(resultRecipe, stacksInPot, minutes));
    }

    public void tickServer(FilledCookingPotBE blockEntity) {
        if (level.isClientSide())
            return;

        boolean boiling = isBoiling();

        if (boiling) {
            if (isCooking()) {
                if (stacksInPot.isEmpty()) {
                    stopCooking();
                } else {
                    cook();
                }
            } else {
                if (!stacksInPot.isEmpty()) {
                    startCooking();
                }
            }
        } else {
            if (isCooking()) {
                stopCooking();
            }
        }

        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(FilledCookingPotBlock.BOILING) != boiling) {
            level.setBlock(worldPosition, state.setValue(FilledCookingPotBlock.BOILING, boiling), 3);
        }
    }

    private void cook() {
        int before = minutes;
        ticks++;
        minutes = (ticks) / 100; // TODO: Don't forget to set timer back to irl minutes
        if (minutes > before) System.out.println(minutes + " Minutes");
    }

    private boolean isBoiling() {
        Block block = level.getBlockState(worldPosition.below()).getBlock();
        return block == Blocks.FIRE || block == Blocks.LAVA || block == Blocks.MAGMA_BLOCK;
    }

    private boolean isCooking() {
        return cooking;
    }

    private void startCooking() {
        cooking = true;
    }

    private void stopCooking() {
        cooking = false;
    }
}
