package io.github.aliwithadee.alisbeanmod.common.cooking.block;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.Drink;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.DrinkRecipe;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import io.github.aliwithadee.alisbeanmod.core.util.BeanModCommonConfig;
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

    private void ageDrinks() {
        for (ItemStack stack : this.items) {
            if (stack.getItem() == ModItems.DRINK.get()) {
                Drink drink = DrinkUtils.getDrink(stack);
                if (drink.inProgress()) {
                    int minutesBefore = drink.getBarrelMinutes();
                    int yearsBefore = minutesBefore / BeanModCommonConfig.MINUTES_PER_BARREL_YEAR.get();

                    int minutesNow = minutesBefore + 1;
                    int yearsNow = minutesNow / BeanModCommonConfig.MINUTES_PER_BARREL_YEAR.get();

                    // Increment age
                    drink.setBarrelMinutes(minutesNow);

                    // Update drink rating, if years just increased
                    if (drink.isGraded() && yearsNow > yearsBefore) {
                        DrinkUtils.gradeDrink(drink);
                    }

                    // Update drink, if not already graded
                    DrinkRecipe recipe = drink.getRecipe();
                    if (!drink.isGraded()) {
                        if (yearsNow > 0 && recipe.requiresAgeing()) {
                            drink.setBaseDrink(recipe.getResult());
                            DrinkUtils.gradeDrink(drink);
                        }
                    }

                    // Update stack
                    DrinkUtils.setDrink(stack, drink);
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
