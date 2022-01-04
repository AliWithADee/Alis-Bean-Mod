package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class BrewingCauldronWaterBlock extends BrewingCauldronBlock {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 3;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", MIN_LEVEL, MAX_LEVEL);

    @Override
    protected InteractionResult addIngredient(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (isFull(state)) {
            if (!level.isClientSide) {
                player.getItemInHand(hand).shrink(1);
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult fillBottle(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        System.out.println("Do the Brew!");
        return addIngredient(state, level, pos, player, hand, stack);
    }

    @Override
    protected InteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (!level.isClientSide && isFull(state)) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(pos, GeneralBlocks.BREWING_CAULDRON.get().defaultBlockState());
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult emptyBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void lowerFillLevel(BlockState state, Level level, BlockPos pos) {
        int newLevel = state.getValue(LEVEL) - 1;
        level.setBlockAndUpdate(pos, newLevel == 0 ? GeneralBlocks.BREWING_CAULDRON.get().defaultBlockState() : state.setValue(LEVEL, newLevel));
    }

    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == MAX_LEVEL;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity.isOnFire() && entityInsideContent(state, pos, entity)) {
            entity.clearFire();
            if (entity.mayInteract(level, pos)) {
                lowerFillLevel(state, level, pos);
            }
        }
    }

    private boolean entityInsideContent(BlockState state, BlockPos pos, Entity entity) {
        return entity.getY() < (double)pos.getY() + getContentHeight(state) && entity.getBoundingBox().maxY > (double)pos.getY() + 0.25D;
    }

    private double getContentHeight(BlockState pState) {
        return (6.0D + (double) pState.getValue(LEVEL) * 3.0D) / 16.0D;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }
}
