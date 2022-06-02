package io.github.aliwithadee.alisbeanmod.core.cooking.drinks;

import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModDrinks {
    public static final Map<String, BaseDrink> DRINKS = new HashMap<>();
    public static final Map<String, DrinkRecipe> RECIPES = new HashMap<>();

    // Alcoholic Drinks
    public static final BaseDrink SCUFFED = createBaseDrink(new BaseDrink("scuffed", 78953176, 0.0f, Map.of(
            1, List.of(new MobEffectInstance(MobEffects.POISON, 500)),
            2, List.of(new MobEffectInstance(MobEffects.POISON, 500)),
            3, List.of(new MobEffectInstance(MobEffects.POISON, 500)),
            4, List.of(new MobEffectInstance(MobEffects.POISON, 500)),
            5, List.of(new MobEffectInstance(MobEffects.POISON, 500)))));
    public static final BaseDrink BEER = createBaseDrink(new BaseDrink("beer", 12421704, 1.0f));
    public static final BaseDrink CIDER = createBaseDrink(new BaseDrink("cider", 12080187, 1.0f));
    public static final BaseDrink DARK_CIDER = createBaseDrink(new BaseDrink("dark_cider", 9850939, 1.0f));

    // Non-Alcoholic Drinks
    public static final BaseDrink COFFEE = createBaseDrink(new BaseDrink("coffee", 0));

    // Cooking Results
    public static final BaseDrink GRAPE_MUST = createBaseDrink(new BaseDrink("grape_must", 12720968));

    // Default Drink
    public static final Drink DEFAULT = new Drink(SCUFFED);

    // Drink Recipes
    public static final DrinkRecipe CIDER_RECIPE = createDrinkRecipe(
            new DrinkRecipe(CIDER, GRAPE_MUST, 1, 3, 3,
            new ItemStack(Items.WHEAT, 6),
            new ItemStack(Items.SUGAR, 4)));

    public static final DrinkRecipe DARK_CIDER_RECIPE = createDrinkRecipe(
            new DrinkRecipe(DARK_CIDER, GRAPE_MUST, 2, 0, 3,
                    new ItemStack(Items.WHEAT, 13),
                    new ItemStack(Items.SUGAR, 2)));

    public static final DrinkRecipe BEER_RECIPE = createDrinkRecipe(
            new DrinkRecipe(BEER, GRAPE_MUST, 1, 3, 0,
                    new ItemStack(Items.WHEAT, 8),
                    new ItemStack(Items.SUGAR, 6)));

    public static final DrinkRecipe COFFEE_RECIPE = createDrinkRecipe(
            new DrinkRecipe(COFFEE, null, 2, 0, 0,
                    new ItemStack(ModItems.COFFEE_BEAN.get(), 6)));

    // ---------- Methods ----------

    private static BaseDrink createBaseDrink(BaseDrink drink) {
        DRINKS.put(drink.getName(), drink);
        return drink;
    }

    private static DrinkRecipe createDrinkRecipe(DrinkRecipe recipe) {
        RECIPES.put(recipe.getName(), recipe);
        return recipe;
    }

    public static BaseDrink getBaseDrink(String name) {
        return DRINKS.get(name);
    }

    public static DrinkRecipe getDrinkRecipe(String name) {
        return RECIPES.get(name);
    }
}
