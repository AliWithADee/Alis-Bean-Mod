package io.github.aliwithadee.alisbeanmod.core.cooking.dish;

import io.github.aliwithadee.alisbeanmod.core.cooking.dish.recipe.CookingRecipe;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.recipe.DishRecipe;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.stats.DishStats;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModDishes {
    public static final DishStats DEFAULT_STATS = new DishStats(1);
    public static final DishStats PERFECT_STATS = new DishStats(5);
    public static final Map<String, CookingRecipe> COOKING_RECIPES = new HashMap<>();

    public static final DishRecipe BOWL_OF_RICE_RECIPE = createCookingRecipe(
            new CookingRecipe(ModItems.BOWL_OF_RICE.get(), 1,
                    new ItemStack(Items.BONE_MEAL, 9)));

    public static final DishRecipe CHILI_CON_CARNE_RECIPE = createCookingRecipe(
            new CookingRecipe(ModItems.CHILI_CON_CARNE.get(), 2,
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.EGG, 4),
                    new ItemStack(ModItems.BOWL_OF_RICE.get(), 1)));


    // ---------- Methods ----------

    private static CookingRecipe createCookingRecipe(CookingRecipe recipe) {
        COOKING_RECIPES.put(recipe.getName(), recipe);
        return recipe;
    }

    public static DishRecipe getDishRecipe(String name) {
        if (COOKING_RECIPES.containsKey(name)) {
            return COOKING_RECIPES.get(name);
        }
        return null;
    }
}
