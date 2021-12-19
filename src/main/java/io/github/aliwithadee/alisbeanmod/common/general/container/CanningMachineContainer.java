package io.github.aliwithadee.alisbeanmod.common.general.container;

import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralBlocks;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CanningMachineContainer extends AbstractContainerMenu {
    private final BlockEntity blockEntity;
    private final Player playerEntity;
    private final IItemHandler playerInventory;

    public CanningMachineContainer(int windowId, Level level, BlockPos pos, Inventory playerInventory, Player player) {
        super(GeneralContainers.CANNING_MACHINE_CONTAINER.get(), windowId);

        blockEntity = level.getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);

        if (blockEntity != null && blockEntity instanceof Container containerProvider) {

            addSlot(new Slot(containerProvider, 0, 71, 30) { // input
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return containerProvider.canPlaceItem(0, stack);
                }
            });

            addSlot(new Slot(containerProvider, 1, 71, 56) { // tin can
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return containerProvider.canPlaceItem(1, stack);
                }
            });

            addSlot(new Slot(containerProvider, 2, 45, 43) { // fuel
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return containerProvider.canPlaceItem(2, stack);
                }
            });

            addSlot(new Slot(containerProvider, 3, 131, 43) { // output
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return containerProvider.canPlaceItem(3, stack);
                }
            });
        }

        layoutPlayerInventorySlots(8, 84);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, GeneralBlocks.CANNING_MACHINE.get());
    }

    // Used in layoutPlayerInventorySlots() method
    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    // Used in layoutPlayerInventorySlots() method
    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        // If slot shift-clicked is NOT empty
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem(); // Get the item in the slot that was clicked
            itemstack = stack.copy(); // Set itemstack as a copy of it

            if (index < 4) { // If slot clicked is in gui inventory

                // Try merging item stack with first available slot,
                // from index 4 (first slot of player inv) to 38 (last slot of player inv).
                if (!this.moveItemStackTo(stack, 4, 40, false)) {
                    return ItemStack.EMPTY; // If we cannot do the merge, return empty item stack
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                if (index < 40) {
                    if (!this.moveItemStackTo(stack, 0, 4, false)) {
                        return ItemStack.EMPTY; // If we cannot do the merge, return empty item stack
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);

        }

        return itemstack;
    }
}
