package io.github.aliwithadee.alisbeanmod.core.brewery.drink;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModDrinks {

    public static final Map<String, BaseDrink> DRINKS = new HashMap<>();
    public static final Map<String, DrinkRecipe> RECIPES = new HashMap<>();

    public static final BaseDrink SCUFFED = createDrink(new BaseDrink("scuffed", 78953176, 0.0f,
            new MobEffectInstance(MobEffects.POISON, 500)));
    public static final BaseDrink BEER = createDrink(new BaseDrink("beer", 12421704, 3.0f));
    public static final BaseDrink CIDER = createDrink(new BaseDrink("cider", 12080187, 3.0f,
            new MobEffectInstance(MobEffects.ABSORPTION, 500)));
    public static final BaseDrink DARK_CIDER = createDrink(new BaseDrink("dark_cider", 9850939, 3.0f,
            new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 500)));
    public static final BaseDrink GRAPE_MUST = createDrink(new BaseDrink("grape_must", 12720968));

    public static final Drink DEFAULT = new Drink(SCUFFED);

    public static final DrinkRecipe CIDER_RECIPE = createRecipe(
            new DrinkRecipe(CIDER, GRAPE_MUST, 1, 3, 3,
            new ItemStack(Items.WHEAT, 6),
            new ItemStack(Items.SUGAR, 4)));

    public static final DrinkRecipe DARK_CIDER_RECIPE = createRecipe(
            new DrinkRecipe(DARK_CIDER, GRAPE_MUST, 2, 0, 3,
                    new ItemStack(Items.WHEAT, 13),
                    new ItemStack(Items.SUGAR, 2)));

    public static final DrinkRecipe BEER_RECIPE = createRecipe(
            new DrinkRecipe(BEER, GRAPE_MUST, 1, 3, 0,
                    new ItemStack(Items.WHEAT, 8),
                    new ItemStack(Items.SUGAR, 6)));

    // ---------- Methods ----------

    private static BaseDrink createDrink(BaseDrink drink) {
        DRINKS.put(drink.getName(), drink);
        return drink;
    }

    private static DrinkRecipe createRecipe(DrinkRecipe recipe) {
        RECIPES.put(recipe.getResult().getName(), recipe);
        return recipe;
    }

    public static BaseDrink getDrink(String name) {
        return DRINKS.get(name);
    }

    public static DrinkRecipe getRecipe(String name) {
        return RECIPES.get(name);
    }
}
