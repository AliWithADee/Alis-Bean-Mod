package io.github.aliwithadee.alisbeanmod.common.general.item;

import io.github.aliwithadee.alisbeanmod.common.general.block.ObeliskBlock;
import io.github.aliwithadee.alisbeanmod.common.general.block.PlantedObeliskBlock;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class ObeliskSeedItem extends Item {
    private final PlantedObeliskBlock plant;

    public ObeliskSeedItem(PlantedObeliskBlock plant, Item.Properties properties) {
        super(properties);
        this.plant = plant;
    }

    private boolean tryPlant(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockState stateAbove = level.getBlockState(pos.above());
        if (state.is(ModBlocks.OBELISK.get()) && state.getValue(ObeliskBlock.HALF) == DoubleBlockHalf.LOWER) {
            if (stateAbove.is(ModBlocks.OBELISK.get()) && stateAbove.getValue(ObeliskBlock.HALF) == DoubleBlockHalf.UPPER) {
                if (level.getBlockState(pos.below()).is(Blocks.FARMLAND)) {
                    level.setBlockAndUpdate(pos, this.plant.defaultBlockState().setValue(ObeliskBlock.HALF, DoubleBlockHalf.LOWER));
                    level.setBlockAndUpdate(pos.above(), this.plant.defaultBlockState().setValue(ObeliskBlock.HALF, DoubleBlockHalf.UPPER));

                    level.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (state.is(ModBlocks.OBELISK.get())) {
            if (tryPlant(level, pos)) {
                context.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        } else if (state.is(Blocks.FARMLAND) && context.getClickedFace() == Direction.UP) {
            if (tryPlant(level, pos.above())) {
                context.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }
}
