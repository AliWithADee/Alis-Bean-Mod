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

    public static Drink getDrink(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return ModDrinks.DEFAULT;

        BaseDrink base = ModDrinks.getDrink(tag.getString("Drink"));
        CompoundTag dataTag = tag.getCompound("Data");

        return new Drink(base.getName(), base.getColor(), dataTag.getInt("Rating"), ModDrinks.getRecipe(dataTag.getString("Result")),
                getIngredients(dataTag), dataTag.getInt("CookTime"), dataTag.getInt("Distills"), dataTag.getInt("Age"));
    }

    public static ItemStack setDrink(ItemStack stack, Drink drink) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("Drink", drink.getName());

        if (drink.inProgress()) {
            CompoundTag dataTag = new CompoundTag();

            dataTag.putString("Result", drink.getRecipe().getResult().getName());
            saveIngredients(dataTag, drink.getIngredients());
            dataTag.putInt("CookTime", drink.getCookTime());
            dataTag.putInt("Distills", drink.getDistills());
            dataTag.putInt("Age", drink.getBarrelAge());

            tag.put("Data", dataTag);
        } else if (drink.isGraded()) {
            CompoundTag dataTag = new CompoundTag();

            dataTag.putInt("Rating", drink.getRating());
            tag.put("Data", dataTag);
        }

        stack.setTag(tag);

        return stack;
    }

    public static ItemStack removeDrink(ItemStack stack) {
        stack.removeTagKey("Drink");
        stack.removeTagKey("Data");
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

        int rating = drink.getRating();
        if (drink.isGraded()) tooltips.add(new TextComponent("Rating: " + rating).withStyle(ChatFormatting.GRAY));

        if (drink.inProgress()) {
            NonNullList<ItemStack> ingredients = drink.getIngredients();
            if (Screen.hasShiftDown()) {
                tooltips.add(new TextComponent("Ingredients:").withStyle(ChatFormatting.GRAY));
                for (ItemStack ingredient : ingredients) {
                    tooltips.add(new TextComponent("  -  " + ingredient.getDisplayName().getString() + " x" + ingredient.getCount()).withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltips.add(new TextComponent("Ingredients: x" + ingredients.size()).withStyle(ChatFormatting.GRAY));
            }

            int cookTime = drink.getCookTime();
            if (cookTime == 1) tooltips.add(new TextComponent("Cooked for 1 minute").withStyle(ChatFormatting.GRAY));
            else if (cookTime > 1) tooltips.add(new TextComponent("Cooked for " + cookTime + " minutes").withStyle(ChatFormatting.GRAY));

            int distills = drink.getDistills();
            if (distills == 1) tooltips.add(new TextComponent("Distilled once").withStyle(ChatFormatting.GRAY));
            else if (distills > 1) tooltips.add(new TextComponent("Distilled " + distills + " times").withStyle(ChatFormatting.GRAY));

            int years = drink.getBarrelYears();
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

    private static NonNullList<ItemStack> getIngredients(CompoundTag tag) {
        NonNullList<ItemStack> ingredients = NonNullList.create();
        ListTag listTag = tag.getList("Ingredients", 10);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            ingredients.add(ItemStack.of(compoundTag));
        }
        return ingredients;
    }
}
