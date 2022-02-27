package io.github.aliwithadee.alisbeanmod.core.cooking.dish.recipe;

import io.github.aliwithadee.alisbeanmod.common.cooking.item.DishItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class CookingRecipe extends DishRecipe {
    private final int cookTime;
    private final NonNullList<ItemStack> ingredients;

    public CookingRecipe(DishItem result, int cookTime, ItemStack... ingredients) {
        super(result);
        this.cookTime = cookTime;
        this.ingredients = NonNullList.of(new ItemStack(null), ingredients);
    }

    public int getCookTime() {
        return cookTime;
    }

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
    }
}
