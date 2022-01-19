package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.general.menu.CanningMachineMenu;
import io.github.aliwithadee.alisbeanmod.core.data.recipe.general.CanningMachineRecipe;
import io.github.aliwithadee.alisbeanmod.core.energy.BeanEnergyConsumerStorage;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.init.ModItems;
import io.github.aliwithadee.alisbeanmod.core.init.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CanningMachineBE extends BlockEntity implements WorldlyContainer, MenuProvider {

    private final int[] SLOTS_FOR_SOUTH = new int[]{1}; // tin-can
    private final int[] SLOTS_FOR_EAST = new int[]{2}; // fuel
    private final int[] SLOTS_FOR_DOWN = new int[]{3}; // output
    private final int[] SLOTS_FOR_SIDES = new int[]{0}; // input slot by default

    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private int processTime = 0;
    private int curProcessTime = 0;
    private boolean lit = false;

    private final BeanEnergyConsumerStorage energyStorage = createEnergyStorage();
    private final int ENERGY_CAPACITY = 5000;
    private final int MAX_ENERGY_RECEIVE = 500;
    private final int ENERGY_PER_TICK = 5;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CanningMachineBE.this.processTime;
                case 1 -> CanningMachineBE.this.curProcessTime;
                case 2 -> CanningMachineBE.this.energyStorage.getMaxEnergyStored();
                case 3 -> CanningMachineBE.this.energyStorage.getEnergyStored();
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {

        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public CanningMachineBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CANNING_MACHINE_BE.get(), pos, state);
    }

    private BeanEnergyConsumerStorage createEnergyStorage() {
        return new BeanEnergyConsumerStorage(ENERGY_CAPACITY, MAX_ENERGY_RECEIVE, true) {
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

        int OUTPUT_SLOT_LIMIT = 1;

        ItemStack output = recipe.getResultItem();
        ItemStack existing = getItem(3);
        int limit = Math.min(OUTPUT_SLOT_LIMIT, output.getMaxStackSize());

        // If there is something in the output slot
        if (!existing.isEmpty())
        {
            // If we cannot stack with what is already there, then return false
            if (!ItemHandlerHelper.canItemStacksStack(output, existing)) return false;
            // Else, set the limit to the limit - how much is already there
            limit -= existing.getCount();
        }

        // If there is not enough energy to craft, then return false
        if (energyStorage.getEnergyStored() < ENERGY_PER_TICK) return false;

        // If the limit is 0 or less then we cannot craft, so return false
        return limit > 0;
    }

    private void burnFuel() {
        ItemStack fuel = getItem(2);

        if (!fuel.isEmpty()) {
            int burnTime = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
            int energy = burnTime / 10;

            if (burnTime > 0 && energyStorage.canAccept(energy)) {
                removeItem(2, 1);
                energyStorage.createEnergy(energy);
            }
        }
    }

    // Called every tick, from block class, on the server
    public void tickServer(CanningMachineBE blockEntity) {
        if(level.isClientSide())
            return;

        lit = false;
        Optional<CanningMachineRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.CANNING_RECIPE, blockEntity, level);

        burnFuel();

        // If there is something to process AND we haven't finished processing it
        if (processTime > 0 && curProcessTime < processTime) {
            // If recipe is still present
            recipe.ifPresent(foundRecipe -> {
                // If we can't craft anymore
                if (!canCraft(foundRecipe)){
                    // Reset / stop processing
                    processTime = 0;
                    curProcessTime = 0;
                    // Stay lit until next tick (stops annoying flashing)
                    lit = true;
                } else {
                    // If we can craft
                    // Increase processing time
                    curProcessTime++;
                    // Set block to lit
                    lit = true;
                    // Consume energy
                    energyStorage.consumeEnergy(ENERGY_PER_TICK);
                }
            });
            // If recipe is no longer present
            if (recipe.isEmpty()) {
                // Reset / stop processing
                processTime = 0;
                curProcessTime = 0;
                // Stay lit until next tick (stops annoying flashing)
                lit = true;
            }
        } else {

            // If there isn't anything to process OR we have finished processing

            // If recipe is present
            recipe.ifPresent(foundRecipe -> {

                // If we can craft the result
                if (canCraft(foundRecipe)) {
                    // If there isn't anything to process
                    if (processTime == 0) {
                        // START processing this recipe
                        processTime = foundRecipe.getProcessTime();
                        // Stay lit until next tick (stops annoying flashing)
                        lit = true;
                    } else if (curProcessTime >= processTime) {
                        // If we have finished processing this recipe
                        // Craft the result
                        craft(foundRecipe);
                        // Reset / stop processing
                        processTime = 0;
                        curProcessTime = 0;
                        // Stay lit until next tick (stops annoying flashing)
                        lit = true;
                    }
                }
            });
            // If recipe is not present, lit remains = false
        }

        // If lit was set to true, ensure the block is lit
        // Otherwise, ensure the block is not lit
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockStateProperties.LIT) != lit) {
            level.setBlock(worldPosition, state.setValue(BlockStateProperties.LIT, lit), 3);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        energyStorage.setEnergy(tag.getInt("Energy"));
        processTime = tag.getInt("ProcessTime");
        curProcessTime = tag.getInt("CurProcessTime");
        lit = tag.getBoolean("Lit");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt("Energy", energyStorage.getEnergyStored());
        tag.putInt("ProcessTime", processTime);
        tag.putInt("CurProcessTime", curProcessTime);
        tag.putBoolean("Lit", lit);
    }

    // ----------------- WorldlyContainer Implementations -----------------

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {

        Direction side = getLocalDirection(direction);
        return switch (side) {
            case SOUTH -> SLOTS_FOR_SOUTH;
            case EAST -> SLOTS_FOR_EAST;
            case DOWN -> SLOTS_FOR_DOWN;
            default -> SLOTS_FOR_SIDES;
        };
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case 1 -> stack.getItem() == ModItems.TIN_CAN.get();
            case 2 -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
            case 3 -> false;
            default -> true;
        };
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
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        this.setChanged();
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
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN && slot == 3;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    // ----------------- Capabilities -----------------

    private LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this,
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    //      Anything         Fuel            Tin Can          Anything

    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {

        if (!this.remove && direction != null) {

            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if (direction == Direction.NORTH) {
                    return handlers[0].cast();
                }
                else if (direction == Direction.EAST) {
                    return handlers[1].cast();
                }
                else if (direction == Direction.SOUTH) {
                    return handlers[2].cast();
                }
                else if (direction == Direction.WEST) {
                    return handlers[3].cast();
                }

            } else if (cap == CapabilityEnergy.ENERGY) {
                return energy.cast();
            }
        }
        return LazyOptional.empty();
    }

    private Direction getLocalDirection(Direction globalDirection) {
        Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (globalDirection == Direction.UP || globalDirection == Direction.DOWN){
            return globalDirection;
        }
        return switch (facing) {
            case EAST -> globalDirection.getCounterClockWise();
            case WEST -> globalDirection.getClockWise();
            case SOUTH -> globalDirection.getOpposite();
            default -> globalDirection;
        };
    }

    @Override
    public void invalidateCaps() {
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
        energy.invalidate();
        super.invalidateCaps();
    }

    @Override
    public void reviveCaps() {
        this.handlers = SidedInvWrapper.create(this,
                Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
        //      Anything         Fuel            Tin Can          Anything
        this.energy = LazyOptional.of(() -> energyStorage);
        super.reviveCaps();
    }

    // ----------------- MenuProvider -----------------

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("screen." + AlisBeanMod.MOD_ID + ".canning_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new CanningMachineMenu(id, this.level, this.getBlockPos(), player.getInventory(), player, this.data);
    }
}
