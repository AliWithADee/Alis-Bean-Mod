package io.github.aliwithadee.alisbeanmod.core.cooking.dish.stats;

import io.github.aliwithadee.alisbeanmod.core.cooking.dish.DishUtils;
import io.github.aliwithadee.alisbeanmod.core.cooking.dish.recipe.DishRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DishStats {
    protected int rating;
    protected final DishRecipe recipe;

    public DishStats(int rating) {
        this(rating, null);
    }

    protected DishStats(int rating, DishRecipe recipe) {
        this.rating = rating;
        this.recipe = recipe;
    }

    public int getRating() {
        return rating;
    }

    public DishRecipe getRecipe() {
        return recipe;
    }

    public boolean isGraded() {
        return rating > 0;
    }

    public boolean inProgress() {
        return recipe != null;
    }

    public void computeRating() {
    }

    public void addTooltip(ItemStack stack, List<Component> tooltips) {
        DishStats dishStats = DishUtils.getDishStats(stack);
        int rating = dishStats.getRating();
        if (dishStats.isGraded()) tooltips.add(new TextComponent("Rating: " + rating).withStyle(ChatFormatting.GRAY));
    }
}
