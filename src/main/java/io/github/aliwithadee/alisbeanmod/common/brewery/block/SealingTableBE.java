package io.github.aliwithadee.alisbeanmod.common.brewery.block;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.Drink;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.DrinkUtils;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class SealingTableBE extends BlockEntity implements Container, MenuProvider {
    private NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
    private int ticks;

    public SealingTableBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SEALING_TABLE_BE.get(), pos, state);
    }

    private void sealDrinks() {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);

            if (stack.getItem() == ModItems.DRINK.get()) {
                Drink drink = DrinkUtils.getDrink(stack);
                if (drink.inProgress() && drink.isGraded()) {
                    Drink newDrink = new Drink(drink.getRecipe().getResult(), drink.getRating());
                    items.set(i, DrinkUtils.createDrinkItem(newDrink));

                    level.playSound(null, worldPosition, SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    public void tickServer(SealingTableBE blockEntity) {
        if (level.isClientSide())
            return;

        if (!isEmpty()) {
            if (ticks < 16) { // 1 minecraft minute = 16 ticks
                ticks++;
            } else {
                ticks = 0;
                sealDrinks();
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
        return new TranslatableComponent("screen." + AlisBeanMod.MOD_ID + ".sealing_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ChestMenu(MenuType.GENERIC_9x4, id, inventory, this, 4);
    }
}
