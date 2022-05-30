package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class ObeliskBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape SHAPE_LOWER = Stream.of(
            Block.box(2, -1, 2, 14, 6, 3),
            Block.box(3, 6, 3, 13, 12, 4),
            Block.box(4, 12, 4, 12, 16, 5),
            Block.box(2, -1, 3, 3, 6, 13),
            Block.box(3, 6, 4, 4, 12, 12),
            Block.box(4, 12, 5, 5, 16, 11),
            Block.box(2, -1, 13, 14, 6, 14),
            Block.box(3, 6, 12, 13, 12, 13),
            Block.box(4, 12, 11, 12, 16, 12),
            Block.box(13, -1, 3, 14, 6, 13),
            Block.box(12, 6, 4, 13, 12, 12),
            Block.box(11, 12, 5, 12, 16, 11),
            Block.box(3, 5, 12, 13, 6, 13),
            Block.box(3, 5, 3, 13, 6, 4),
            Block.box(12, 5, 4, 13, 6, 12),
            Block.box(3, 5, 4, 4, 6, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_UPPER = Stream.of(
            Block.box(6, 8, 6, 10, 14, 7),
            Block.box(5, 2, 5, 11, 8, 6),
            Block.box(5, 0, 4, 11, 2, 5),
            Block.box(4, 0, 4, 5, 2, 12),
            Block.box(5, 2, 6, 6, 8, 10),
            Block.box(6, 8, 7, 7, 14, 9),
            Block.box(5, 0, 11, 11, 2, 12),
            Block.box(5, 2, 10, 11, 8, 11),
            Block.box(6, 8, 9, 10, 14, 10),
            Block.box(11, 0, 4, 12, 2, 12),
            Block.box(10, 2, 6, 11, 8, 10),
            Block.box(9, 8, 7, 10, 14, 9)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public ObeliskBlock() {
        super(BlockBehaviour.Properties.of(new Material.Builder(MaterialColor.WOOD).nonSolid().build())
                .noOcclusion()
                .strength(1.5f)
                .sound(SoundType.BAMBOO));

        registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) return SHAPE_UPPER;
        return SHAPE_LOWER;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? stateBelow.isFaceSturdy(level, pos.below(), Direction.UP) || stateBelow.is(Blocks.FARMLAND) : stateBelow.is(this);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!state.canSurvive(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                BlockState stateBelow = level.getBlockState(pos.below());
                if (stateBelow.is(this)) {
                    level.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, pos.below(), Block.getId(stateBelow));
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!(newState.getBlock() instanceof ObeliskBlock) && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState stateBelow = level.getBlockState(pos.below());
            if (stateBelow.getBlock() instanceof ObeliskBlock) {
                level.destroyBlock(pos.below(), true);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @NotNull
    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.DESTROY;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return new ItemStack(ModBlocks.OBELISK.get());
    }
}
