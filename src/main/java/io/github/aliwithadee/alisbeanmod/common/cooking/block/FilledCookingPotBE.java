package io.github.aliwithadee.alisbeanmod.common.cooking.block;

import io.github.aliwithadee.alisbeanmod.core.cooking.CookingUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.DishUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.ModDishes;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.recipe.CookingPotDishRecipe;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.stats.CookingPotDishStats;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.Drink;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.DrinkRecipe;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.ModDrinks;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
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
        if (stack.hasTag()) {
            stacksInPot.add(stack);
        } else {
            ItemStack existing = ItemStack.EMPTY;
            int index = -1;

            for (int i = 0; i < stacksInPot.size(); i++) {
                ItemStack otherStack = stacksInPot.get(i);
                if (otherStack.sameItem(stack)) {
                    existing = otherStack;
                    index = i;
                }
            }

            if (index == -1) stacksInPot.add(stack);
            else stacksInPot.set(index, new ItemStack(stack.getItem(), existing.getCount() + stack.getCount()));
        }

        System.out.println(stacksInPot); // TODO: Remove debug print statements
    }

    private boolean containsIngredients(NonNullList<ItemStack> ingredients) {
        for (ItemStack ingredient : ingredients) {
            if (!CookingUtils.stackInList(ingredient, stacksInPot)) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getDrinkResult() {
        if (stacksInPot.isEmpty()) return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
        if (minutes == 0) return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.MUNDANE);

        DrinkRecipe resultRecipe = null;
        int bestDiff = 0;
        for (DrinkRecipe recipe : ModDrinks.RECIPES.values()) {
            if (containsIngredients(recipe.getIngredients())) {
                int thisDiff = CookingUtils.getIngredientError(recipe.getIngredients(), stacksInPot);
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

        Drink drink = Drink.fromCooking(resultRecipe, stacksInPot, minutes);

        if (!resultRecipe.requiresAgeing() && !resultRecipe.requiresDistilling()) {
            DrinkUtils.gradeDrink(drink);
        }

        return DrinkUtils.createDrinkItem(drink);
    }

    public ItemStack getDishResult() {
        if (stacksInPot.isEmpty()) return new ItemStack(Items.MUSHROOM_STEW); // TODO: Bowl of water
        if (minutes == 0) return new ItemStack(Items.MUSHROOM_STEW);

        CookingPotDishRecipe resultRecipe = null;
        int bestDiff = 0;
        for (CookingPotDishRecipe recipe : ModDishes.COOKING_POT_DISH_RECIPES.values()) {
            if (containsIngredients(recipe.getIngredients())) {
                int thisDiff = CookingUtils.getIngredientError(recipe.getIngredients(), stacksInPot);
                if (resultRecipe != null) {
                    if (thisDiff < bestDiff) {
                        resultRecipe = recipe;
                        bestDiff = thisDiff;
                    }
                } else if (thisDiff <= BeanModConfig.MAX_RECIPE_DIFFERENCE) { // TODO: Different config option?
                    resultRecipe = recipe;
                    bestDiff = thisDiff;
                }
            }
        }
        if (resultRecipe == null) return DishUtils.createDishItem(ModItems.SCUFFED_CUISINE.get());

        CookingPotDishStats cookingStats = new CookingPotDishStats(resultRecipe, stacksInPot, minutes);
        cookingStats.computeRating();

        return DishUtils.createDishItem(resultRecipe.getResult(), cookingStats);
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
