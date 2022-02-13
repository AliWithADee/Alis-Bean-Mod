package io.github.aliwithadee.alisbeanmod.common.general.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class ObeliskBlock extends Block implements BonemealableBlock, IPlantable {
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    public ObeliskBlock() {
        super(BlockBehaviour.Properties.of(Material.BAMBOO)
                .noOcclusion()
                .requiresCorrectToolForDrops()
                .strength(1.5f)
                .sound(SoundType.BAMBOO));
        registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, AGE);
    }

    // TODO
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        //level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @NotNull
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return InteractionResult.SUCCESS;
    }

    @NotNull
    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK; // TODO: Test
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide) {
            DoubleBlockHalf half = state.getValue(HALF);
            BlockPos posOther = half == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
            BlockState stateOther = level.getBlockState(posOther);
            if (stateOther.is(state.getBlock()) && stateOther.getValue(HALF) != half) {
                level.setBlock(posOther, Blocks.AIR.defaultBlockState(), 35);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    public int getMaxAge() {
        return 7;
    }

    public boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) >= getMaxAge();
    }

    protected int getAge(BlockState pState) {
        return pState.getValue(AGE);
    }

    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(AGE, age);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) return;

        if (level.getRawBrightness(pos, 0) >= 9) {
            int curAge = getAge(state);
            if (curAge < getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, level, pos);
                if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int)(25.0F / growthSpeed) + 1) == 0)) {
                    level.setBlock(pos, getStateForAge(curAge + 1), 2);
                    level.setBlock(pos.above(), getStateForAge(curAge + 1), 2);
                    ForgeHooks.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }

    private static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockpos = pos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                BlockState blockstate = level.getBlockState(blockpos.offset(i, 0, j));
                if (blockstate.canSustainPlant(level, blockpos.offset(i, 0, j), Direction.UP, (IPlantable) block)) {
                    f1 = 1.0F;
                    if (blockstate.isFertile(level, pos.offset(i, 0, j))) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        return f;
    }

    private int getBoneMealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 2, 5);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return !isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        int newAge = getAge(state) + getBoneMealAgeIncrease(level);
        if (newAge > getMaxAge()) {
            newAge = getMaxAge();
        }

        level.setBlock(pos, getStateForAge(newAge), 2);
        level.setBlock(pos.above(), getStateForAge(newAge), 2);
    }

    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.CROP;
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return defaultBlockState();
        return state;
    }
}
