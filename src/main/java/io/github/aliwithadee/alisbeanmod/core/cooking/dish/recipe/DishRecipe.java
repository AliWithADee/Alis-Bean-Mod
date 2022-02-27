package io.github.aliwithadee.alisbeanmod.core.cooking.dish.recipe;

import io.github.aliwithadee.alisbeanmod.common.cooking.item.DishItem;
import net.minecraft.resources.ResourceLocation;

public class DishRecipe {
    protected final DishItem result;

    protected DishRecipe(DishItem result) {
        this.result = result;
    }

    public String getName() {
        ResourceLocation regName = result.getRegistryName();
        if (regName == null) return null;

        return regName.toString();
    }

    public DishItem getResult() {
        return result;
    }
}
