package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.core.data.recipe.general.CanningMachineRecipe;
import io.github.aliwithadee.alisbeanmod.core.energy.BeanEnergyConsumerStorage;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralItems;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CanningMachineBE extends BlockEntity implements WorldlyContainer {

    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private static final int[] SLOTS_FOR_SOUTH = new int[]{1}; // tin-can
    private static final int[] SLOTS_FOR_EAST = new int[]{2}; // fuel
    private static final int[] SLOTS_FOR_DOWN = new int[]{3}; // output
    private static final int[] SLOTS_FOR_SIDES = new int[]{0}; // input slot by default
    private static final int OUTPUT_SLOT_LIMIT = 1;

    private int processTime = 0;
    private int curProcessTime = 0;

    private final BeanEnergyConsumerStorage energyStorage;

    public CanningMachineBE(BlockPos pos, BlockState state) {
        super(GeneralBlockEntities.CANNING_MACHINE_BE.get(), pos, state);

        energyStorage = createEnergyStorage();
    }

    private BeanEnergyConsumerStorage createEnergyStorage() {
        return new BeanEnergyConsumerStorage(10000, 1000, true) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    private void craft(CanningMachineRecipe recipe) {
        ItemStack output = recipe.getResultItem();
        ItemStack existing = getItem(3);
        // If there is nothing in the output slot, set the output slot to the output
        if (existing.isEmpty()) setItem(3, output);
            // Else, grow what is already there by 1
        else existing.grow(1);
        // Extract inputs
        removeItem(0, 1);
        removeItem(1, 1);
    }

    // Can we craft this recipe?
    private boolean canCraft(CanningMachineRecipe recipe) {

        ItemStack output = recipe.getResultItem();
        ItemStack existing = getItem(3);
        int limit = Math.min(OUTPUT_SLOT_LIMIT, output.getMaxStackSize());

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
            //setChanged();
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
                    }
                    // This is saying: "Something has changed! We must save this chunk!"
                    //setChanged();
                }

            });
            // If recipe is not valid, do nothing
        }
    }

    @Override
    public void load(CompoundTag tag) {
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        processTime = tag.getInt("processTime");
        curProcessTime = tag.getInt("curProcessTime");
        energyStorage.setEnergy(tag.getInt("energy"));
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt("processTime", processTime);
        tag.putInt("curProcessTime", curProcessTime);
        tag.putInt("energy", energyStorage.getEnergyStored());
        return super.save(tag);
    }


    // ----------------- WorldlyContainer Implementations -----------------


    @Override
    public int[] getSlotsForFace(Direction side) {
        return switch (side) {
            case SOUTH -> SLOTS_FOR_SOUTH;
            case EAST -> SLOTS_FOR_EAST;
            case DOWN -> SLOTS_FOR_DOWN;
            default -> SLOTS_FOR_SIDES;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case 1 -> stack.getItem() == GeneralItems.TIN_CAN.get();
            case 2 -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
            case 3 -> false;
            default -> true;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN && slot == 3;
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
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return ContainerHelper.removeItem(this.items, slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
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


    // ----------------- Capabilities -----------------


    private LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this,
            Direction.SOUTH, Direction.EAST, Direction.DOWN);
    //      Tin Cans         Fuel            Output

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if (side == Direction.SOUTH) {
                    System.out.println("South. Tin-can slot.");
                    return handlers[0].cast();
                }
                else if (side == Direction.EAST) {
                    System.out.println("East. Fuel slot.");
                    return handlers[1].cast();
                }
                else if (side == Direction.DOWN) {
                    System.out.println("Down. Output slot.");
                    return handlers[2].cast();
                }
                System.out.println("Default input slot.");

            } else if (cap == CapabilityEnergy.ENERGY) {
                return LazyOptional.of(() -> energyStorage).cast();
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
        super.invalidateCaps();
    }

    @Override
    public void reviveCaps() {
        this.handlers = SidedInvWrapper.create(this, Direction.SOUTH, Direction.EAST, Direction.DOWN);
        //                                               Tin Cans         Fuel            Output
        super.reviveCaps();
    }
}
