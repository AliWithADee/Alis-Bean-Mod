package io.github.aliwithadee.alisbeanmod.core.brewery;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModDrinks {
    private static final Map<String, Drink> DRINKS = new HashMap<>();
    private static final Map<String, PartialDrink> PARTIAL_DRINKS = new HashMap<>();
    private static final Map<String, DrinkRecipe> RECIPES = new HashMap<>();

    public static final Drink SCUFFED = createDrink(new Drink("scuffed", 78953176, 0.0f,
            new MobEffectInstance(MobEffects.POISON, 900)));
    public static final Drink BEER = createDrink(new Drink("beer", 12421704, 3.0f));
    public static final Drink CIDER = createDrink(new Drink("cider", 12080187, 2.0f));
    public static final Drink DARK_CIDER = createDrink(new Drink("dark_cider", 9850939, 2.0f));

    public static final PartialDrink GRAPE_MUST = createPartialDrink(new PartialDrink("grape_must", 12720968));

    public static final DrinkRecipe BEER_RECIPE = createRecipe(new DrinkRecipe(BEER, GRAPE_MUST, 3, 5,
            new ItemStack(Items.WHEAT, 6),
            new ItemStack(Items.SUGAR, 4)));

    // ---------- Methods ----------

    private static Drink createDrink(Drink drink) {
        DRINKS.put(drink.getName(), drink);
        return drink;
    }

    private static PartialDrink createPartialDrink(PartialDrink drink) {
        PARTIAL_DRINKS.put(drink.getName(), drink);
        return drink;
    }

    private static DrinkRecipe createRecipe(DrinkRecipe recipe) {
        RECIPES.put(recipe.getName(), recipe);
        return recipe;
    }

    public static Map<String, Drink> getDrinks() {
        return DRINKS;
    }

    public static Map<String, PartialDrink> getPartialDrinks() {
        return PARTIAL_DRINKS;
    }

    public static Map<String, DrinkRecipe> getRecipes() {
        return RECIPES;
    }
}
