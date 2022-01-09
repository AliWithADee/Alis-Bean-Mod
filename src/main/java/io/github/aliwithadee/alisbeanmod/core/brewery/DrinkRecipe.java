package io.github.aliwithadee.alisbeanmod.core.brewery;

import io.github.aliwithadee.alisbeanmod.common.brewery.item.PartialDrinkItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class DrinkRecipe {

    private final Drink drink;
    private final PartialDrinkItem partialItem;
    private final int cookTime;
    private final int distills;
    private final int barrelAge;
    private final NonNullList<ItemStack> ingredients;

    public DrinkRecipe(Drink drink, PartialDrinkItem partialItem, int cookTime, int barrelAge, ItemStack... ingredients) {
        this(drink, partialItem, cookTime, 0, barrelAge, ingredients);
    }

    public DrinkRecipe(Drink drink, PartialDrinkItem partialItem, int cookTime, int distills, int barrelAge, ItemStack... ingredients) {
        this.drink = drink;
        this.partialItem = partialItem;
        this.cookTime = cookTime;
        this.distills = distills;
        this.barrelAge = barrelAge;
        this.ingredients = NonNullList.of(new ItemStack(null), ingredients);
    }

    public String getName() {
        return drink.getName();
    }

    public Drink getDrink() {
        return drink;
    }

    public PartialDrinkItem getPartialItem() {
        return partialItem;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getDistills() {
        return distills;
    }

    public int getBarrelAge() {
        return barrelAge;
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
