package io.github.aliwithadee.alisbeanmod.core.cooking;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class CookingUtils {
    public static final String NBT_DRINK_NAME = "name";
    public static final String NBT_RATING = "rating";
    public static final String NBT_DATA = "data";
    public static final String NBT_DISH_TYPE = "type";
    public static final String NBT_RECIPE = "recipe";
    public static final String NBT_COOK = "minutes";
    public static final String NBT_ING = "ingredients";
    public static final String NBT_DISTILLS = "distills";
    public static final String NBT_AGE = "years";

    public static void ingredientsToTag(CompoundTag tag, NonNullList<ItemStack> ingredients) {
        ListTag listTag = new ListTag();
        for (ItemStack stack : ingredients) {
            if (!stack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                stack.save(compoundTag);
                listTag.add(compoundTag);
            }
        }
        tag.put(NBT_ING, listTag);
    }

    public static NonNullList<ItemStack> ingredientsFromTag(CompoundTag tag) {
        NonNullList<ItemStack> ingredients = NonNullList.create();
        ListTag listTag = tag.getList(NBT_ING, 10);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            ingredients.add(ItemStack.of(compoundTag));
        }
        return ingredients;
    }

    public static boolean stackInList(ItemStack stack, NonNullList<ItemStack> list) {
        for (ItemStack itemStack : list) {
            if (stack.getItem() == itemStack.getItem()) {
                return true;
            }
        }
        return false;
    }

    public static int getIngredientCount(ItemStack ingredient, NonNullList<ItemStack> ingredients) {
        int count = 0;
        for (ItemStack stack : ingredients) {
            if (stack.getItem() == ingredient.getItem()) {
                count += stack.getCount();
            }
        }
        return count;
    }

    // Integer showing how far the recipe ingredients are away from the stacks the pot
    public static int getIngredientError(NonNullList<ItemStack> recipeIngredients, NonNullList<ItemStack> dishIngredients) {
        int error = 0;
        for (ItemStack ingredient : recipeIngredients) {
            int diff;
            if (stackInList(ingredient, dishIngredients)) {
                diff = Math.abs(getIngredientCount(ingredient, dishIngredients) - ingredient.getCount());
            } else {
                diff = ingredient.getCount();
            }
            error += diff;
        }
        return error;
    }
}
