package io.github.aliwithadee.alisbeanmod.core.cooking.dish;

import io.github.aliwithadee.alisbeanmod.common.cooking.item.DishItem;
import io.github.aliwithadee.alisbeanmod.core.cooking.CookingUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.stats.CookingStats;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.stats.DishStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class DishUtils {
    public static DishStats getDishStats(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return ModDishes.DEFAULT_STATS;
        CompoundTag dataTag = tag.getCompound(CookingUtils.NBT_DATA);
        String type = dataTag.getString(CookingUtils.NBT_DISH_TYPE);

        if (type.equals(CookingStats.TYPE)) {
            return new CookingStats(tag.getInt(CookingUtils.NBT_RATING),
                    ModDishes.getDishRecipe(dataTag.getString(CookingUtils.NBT_RECIPE)),
                    CookingUtils.ingredientsFromTag(dataTag), dataTag.getInt(CookingUtils.NBT_COOK));
        }

        return new DishStats(tag.getInt(CookingUtils.NBT_RATING));
    }

    public static ItemStack setDishStats(ItemStack stack, DishStats dishStats) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(CookingUtils.NBT_RATING, dishStats.getRating());

        if (dishStats.inProgress()) {
            CompoundTag dataTag = new CompoundTag();
            dataTag.putString(CookingUtils.NBT_RECIPE, dishStats.getRecipe().getName());

            if (dishStats instanceof CookingStats cookingStats) {
                dataTag.putString(CookingUtils.NBT_DISH_TYPE, CookingStats.TYPE);
                dataTag.putInt(CookingUtils.NBT_COOK, cookingStats.getCookTime());
                CookingUtils.ingredientsToTag(dataTag, cookingStats.getIngredients());
            }

            tag.put(CookingUtils.NBT_DATA, dataTag);
        }

        stack.setTag(tag);
        return stack;
    }

    public static ItemStack createDishItem(DishItem dish) {
        return createDishItem(dish, ModDishes.DEFAULT_STATS);
    }

    public static ItemStack createDishItem(DishItem dish, DishStats dishStats) {
        return setDishStats(new ItemStack(dish), dishStats);
    }
}