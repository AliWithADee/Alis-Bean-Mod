package io.github.aliwithadee.alisbeanmod.common.cooking.block;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import io.github.aliwithadee.alisbeanmod.common.cooking.menu.BrewingAlembicMenu;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.Drink;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.DrinkRecipe;
import io.github.aliwithadee.alisbeanmod.core.cooking.drinks.DrinkUtils;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BrewingAlembicBE extends BlockEntity implements Container, MenuProvider {
    public static final int TICKS_TO_DISTILL = 60; // TODO: Make distilling process take more time

    private NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    private int ticks = 0;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> BrewingAlembicBE.this.ticks;
                case 1 -> BrewingAlembicBE.this.items.get(0).isEmpty() ? 0 : 1;
                case 2 -> BrewingAlembicBE.this.items.get(1).isEmpty() ? 0 : 1;
                case 3 -> BrewingAlembicBE.this.items.get(2).isEmpty() ? 0 : 1;
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

    public BrewingAlembicBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BREWING_ALEMBIC_BE.get(), pos, state);
    }

    private boolean canDistill() {
        if (!isEmpty()) {
            Block block = level.getBlockState(worldPosition.below()).getBlock();
            if(block == Blocks.FIRE || block == Blocks.LAVA || block == Blocks.MAGMA_BLOCK) {
                int inputsPresent = 0;
                for (int i = 0; i < 3; i++) {
                    ItemStack stack = items.get(i);
                    if (stack.getItem() == ModItems.DRINK.get()) {
                        Drink drink = DrinkUtils.getDrink(stack);
                        if (drink.inProgress()) inputsPresent += 1;
                    }
                }
                if (inputsPresent > 0) {
                    int outputsAvailable = 0;
                    for (int i = 3; i < 6; i++) {
                        ItemStack stack = items.get(i);
                        if (stack.isEmpty()) outputsAvailable += 1;
                    }
                    return outputsAvailable >= inputsPresent;
                }
            }
        }
        return false;
    }

    private void distillDrinks() {
        for (int i = 0; i < 3; i++) {
            ItemStack stack = items.get(i);
            if (stack.getItem() == ModItems.DRINK.get()) {
                Drink drink = DrinkUtils.getDrink(stack);
                if (drink.inProgress()) {
                    int distillsBefore = drink.getDistills();
                    int distillsNow = distillsBefore + 1;

                    // Increment distills
                    drink.setDistills(distillsNow);

                    // Update Rating, if already graded
                    if (drink.isGraded()) {
                        DrinkUtils.gradeDrink(drink);
                    }

                    // Update drink, if not already graded
                    DrinkRecipe recipe = drink.getRecipe();
                    if (!drink.isGraded()) {
                        if (recipe.requiresDistilling() && !recipe.requiresAgeing()) {
                            drink.setBaseDrink(recipe.getResult());
                            DrinkUtils.gradeDrink(drink);
                        }
                    }

                    // Update stack
                    DrinkUtils.setDrink(stack, drink);

                    // Output result
                    for (int o = 3; o < 6; o++) {
                        ItemStack output = items.get(o);
                        if (output.isEmpty()) {
                            items.set(o, stack); // Set next available output slot to updated item stack
                            items.set(i, ItemStack.EMPTY); // Set current slot to empty
                            break;
                        }
                    }
                }
            }
        }
    }

    public void tickServer(BrewingAlembicBE blockEntity) {
        if (level.isClientSide())
            return;

        if (ticks == 0 && canDistill()) {
            ticks++;
        } else if (ticks > 0 && ticks < TICKS_TO_DISTILL) {
            if (canDistill()) {
                ticks++;
            } else {
                ticks = 0;
            }
        } else if (ticks >= TICKS_TO_DISTILL) {
            distillDrinks();
            ticks = 0;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        ticks = tag.getInt("Ticks");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt("Ticks", ticks);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case 0,1,2 -> stack.getItem() == ModItems.DRINK.get();
            default -> false;
        };
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
        return new TranslatableComponent("screen." + AlisBeanMod.MOD_ID + ".brewing_alembic");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BrewingAlembicMenu(id, this.level, this.getBlockPos(), player.getInventory(), player, this.data);
    }
}
