package io.github.aliwithadee.alisbeanmod.core.brewery;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class DrinkRecipe {

    private final BaseDrink result;
    private final BaseDrink partialDrink;
    private final NonNullList<ItemStack> ingredients;
    private final int cookTime;
    private final int distills;
    private final int barrelAge;

    public DrinkRecipe(BaseDrink result, BaseDrink partialDrink, int cookTime, int distills, int barrelAge, ItemStack... ingredients) {
        this.result = result;
        this.partialDrink = partialDrink;
        this.ingredients = NonNullList.of(new ItemStack(null), ingredients);
        this.cookTime = cookTime;
        this.distills = distills;
        this.barrelAge = barrelAge;
    }

    public BaseDrink getResult() {
        return result;
    }

    public BaseDrink getPartialDrink() {
        return partialDrink;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getDistills() {
        return distills;
    }

    public int getBarrelYears() {
        return barrelAge;
    }

    public boolean requiresDistilling() {
        return distills > 0;
    }

    public boolean requiresAgeing() {
        return barrelAge > 0;
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
