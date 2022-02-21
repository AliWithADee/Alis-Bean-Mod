package io.github.aliwithadee.alisbeanmod.common.cooking.block;

import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class AgeingBarrelBlock extends Block implements EntityBlock {
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_NORTH = Stream.of(
            Block.box(5, 1, 2, 11, 2, 5),
            Block.box(5, 12, 2, 11, 13, 5),
            Block.box(3, 2, 2, 5, 3, 5),
            Block.box(11, 2, 2, 13, 3, 5),
            Block.box(3, 11, 2, 5, 12, 5),
            Block.box(11, 11, 2, 13, 12, 5),
            Block.box(3, 3, 2, 4, 4, 5),
            Block.box(12, 3, 2, 13, 4, 5),
            Block.box(3, 10, 2, 4, 11, 5),
            Block.box(12, 10, 2, 13, 11, 5),
            Block.box(2, 4, 2, 3, 10, 5),
            Block.box(13, 4, 2, 14, 10, 5),
            Block.box(5, 2, 2, 11, 12, 3),
            Block.box(4, 3, 2, 5, 4, 3),
            Block.box(11, 3, 2, 12, 4, 3),
            Block.box(4, 10, 2, 5, 11, 3),
            Block.box(11, 10, 2, 12, 11, 3),
            Block.box(3, 4, 2, 5, 10, 3),
            Block.box(11, 4, 2, 13, 10, 3),
            Block.box(5, 0, 5, 11, 1, 13),
            Block.box(5, 13, 5, 11, 14, 13),
            Block.box(3, 1, 5, 5, 2, 13),
            Block.box(11, 1, 5, 13, 2, 13),
            Block.box(3, 12, 5, 5, 13, 13),
            Block.box(11, 12, 5, 13, 13, 13),
            Block.box(2, 2, 5, 3, 4, 13),
            Block.box(13, 2, 5, 14, 4, 13),
            Block.box(2, 10, 5, 3, 12, 13),
            Block.box(13, 10, 5, 14, 12, 13),
            Block.box(1, 4, 5, 2, 10, 13),
            Block.box(14, 4, 5, 15, 10, 13),
            Block.box(5, 1, 13, 11, 2, 16),
            Block.box(5, 12, 13, 11, 13, 16),
            Block.box(3, 2, 13, 5, 3, 16),
            Block.box(11, 2, 13, 13, 3, 16),
            Block.box(3, 11, 13, 5, 12, 16),
            Block.box(11, 11, 13, 13, 12, 16),
            Block.box(3, 3, 13, 4, 4, 16),
            Block.box(12, 3, 13, 13, 4, 16),
            Block.box(3, 10, 13, 4, 11, 16),
            Block.box(12, 10, 13, 13, 11, 16),
            Block.box(2, 4, 13, 3, 10, 16),
            Block.box(13, 4, 13, 14, 10, 16),
            Block.box(5, 2, 15, 11, 12, 16),
            Block.box(4, 3, 15, 5, 4, 16),
            Block.box(11, 3, 15, 12, 4, 16),
            Block.box(4, 10, 15, 5, 11, 16),
            Block.box(11, 10, 15, 12, 11, 16),
            Block.box(3, 4, 15, 5, 10, 16),
            Block.box(11, 4, 15, 13, 10, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_EAST = Stream.of(
            Block.box(11, 1, 5, 14, 2, 11),
            Block.box(11, 12, 5, 14, 13, 11),
            Block.box(11, 2, 3, 14, 3, 5),
            Block.box(11, 2, 11, 14, 3, 13),
            Block.box(11, 11, 3, 14, 12, 5),
            Block.box(11, 11, 11, 14, 12, 13),
            Block.box(11, 3, 3, 14, 4, 4),
            Block.box(11, 3, 12, 14, 4, 13),
            Block.box(11, 10, 3, 14, 11, 4),
            Block.box(11, 10, 12, 14, 11, 13),
            Block.box(11, 4, 2, 14, 10, 3),
            Block.box(11, 4, 13, 14, 10, 14),
            Block.box(13, 2, 5, 14, 12, 11),
            Block.box(13, 3, 4, 14, 4, 5),
            Block.box(13, 3, 11, 14, 4, 12),
            Block.box(13, 10, 4, 14, 11, 5),
            Block.box(13, 10, 11, 14, 11, 12),
            Block.box(13, 4, 3, 14, 10, 5),
            Block.box(13, 4, 11, 14, 10, 13),
            Block.box(3, 0, 5, 11, 1, 11),
            Block.box(3, 13, 5, 11, 14, 11),
            Block.box(3, 1, 3, 11, 2, 5),
            Block.box(3, 1, 11, 11, 2, 13),
            Block.box(3, 12, 3, 11, 13, 5),
            Block.box(3, 12, 11, 11, 13, 13),
            Block.box(3, 2, 2, 11, 4, 3),
            Block.box(3, 2, 13, 11, 4, 14),
            Block.box(3, 10, 2, 11, 12, 3),
            Block.box(3, 10, 13, 11, 12, 14),
            Block.box(3, 4, 1, 11, 10, 2),
            Block.box(3, 4, 14, 11, 10, 15),
            Block.box(0, 1, 5, 3, 2, 11),
            Block.box(0, 12, 5, 3, 13, 11),
            Block.box(0, 2, 3, 3, 3, 5),
            Block.box(0, 2, 11, 3, 3, 13),
            Block.box(0, 11, 3, 3, 12, 5),
            Block.box(0, 11, 11, 3, 12, 13),
            Block.box(0, 3, 3, 3, 4, 4),
            Block.box(0, 3, 12, 3, 4, 13),
            Block.box(0, 10, 3, 3, 11, 4),
            Block.box(0, 10, 12, 3, 11, 13),
            Block.box(0, 4, 2, 3, 10, 3),
            Block.box(0, 4, 13, 3, 10, 14),
            Block.box(0, 2, 5, 1, 12, 11),
            Block.box(0, 3, 4, 1, 4, 5),
            Block.box(0, 3, 11, 1, 4, 12),
            Block.box(0, 10, 4, 1, 11, 5),
            Block.box(0, 10, 11, 1, 11, 12),
            Block.box(0, 4, 3, 1, 10, 5),
            Block.box(0, 4, 11, 1, 10, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_SOUTH = Stream.of(
            Block.box(5, 1, 11, 11, 2, 14),
            Block.box(5, 12, 11, 11, 13, 14),
            Block.box(11, 2, 11, 13, 3, 14),
            Block.box(3, 2, 11, 5, 3, 14),
            Block.box(11, 11, 11, 13, 12, 14),
            Block.box(3, 11, 11, 5, 12, 14),
            Block.box(12, 3, 11, 13, 4, 14),
            Block.box(3, 3, 11, 4, 4, 14),
            Block.box(12, 10, 11, 13, 11, 14),
            Block.box(3, 10, 11, 4, 11, 14),
            Block.box(13, 4, 11, 14, 10, 14),
            Block.box(2, 4, 11, 3, 10, 14),
            Block.box(5, 2, 13, 11, 12, 14),
            Block.box(11, 3, 13, 12, 4, 14),
            Block.box(4, 3, 13, 5, 4, 14),
            Block.box(11, 10, 13, 12, 11, 14),
            Block.box(4, 10, 13, 5, 11, 14),
            Block.box(11, 4, 13, 13, 10, 14),
            Block.box(3, 4, 13, 5, 10, 14),
            Block.box(5, 0, 3, 11, 1, 11),
            Block.box(5, 13, 3, 11, 14, 11),
            Block.box(11, 1, 3, 13, 2, 11),
            Block.box(3, 1, 3, 5, 2, 11),
            Block.box(11, 12, 3, 13, 13, 11),
            Block.box(3, 12, 3, 5, 13, 11),
            Block.box(13, 2, 3, 14, 4, 11),
            Block.box(2, 2, 3, 3, 4, 11),
            Block.box(13, 10, 3, 14, 12, 11),
            Block.box(2, 10, 3, 3, 12, 11),
            Block.box(14, 4, 3, 15, 10, 11),
            Block.box(1, 4, 3, 2, 10, 11),
            Block.box(5, 1, 0, 11, 2, 3),
            Block.box(5, 12, 0, 11, 13, 3),
            Block.box(11, 2, 0, 13, 3, 3),
            Block.box(3, 2, 0, 5, 3, 3),
            Block.box(11, 11, 0, 13, 12, 3),
            Block.box(3, 11, 0, 5, 12, 3),
            Block.box(12, 3, 0, 13, 4, 3),
            Block.box(3, 3, 0, 4, 4, 3),
            Block.box(12, 10, 0, 13, 11, 3),
            Block.box(3, 10, 0, 4, 11, 3),
            Block.box(13, 4, 0, 14, 10, 3),
            Block.box(2, 4, 0, 3, 10, 3),
            Block.box(5, 2, 0, 11, 12, 1),
            Block.box(11, 3, 0, 12, 4, 1),
            Block.box(4, 3, 0, 5, 4, 1),
            Block.box(11, 10, 0, 12, 11, 1),
            Block.box(4, 10, 0, 5, 11, 1),
            Block.box(11, 4, 0, 13, 10, 1),
            Block.box(3, 4, 0, 5, 10, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_WEST = Stream.of(
            Block.box(2, 1, 5, 5, 2, 11),
            Block.box(2, 12, 5, 5, 13, 11),
            Block.box(2, 2, 11, 5, 3, 13),
            Block.box(2, 2, 3, 5, 3, 5),
            Block.box(2, 11, 11, 5, 12, 13),
            Block.box(2, 11, 3, 5, 12, 5),
            Block.box(2, 3, 12, 5, 4, 13),
            Block.box(2, 3, 3, 5, 4, 4),
            Block.box(2, 10, 12, 5, 11, 13),
            Block.box(2, 10, 3, 5, 11, 4),
            Block.box(2, 4, 13, 5, 10, 14),
            Block.box(2, 4, 2, 5, 10, 3),
            Block.box(2, 2, 5, 3, 12, 11),
            Block.box(2, 3, 11, 3, 4, 12),
            Block.box(2, 3, 4, 3, 4, 5),
            Block.box(2, 10, 11, 3, 11, 12),
            Block.box(2, 10, 4, 3, 11, 5),
            Block.box(2, 4, 11, 3, 10, 13),
            Block.box(2, 4, 3, 3, 10, 5),
            Block.box(5, 0, 5, 13, 1, 11),
            Block.box(5, 13, 5, 13, 14, 11),
            Block.box(5, 1, 11, 13, 2, 13),
            Block.box(5, 1, 3, 13, 2, 5),
            Block.box(5, 12, 11, 13, 13, 13),
            Block.box(5, 12, 3, 13, 13, 5),
            Block.box(5, 2, 13, 13, 4, 14),
            Block.box(5, 2, 2, 13, 4, 3),
            Block.box(5, 10, 13, 13, 12, 14),
            Block.box(5, 10, 2, 13, 12, 3),
            Block.box(5, 4, 14, 13, 10, 15),
            Block.box(5, 4, 1, 13, 10, 2),
            Block.box(13, 1, 5, 16, 2, 11),
            Block.box(13, 12, 5, 16, 13, 11),
            Block.box(13, 2, 11, 16, 3, 13),
            Block.box(13, 2, 3, 16, 3, 5),
            Block.box(13, 11, 11, 16, 12, 13),
            Block.box(13, 11, 3, 16, 12, 5),
            Block.box(13, 3, 12, 16, 4, 13),
            Block.box(13, 3, 3, 16, 4, 4),
            Block.box(13, 10, 12, 16, 11, 13),
            Block.box(13, 10, 3, 16, 11, 4),
            Block.box(13, 4, 13, 16, 10, 14),
            Block.box(13, 4, 2, 16, 10, 3),
            Block.box(15, 2, 5, 16, 12, 11),
            Block.box(15, 3, 11, 16, 4, 12),
            Block.box(15, 3, 4, 16, 4, 5),
            Block.box(15, 10, 11, 16, 11, 12),
            Block.box(15, 10, 4, 16, 11, 5),
            Block.box(15, 4, 11, 16, 10, 13),
            Block.box(15, 4, 3, 16, 10, 5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public AgeingBarrelBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD)
                .noOcclusion()
                .requiresCorrectToolForDrops()
                .strength(2.0f)
                .sound(SoundType.WOOD));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if(blockEntity instanceof AgeingBarrelBE) {
                NetworkHooks.openGui(((ServerPlayer) player), (MenuProvider) blockEntity, blockEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Menu provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof Container container) {
                Containers.dropContents(level, pos, container);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.AGEING_BARREL_BE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        } else {
            return (level1, pos, state1, blockEntity) -> {
                if (blockEntity instanceof AgeingBarrelBE ageingBarrelBE) {
                    ageingBarrelBE.tickServer(ageingBarrelBE);
                }
            };
        }
    }
}
