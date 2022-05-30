package io.github.aliwithadee.alisbeanmod.common.cooking.block;

import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class CookingPotBlock extends Block {
    protected static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(
            box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D),
            box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D),
            box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);

    public CookingPotBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty()) {
            if (stack.getItem() == Items.WATER_BUCKET) {
                return emptyBucket(state, level, pos, player, hand, stack);
            } else if (stack.getItem() == Items.BUCKET) {
                return fillBucket(state, level, pos, player, hand, stack);
            } else if (stack.getItem() == Items.GLASS_BOTTLE) {
                return fillBottle(state, level, pos, player, hand, stack);
            } else if (stack.getItem() == Items.BOWL) {
                return fillBowl(state, level, pos, player, hand, stack);
            } else if (hit.getDirection() == Direction.UP) {
                return addIngredient(state, level, pos, player, hand, stack);
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    protected InteractionResult emptyBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (!level.isClientSide) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));

            level.setBlockAndUpdate(pos, ModBlocks.FILLED_COOKING_POT.get().defaultBlockState()
                    .setValue(FilledCookingPotBlock.LEVEL, FilledCookingPotBlock.MAX_LEVEL));

            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected InteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected InteractionResult fillBottle(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected InteractionResult fillBowl(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected InteractionResult addIngredient(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return INSIDE;
    }
}
