package io.github.aliwithadee.alisbeanmod.core.cooking.dishes;

import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModDishes {
    public static final DishStats DEFAULT_STATS = new DishStats(1);
    public static final Map<String, DishRecipe> RECIPES = new HashMap<>();

    public static final DishRecipe CHILI_CON_CARNE_RECIPE = createDishRecipe(
            new DishRecipe(ModItems.CHILI_CON_CARNE.get(), 2,
                    new ItemStack(Items.EMERALD, 12), new ItemStack(Items.EGG, 4)));

    // ---------- Methods ----------

    private static DishRecipe createDishRecipe(DishRecipe recipe) {
        RECIPES.put(recipe.getName(), recipe);
        return recipe;
    }

    public static DishRecipe getDishRecipe(String name) {
        return RECIPES.get(name);
    }
}
