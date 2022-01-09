package io.github.aliwithadee.alisbeanmod.core.brewery;

import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrinkUtils {

    // ---------- Finished Drinks ----------

    public static Drink getDrink(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return Drink.EMPTY;

        Drink drink = ModDrinks.getDrinks().get(tag.getString("Drink"));
        return drink.with(tag);
    }

    public static ItemStack setDrink(ItemStack stack, Drink drink) {
        if (drink == Drink.EMPTY) {
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
        return getDrink(stack).getColor();
    }

    public static void addDrinkTooltip(ItemStack stack, List<Component> tooltips, float durationFactor) {
        Drink drink = getDrink(stack);
        if (drink != Drink.EMPTY && drink != ModDrinks.SCUFFED) {
            tooltips.add(new TextComponent("Rating: " + drink.getRating()).withStyle(ChatFormatting.GRAY));
        }
    }

    // ---------- Partial Drinks ----------

    public static PartialDrink getPartialDrink(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return PartialDrink.EMPTY;

        PartialDrink drink = ModDrinks.getPartialDrinks().get(tag.getString("Drink"));
        return drink.with(tag);
    }

    public static ItemStack setPartialDrink(ItemStack stack, PartialDrink drink) {
        if (drink == PartialDrink.EMPTY) {
            stack.removeTagKey("Drink");
            stack.removeTagKey("Recipe");
            stack.removeTagKey("Ingredients");
            stack.removeTagKey("CookTime");
            stack.removeTagKey("Distills");
            stack.removeTagKey("Age");
            stack.removeTagKey("Finished");
        } else {
            CompoundTag tag = stack.getOrCreateTag();

            tag.putString("Drink", drink.getName());
            if (drink.hasRecipe()) {
                tag.putString("Recipe", drink.getRecipe().getName());
                saveIngredients(tag, drink.getIngredients());
                tag.putInt("CookTime", drink.getCookTime());
                tag.putInt("Distills", drink.getDistills());
                tag.putInt("Age", drink.getBarrelAge());
                tag.putBoolean("Finished", drink.isFinished());
            }

            stack.setTag(tag);
        }

        return stack;
    }

    public static ItemStack createPartialDrinkItem(PartialDrink drink) {
        return setPartialDrink(new ItemStack(ModItems.PARTIAL_DRINK.get()), drink);
    }

    public static int getPartialDrinkColor(ItemStack stack) {
        return getPartialDrink(stack).getColor();
    }

    public static void addPartialDrinkTooltip(ItemStack stack, List<Component> tooltips, float durationFactor) {
        PartialDrink drink = getPartialDrink(stack);
        if (drink != PartialDrink.EMPTY && drink.hasRecipe()) {

            NonNullList<ItemStack> ingredients = drink.getIngredients();
            if (Screen.hasShiftDown()) {
                tooltips.add(new TextComponent("Ingredients:").withStyle(ChatFormatting.GRAY));
                for (ItemStack ingredient : ingredients) {
                    tooltips.add(new TextComponent("  -  " + ingredient.getDisplayName().getString() + " x" + stack.getCount()).withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltips.add(new TextComponent("Ingredients: x" + ingredients.size()).withStyle(ChatFormatting.GRAY));
            }

            int minutes = drink.getCookTime();
            if (minutes == 1) tooltips.add(new TextComponent("Cooked for 1 minute").withStyle(ChatFormatting.GRAY));
            else if (minutes > 1) tooltips.add(new TextComponent("Cooked for " + minutes + " minutes").withStyle(ChatFormatting.GRAY));

            int distills = drink.getDistills();
            if (distills == 1) tooltips.add(new TextComponent("Distilled once").withStyle(ChatFormatting.GRAY));
            else if (distills > 1) tooltips.add(new TextComponent("Distilled " + distills + " times").withStyle(ChatFormatting.GRAY));

            int years = drink.getBarrelAge();
            if (years == 1) tooltips.add(new TextComponent("Aged for 1 year").withStyle(ChatFormatting.GRAY));
            else if (years > 1) tooltips.add(new TextComponent("Aged for " + years + " years").withStyle(ChatFormatting.GRAY));
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
