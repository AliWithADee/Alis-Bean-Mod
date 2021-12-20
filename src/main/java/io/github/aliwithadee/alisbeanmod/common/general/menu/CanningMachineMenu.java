package io.github.aliwithadee.alisbeanmod.common.general.menu;

import io.github.aliwithadee.alisbeanmod.common.general.block.CanningMachineBE;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralBlocks;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CanningMachineMenu extends AbstractContainerMenu {
    private final CanningMachineBE blockEntity;
    private final ContainerData data;
    private final Player player;
    private final IItemHandler playerInv;

    public CanningMachineMenu(int windowId, Level level, BlockPos pos, Inventory playerInv, Player player) {
        this(windowId, level, pos, playerInv, player, new SimpleContainerData(4));
    }

    public CanningMachineMenu(int windowId, Level level, BlockPos pos, Inventory playerInv, Player player, ContainerData containerData) {
        super(GeneralContainers.CANNING_MACHINE_CONTAINER.get(), windowId);

        blockEntity = (CanningMachineBE) level.getBlockEntity(pos);
        this.data = containerData;
        this.player = player;
        this.playerInv = new InvWrapper(playerInv);

        addSlot(new Slot(blockEntity, 0, 71, 26) { // input
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(0, stack);
            }
        });

        addSlot(new Slot(blockEntity, 1, 71, 52) { // tin can
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(1, stack);
            }
        });

        addSlot(new Slot(blockEntity, 2, 45, 39) { // fuel
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(2, stack);
            }
        });

        addSlot(new Slot(blockEntity, 3, 131, 39) { // output
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(3, stack);
            }
        });

        layoutPlayerInventorySlots(8, 84);

        this.addDataSlots(containerData);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, GeneralBlocks.CANNING_MACHINE.get());
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
        addSlotBox(playerInv, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInv, 0, leftCol, topRow, 9, 18);
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
                    if (!this.moveItemStackTo(stack, 1, 2, false)) {
                        if (!this.moveItemStackTo(stack, 2, 3, false)) {
                            if (!this.moveItemStackTo(stack, 0, 1, false)) {
                                return ItemStack.EMPTY; // If we cannot do the merge, return empty item stack
                            }
                        }
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

    public int getCanningProgress() {
        int processTime = this.data.get(0);
        int curProcessTime = this.data.get(1);
        return processTime != 0 && curProcessTime != 0 ? curProcessTime * 22 / processTime : 0;
    }

    public int getMaxEnergy() {
        return this.data.get(2);
    }

    public int getEnergy() {
        return this.data.get(3);
    }
}
