package io.github.aliwithadee.alisbeanmod.core.brewery;

import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModDrinks {

    // ---------- Drinks ----------

    private static final Map<String, Drink> DRINKS = new HashMap<>();

    public static final Drink EMPTY = createDrink(new Drink("empty", 78953176, 0.0f));
    public static final Drink SCUFFED = createDrink(new Drink("scuffed", 78953176, 0.0f,
            new MobEffectInstance(MobEffects.POISON)));
    public static final Drink BEER = createDrink(new Drink("beer", 12421704, 3.0f));
    public static final Drink CIDER = createDrink(new Drink("cider", 12080187, 2.0f));
    public static final Drink DARK_CIDER = createDrink(new Drink("dark_cider", 9850939, 2.0f));

    private static Drink createDrink(Drink drink) {
        DRINKS.put(drink.getName(), drink);
        return drink;
    }

    public static Map<String, Drink> getDrinks() {
        return DRINKS;
    }

    // ---------- Recipes ----------

    private static final Map<String, DrinkRecipe> RECIPES = new HashMap<>();

    public static final DrinkRecipe BEER_RECIPE = createRecipe(new DrinkRecipe(BEER,
            ModItems.GRAPE_MUST.get(), 3, 5,
            new ItemStack(Items.WHEAT, 6),
            new ItemStack(Items.SUGAR, 4)));

    private static DrinkRecipe createRecipe(DrinkRecipe recipe) {
        RECIPES.put(recipe.getDrink().getName(), recipe);
        return recipe;
    }

    public static Map<String, DrinkRecipe> getRecipes() {
        return RECIPES;
    }
}
