package io.github.aliwithadee.alisbeanmod.common.brewery.menu;

import io.github.aliwithadee.alisbeanmod.common.brewery.block.BrewingAlembicBE;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import io.github.aliwithadee.alisbeanmod.core.init.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BrewingAlembicMenu extends AbstractContainerMenu {
    private final BrewingAlembicBE blockEntity;
    private final ContainerData data;
    private final Player player;
    private final IItemHandler playerInv;

    public BrewingAlembicMenu(int windowId, Level level, BlockPos pos, Inventory playerInv, Player player) {
        this(windowId, level, pos, playerInv, player, new SimpleContainerData(4));
    }

    public BrewingAlembicMenu(int windowId, Level level, BlockPos pos, Inventory playerInv, Player player, ContainerData containerData) {
        super(ModContainers.BREWING_ALEMBIC_MENU.get(), windowId); //TODO: Menu reg

        blockEntity = (BrewingAlembicBE) level.getBlockEntity(pos);
        this.data = containerData;
        this.player = player;
        this.playerInv = new InvWrapper(playerInv);

        addSlot(new Slot(blockEntity, 0, 24, 50) { // input 1
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(0, stack);
            }
        });

        addSlot(new Slot(blockEntity, 1, 44, 50) { // input 2
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(1, stack);
            }
        });

        addSlot(new Slot(blockEntity, 2, 64, 50) { // input 3
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(2, stack);
            }
        });

        addSlot(new Slot(blockEntity, 3, 96, 50) { // output 1
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(3, stack);
            }
        });

        addSlot(new Slot(blockEntity, 4, 116, 50) { // output 2
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(4, stack);
            }
        });

        addSlot(new Slot(blockEntity, 5, 136, 50) { // output 3
            @Override
            public boolean mayPlace(ItemStack stack) {
                return blockEntity.canPlaceItem(5, stack);
            }
        });

        // Player Inventory
        addSlotBox(this.playerInv, 9, 8, 84, 9, 18, 3, 18);
        addSlotRange(this.playerInv, 0, 8, 142, 9, 18);

        this.addDataSlots(containerData);
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.BREWING_ALEMBIC.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if (index < 6) {
                if (!this.moveItemStackTo(stack, 6, 42, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                if (index < 42) {
                    if (!this.moveItemStackTo(stack, 0, 3, false)) {
                        if (!this.moveItemStackTo(stack, 3, 6, false)) {
                            return ItemStack.EMPTY;
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

    public int getDistillTicks() {
        return this.data.get(0);
    }

    public boolean slotHasInput(int slot) {
        if (slot < 0 || slot > 2) return false;
        return this.data.get(slot+1) == 1;
    }
}
