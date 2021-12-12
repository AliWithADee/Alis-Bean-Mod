package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.core.data.recipe.general.CanningMachineRecipe;
import io.github.aliwithadee.alisbeanmod.core.energy.BeanEnergyConsumerStorage;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralItems;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CanningMachineBE extends BlockEntity implements Container {

    private final ItemStackHandler itemHandler = createItemHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private final BeanEnergyConsumerStorage energyStorage = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    private int processTime = 0;
    private int curProcessTime = 0;

    public CanningMachineBE(BlockPos pos, BlockState state) {
        super(GeneralBlockEntities.CANNING_MACHINE_BE.get(), pos, state);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return switch (slot) {
                    case 1 -> stack.getItem() == GeneralItems.TIN_CAN.get();
                    case 2 -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
                    case 3 -> false;
                    default -> true;
                };
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private BeanEnergyConsumerStorage createEnergyStorage() {
        return new BeanEnergyConsumerStorage(10000, 1000) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }

            @Override
            public boolean canCreate() {
                return true;
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    // Craft the output of the current recipe and consume the inputs
    private void craft(CanningMachineRecipe recipe) {
        ItemStack output = recipe.getResultItem();
        ItemStack existing = itemHandler.getStackInSlot(3);

        // If there is nothing in the output slot, set the output slot to the output
        if (existing.isEmpty()) itemHandler.setStackInSlot(3, output);
            // Else, grow what is already there by 1
        else existing.grow(1);

        // Extract inputs
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);
    }

    // Can we craft this recipe?
    private boolean canCraft(CanningMachineRecipe recipe) {

        ItemStack output = recipe.getResultItem();
        ItemStack existing = itemHandler.getStackInSlot(3);
        int limit = Math.min(itemHandler.getSlotLimit(3), output.getMaxStackSize());

        // If there is something in the output slot
        if (!existing.isEmpty())
        {
            // If we cannot stack with what is already there, then return
            if (!ItemHandlerHelper.canItemStacksStack(output, existing)) return false;
            // Else, set the limit to the limit - how much is already there
            limit -= existing.getCount();
        }

        // If the limit is 0 or less then we cannot craft, so return false
        return limit > 0;
    }

    // Called every tick, from block class, on the server
    public void tickServer(BlockEntity blockEntity) {
        if(level.isClientSide())
            return;

        Optional<CanningMachineRecipe> recipe = level.getRecipeManager().getRecipeFor(GeneralRecipeTypes.CANNING_RECIPE,
                (CanningMachineBE) blockEntity, level);

        // If we are currently processing
        if (processTime > 0 && curProcessTime < processTime) {
            // If recipe is valid
            recipe.ifPresent(foundRecipe -> {
                // If we can't craft stop processing
                if (!canCraft(foundRecipe)){
                    processTime = 0;
                    curProcessTime = 0;
                } else {
                    // Otherwise, continue processing...
                    curProcessTime++;
                }
            });
            // If recipe is not valid, then we need to stop processing
            if (recipe.isEmpty()) {
                processTime = 0;
                curProcessTime = 0;
            }
        } else {
            // If we are not processing something

            // Is there a valid recipe?
            recipe.ifPresent(foundRecipe -> {
                // If the recipe is valid then,
                // We have either FINISHED processing something or we need to START processing something

                // If we can craft the result
                if (canCraft(foundRecipe)) {
                    // If processTime == 0 then we need to START processing
                    if (processTime == 0) {
                        processTime = foundRecipe.getProcessTime();
                    } else if (curProcessTime >= processTime) {
                        // If processTime is not 0 and curProcessTime >= processTime,
                        // then we have FINISHED processing, and we can craft the item
                        craft(foundRecipe);
                        // Reset processing time
                        processTime = 0;
                        curProcessTime = 0;
                        // This is saying: "Something has changed! We must save this chunk!"
                        setChanged();
                    }
                }

            });
            // If recipe is not valid, do nothing
        }
    }


    // ----------------- Container Implementations -----------------

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return itemHandler.extractItem(slot, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
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
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
