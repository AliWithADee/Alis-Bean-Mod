package io.github.aliwithadee.alisbeanmod.common.brewery.block;

import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FilledCookingPotBE extends BlockEntity {

    private NonNullList<ItemStack> ingredients = NonNullList.create();
    private boolean cooking = false;
    private int ticks = 0;
    private int minutes = 0;

    public FilledCookingPotBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILLED_COOKING_POT_BE.get(), pos, state);
    }

    public Potion getResult() {
        return Potions.WATER;
    }

    public void tickServer(FilledCookingPotBE blockEntity) {
        if (level.isClientSide())
            return;

        if (cooking) {
            ticks++;
            minutes = (ticks / 20) / 60;
            System.out.println("Minutes: " + minutes);
        }
    }

    public boolean isCooking() {
        return cooking;
    }

    public void startCooking() {
        cooking = true;
        ticks = 0;
        minutes = 0;
    }
}
