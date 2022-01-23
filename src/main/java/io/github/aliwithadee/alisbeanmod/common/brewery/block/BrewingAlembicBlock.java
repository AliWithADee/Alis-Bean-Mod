package io.github.aliwithadee.alisbeanmod.common.brewery.block;

import io.github.aliwithadee.alisbeanmod.core.brewery.alcohol.CapabilityAlcohol;
import io.github.aliwithadee.alisbeanmod.core.brewery.alcohol.IAlcoholCapability;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class BrewingAlembicBlock extends Block implements EntityBlock {
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_NORTH = Stream.of(
            Block.box(0, 0, 6, 5, 6, 11),
            Block.box(8, 0, 7, 15, 1, 10),
            Block.box(9, 0, 6, 14, 1, 7),
            Block.box(9, 0, 10, 14, 1, 11),
            Block.box(10, 0, 5, 13, 1, 6),
            Block.box(10, 0, 11, 13, 1, 12),
            Block.box(15, 1, 7, 16, 5, 10),
            Block.box(8, 1, 6, 15, 5, 11),
            Block.box(7, 1, 7, 8, 5, 10),
            Block.box(10, 1, 4, 13, 5, 5),
            Block.box(10, 1, 12, 13, 5, 13),
            Block.box(9, 1, 11, 14, 5, 12),
            Block.box(9, 1, 5, 14, 5, 6),
            Block.box(8, 5, 7, 15, 6, 10),
            Block.box(9, 5, 6, 14, 6, 7),
            Block.box(9, 5, 10, 14, 6, 11),
            Block.box(10, 5, 5, 13, 6, 6),
            Block.box(10, 5, 11, 13, 6, 12),
            Block.box(9, 6, 7, 14, 7, 10),
            Block.box(10, 6, 6, 13, 7, 7),
            Block.box(10, 6, 10, 13, 7, 11),
            Block.box(9, 7, 7, 14, 13, 10),
            Block.box(10, 7, 6, 13, 13, 7),
            Block.box(10, 7, 10, 13, 13, 11),
            Block.box(10, 13, 7, 13, 15, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_EAST = Stream.of(
            Block.box(5, 0, 0, 10, 6, 5),
            Block.box(6, 0, 8, 9, 1, 15),
            Block.box(9, 0, 9, 10, 1, 14),
            Block.box(5, 0, 9, 6, 1, 14),
            Block.box(10, 0, 10, 11, 1, 13),
            Block.box(4, 0, 10, 5, 1, 13),
            Block.box(6, 1, 15, 9, 5, 16),
            Block.box(5, 1, 8, 10, 5, 15),
            Block.box(6, 1, 7, 9, 5, 8),
            Block.box(11, 1, 10, 12, 5, 13),
            Block.box(3, 1, 10, 4, 5, 13),
            Block.box(4, 1, 9, 5, 5, 14),
            Block.box(10, 1, 9, 11, 5, 14),
            Block.box(6, 5, 8, 9, 6, 15),
            Block.box(9, 5, 9, 10, 6, 14),
            Block.box(5, 5, 9, 6, 6, 14),
            Block.box(10, 5, 10, 11, 6, 13),
            Block.box(4, 5, 10, 5, 6, 13),
            Block.box(6, 6, 9, 9, 7, 14),
            Block.box(9, 6, 10, 10, 7, 13),
            Block.box(5, 6, 10, 6, 7, 13),
            Block.box(6, 7, 9, 9, 13, 14),
            Block.box(9, 7, 10, 10, 13, 13),
            Block.box(5, 7, 10, 6, 13, 13),
            Block.box(6, 13, 10, 9, 15, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_SOUTH = Stream.of(
            Block.box(11, 0, 5, 16, 6, 10),
            Block.box(1, 0, 6, 8, 1, 9),
            Block.box(2, 0, 9, 7, 1, 10),
            Block.box(2, 0, 5, 7, 1, 6),
            Block.box(3, 0, 10, 6, 1, 11),
            Block.box(3, 0, 4, 6, 1, 5),
            Block.box(0, 1, 6, 1, 5, 9),
            Block.box(1, 1, 5, 8, 5, 10),
            Block.box(8, 1, 6, 9, 5, 9),
            Block.box(3, 1, 11, 6, 5, 12),
            Block.box(3, 1, 3, 6, 5, 4),
            Block.box(2, 1, 4, 7, 5, 5),
            Block.box(2, 1, 10, 7, 5, 11),
            Block.box(1, 5, 6, 8, 6, 9),
            Block.box(2, 5, 9, 7, 6, 10),
            Block.box(2, 5, 5, 7, 6, 6),
            Block.box(3, 5, 10, 6, 6, 11),
            Block.box(3, 5, 4, 6, 6, 5),
            Block.box(2, 6, 6, 7, 7, 9),
            Block.box(3, 6, 9, 6, 7, 10),
            Block.box(3, 6, 5, 6, 7, 6),
            Block.box(2, 7, 6, 7, 13, 9),
            Block.box(3, 7, 9, 6, 13, 10),
            Block.box(3, 7, 5, 6, 13, 6),
            Block.box(3, 13, 6, 6, 15, 9)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_WEST = Stream.of(
            Block.box(6, 0, 11, 11, 6, 16),
            Block.box(7, 0, 1, 10, 1, 8),
            Block.box(6, 0, 2, 7, 1, 7),
            Block.box(10, 0, 2, 11, 1, 7),
            Block.box(5, 0, 3, 6, 1, 6),
            Block.box(11, 0, 3, 12, 1, 6),
            Block.box(7, 1, 0, 10, 5, 1),
            Block.box(6, 1, 1, 11, 5, 8),
            Block.box(7, 1, 8, 10, 5, 9),
            Block.box(4, 1, 3, 5, 5, 6),
            Block.box(12, 1, 3, 13, 5, 6),
            Block.box(11, 1, 2, 12, 5, 7),
            Block.box(5, 1, 2, 6, 5, 7),
            Block.box(7, 5, 1, 10, 6, 8),
            Block.box(6, 5, 2, 7, 6, 7),
            Block.box(10, 5, 2, 11, 6, 7),
            Block.box(5, 5, 3, 6, 6, 6),
            Block.box(11, 5, 3, 12, 6, 6),
            Block.box(7, 6, 2, 10, 7, 7),
            Block.box(6, 6, 3, 7, 7, 6),
            Block.box(10, 6, 3, 11, 7, 6),
            Block.box(7, 7, 2, 10, 13, 7),
            Block.box(6, 7, 3, 7, 13, 6),
            Block.box(10, 7, 3, 11, 13, 6),
            Block.box(7, 13, 3, 10, 15, 6)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public BrewingAlembicBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .noOcclusion()
                .requiresCorrectToolForDrops()
                .strength(3.0f)
                .sound(SoundType.METAL));
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

            if(blockEntity instanceof BrewingAlembicBE) {
                NetworkHooks.openGui(((ServerPlayer) player), (MenuProvider) blockEntity, blockEntity.getBlockPos());

                // TODO: Remove debug
                System.out.println("\n");
                LazyOptional<IAlcoholCapability> cap = player.getCapability(CapabilityAlcohol.ALCOHOL_CAPABILITY);
                cap.ifPresent((alcohol) -> {
                    System.out.println("Alcohol: " + alcohol.getAlcohol());
                    alcohol.addAlcohol(1);
                });

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
        return ModBlockEntities.BREWING_ALEMBIC_BE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        } else {
            return (level1, pos, state1, blockEntity) -> {
                if (blockEntity instanceof BrewingAlembicBE brewingAlembicBE) {
                    brewingAlembicBE.tickServer(brewingAlembicBE);
                }
            };
        }
    }
}
