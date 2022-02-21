package io.github.aliwithadee.alisbeanmod.core.cooking.dishes;

import io.github.aliwithadee.alisbeanmod.common.cooking.item.DishItem;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class DishRecipe {
    private final DishItem result;
    private final DishItem cookingResult;
    private final NonNullList<ItemStack> ingredients;
    private final int cookTime;

    public DishRecipe(DishItem result, int cookTime, ItemStack... ingredients) {
        this(result, null, cookTime, ingredients);
    }

    public DishRecipe(DishItem result, DishItem cookingResult, int cookTime, ItemStack... ingredients) {
        this.result = result;
        this.cookingResult = cookingResult;
        this.ingredients = NonNullList.of(new ItemStack(null), ingredients);
        this.cookTime = cookTime;
    }

    public String getName() {
        ResourceLocation regName = result.getRegistryName();
        if (regName == null) return null;

        return regName.toString();
    }

    public DishItem getResult() {
        return result;
    }

    public DishItem getCookingResult() {
        if (cookingResult == null) return result;
        return cookingResult;
    }

    public int getCookTime() {
        return cookTime;
    }

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
    }

    public int getIngredientCount(ItemStack ingredient) {
        for (ItemStack stack : ingredients) {
            if (stack.getItem() == ingredient.getItem()) {
                return stack.getCount();
            }
        }
        return 0;
    }
}
