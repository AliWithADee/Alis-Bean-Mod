package io.github.aliwithadee.alisbeanmod.common.brewery.block;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.brewery.*;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AgeingBarrelBE extends BlockEntity implements Container, MenuProvider {
    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    private int ticks;

    public AgeingBarrelBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AGEING_BARREL_BE.get(), pos, state);
    }

    private boolean stackInList(ItemStack stack, NonNullList<ItemStack> list) {
        for (ItemStack itemStack : list) {
            if (stack.getItem() == itemStack.getItem()) {
                return true;
            }
        }
        return false;
    }

    // Integer showing how far the recipe ingredients are away from the stacks the pot
    private int getIngredientDifference(DrinkRecipe recipe, NonNullList<ItemStack> ingredients) {
        int diff = 0;
        for (ItemStack stack : ingredients) {
            int d;
            if (stackInList(stack, recipe.getIngredients())) {
                d = Math.abs(stack.getCount() - recipe.getIngredientCount(stack));
            } else {
                d = stack.getCount();
            }
            diff += d;
        }
        return diff;
    }

    private int gradeDrink(Drink drink) {
        DrinkRecipe recipe = drink.getRecipe();

        int ing_rating;
        int ing_diff = getIngredientDifference(recipe, drink.getIngredients());
        if (ing_diff == 0) ing_rating = 5;
        else if (0 < ing_diff && ing_diff <= BreweryConstants.ING_DIFF_GREAT) ing_rating = 4;
        else if (BreweryConstants.ING_DIFF_GREAT < ing_diff && ing_diff <= BreweryConstants.ING_DIFF_FINE) ing_rating = 3;
        else if (BreweryConstants.ING_DIFF_FINE < ing_diff && ing_diff <= BreweryConstants.ING_DIFF_POOR) ing_rating = 2;
        else ing_rating = 1;

        int cook_rating;
        int cook_diff = Math.abs(recipe.getCookTime() - drink.getCookTime());
        if (cook_diff == 0) cook_rating = 5;
        else if (0 < cook_diff && cook_diff <= BreweryConstants.COOK_DIFF_GREAT) cook_rating = 4;
        else if (BreweryConstants.COOK_DIFF_GREAT < cook_diff && cook_diff <= BreweryConstants.COOK_DIFF_FINE) cook_rating = 3;
        else if (BreweryConstants.COOK_DIFF_FINE < cook_diff && cook_diff <= BreweryConstants.COOK_DIFF_POOR) cook_rating = 2;
        else cook_rating = 1;

        int distill_rating = 0;
        if (recipe.getDistills() > 0) {
            int distill_diff = Math.abs(recipe.getDistills() - drink.getDistills());
            if (distill_diff == 0) distill_rating = 5;
            else if (0 < distill_diff && distill_diff <= BreweryConstants.DISTILL_DIFF_GREAT) distill_rating = 4;
            else if (BreweryConstants.DISTILL_DIFF_GREAT < distill_diff && distill_diff <= BreweryConstants.DISTILL_DIFF_FINE) distill_rating = 3;
            else if (BreweryConstants.DISTILL_DIFF_FINE < distill_diff && distill_diff <= BreweryConstants.DISTILL_DIFF_POOR) distill_rating = 2;
            else distill_rating = 1;
        }

        int age_rating = 0;
        if (recipe.getBarrelYears() > 0) {
            int age_diff = Math.abs(recipe.getBarrelYears() - drink.getBarrelYears());
            if (age_diff == 0) age_rating = 5;
            else if (0 < age_diff && age_diff <= BreweryConstants.AGE_DIFF_GREAT) age_rating = 4;
            else if (BreweryConstants.AGE_DIFF_GREAT < age_diff && age_diff <= BreweryConstants.AGE_DIFF_FINE) age_rating = 3;
            else if (BreweryConstants.AGE_DIFF_FINE < age_diff && age_diff <= BreweryConstants.AGE_DIFF_POOR) age_rating = 2;
            else age_rating = 1;
        }

        float ratio;
        if (distill_rating == 0) ratio = (ing_rating + cook_rating + age_rating) / (BreweryConstants.MAX_RATING * 3.0f);
        else if (age_rating == 0) ratio = (ing_rating + cook_rating + distill_rating) / (BreweryConstants.MAX_RATING * 3.0f);
        else ratio = (ing_rating + cook_rating + distill_rating + age_rating) / (BreweryConstants.MAX_RATING * 4.0f);

        System.out.println("ratio:");
        System.out.println(ratio);

        return (int)(ratio * BreweryConstants.MAX_RATING);
    }

    private void ageDrinks() {
        for (ItemStack stack : this.items) {
            if (stack.getItem() == ModItems.DRINK.get()) {
                Drink drink = DrinkUtils.getDrink(stack);
                if (drink.inProgress()) {
                    CompoundTag tag = stack.getTag();
                    CompoundTag dataTag = tag.getCompound("Data");

                    int minutesBefore = dataTag.getInt("Age");
                    int yearsBefore = minutesBefore / BreweryConstants.MINUTES_PER_BARREL_YEAR;

                    int minutesNow = minutesBefore + 1;
                    int yearsNow = minutesNow / BreweryConstants.MINUTES_PER_BARREL_YEAR;

                    dataTag.putInt("Age", minutesNow);

                    String resultName = drink.getRecipe().getResult().getName();
                    if (!drink.getName().equals(resultName) && yearsNow > 0) tag.putString("Drink", resultName);

                    tag.put("Data", dataTag);
                    stack.setTag(tag);

                    if (yearsNow > yearsBefore) {
                        Drink updated = DrinkUtils.getDrink(stack);
                        int rating = gradeDrink(updated);
                        if (rating > 0) stack.getTag().getCompound("Data").putInt("Rating", rating);
                    }
                }
            }
        }
    }

    public void tickServer(AgeingBarrelBE blockEntity) {
        if (level.isClientSide())
            return;

        if (!isEmpty()) {
            if (ticks < 16) { // 1 minecraft minute = 16 ticks
                ticks++;
            } else {
                ticks = 0;
                ageDrinks();
            }
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, this.items);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        this.items.set(pIndex, pStack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("screen." + AlisBeanMod.MOD_ID + ".ageing_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ChestMenu(MenuType.GENERIC_9x1, id, inventory, this, 1);
    }
}
