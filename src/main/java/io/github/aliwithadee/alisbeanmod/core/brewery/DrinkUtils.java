package io.github.aliwithadee.alisbeanmod.core.brewery;

import io.github.aliwithadee.alisbeanmod.common.brewery.item.PartialDrinkItem;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrinkUtils {

    // ---------- Finished Drinks ----------

    public static Drink getDrink(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return ModDrinks.EMPTY;

        Drink drink = ModDrinks.getDrinks().get(tag.getString("Drink"));
        return drink.with(tag);
    }

    public static ItemStack setDrink(ItemStack stack, Drink drink) {
        if (drink == ModDrinks.EMPTY) {
            stack.removeTagKey("Drink"); // If setting to empty or scuffed, remove drink tag
            stack.removeTagKey("Rating");
        } else {
            CompoundTag tag = stack.getOrCreateTag();

            tag.putString("Drink", drink.getName());
            tag.putInt("Rating", drink.getRating());

            stack.setTag(tag);

        }
        return stack;
    }

    public static ItemStack createDrinkItem(Drink drink) {
        return setDrink(new ItemStack(ModItems.DRINK.get()), drink);
    }

    public static int getDrinkColor(ItemStack stack) {
        return getDrink(stack).getColour();
    }

    public static void addDrinkTooltip(ItemStack stack, List<Component> tooltips, float durationFactor) {
        Drink drink = getDrink(stack);
        if (drink != ModDrinks.EMPTY && drink != ModDrinks.SCUFFED) {
            tooltips.add(new TextComponent("Rating: " + drink.getRating()));
        }
    }

    // ---------- Partial Drinks ----------

    public static PartialDrink getPartialDrink(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return PartialDrink.EMPTY;

        return PartialDrink.with(tag);
    }

    public static ItemStack setPartialDrink(ItemStack stack, PartialDrink drink) {
        if (drink.isEmpty()) {
            stack.removeTagKey("Recipe");
            stack.removeTagKey("Ingredients");
            stack.removeTagKey("CookTime");
            stack.removeTagKey("Distills");
            stack.removeTagKey("Age");
            stack.removeTagKey("Finished");
        } else {
            CompoundTag tag = stack.getOrCreateTag();

            tag.putString("Recipe", drink.getRecipe().getName());
            saveIngredients(tag, drink.getIngredients());
            tag.putInt("CookTime", drink.getCookTime());
            tag.putInt("Distills", drink.getDistills());
            tag.putInt("Age", drink.getBarrelAge());
            tag.putBoolean("Finished", drink.isFinished());

            stack.setTag(tag);
        }

        return stack;
    }

    public static ItemStack createPartialDrinkItem(PartialDrink drink) {
        if (drink.isEmpty()) return createDrinkItem(ModDrinks.EMPTY);

        Item item = drink.getRecipe().getPartialItem();
        return setPartialDrink(new ItemStack(item), drink);
    }

    public static int getPartialDrinkColor(ItemStack stack) {
        PartialDrinkItem item = (PartialDrinkItem) stack.getItem();
        return item.getColor();
    }

    public static void addPartialDrinkTooltip(ItemStack stack, List<Component> tooltips, float durationFactor) {
        PartialDrink drink = getPartialDrink(stack);
        if (!drink.isEmpty()) {

        }
    }

    private static void saveIngredients(CompoundTag tag, NonNullList<ItemStack> ingredients) {
        ListTag listTag = new ListTag();

        for (ItemStack stack : ingredients) {
            if (!stack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                stack.save(compoundTag);
                listTag.add(compoundTag);
            }
        }

        tag.put("Ingredients", listTag);
    }

    public static NonNullList<ItemStack> getIngredients(CompoundTag tag) {
        NonNullList<ItemStack> ingredients = NonNullList.create();

        ListTag listTag = tag.getList("Ingredients", 10);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            ingredients.add(ItemStack.of(compoundTag));
        }

        return ingredients;
    }
}
