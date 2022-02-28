package io.github.aliwithadee.alisbeanmod.common.general.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class PlantedObeliskBlock extends ObeliskBlock implements BonemealableBlock, IPlantable {
    private static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    public PlantedObeliskBlock() {
        super();
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

    @NotNull
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return InteractionResult.SUCCESS;
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
                    level.setBlock(pos, state.setValue(AGE, curAge + 1), 2);
                    level.setBlock(pos.above(), state.setValue(AGE, curAge + 1).setValue(HALF, DoubleBlockHalf.UPPER), 2);
                    ForgeHooks.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }

    private static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        float speed = 1.0F;

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float speed1 = 0.0F;
                BlockPos blockPos = pos.below().offset(i, 0, j);
                BlockState blockState = level.getBlockState(blockPos);

                if (blockState.canSustainPlant(level, blockPos, Direction.UP, (IPlantable) block)) {
                    speed1 = 1.0F;
                    if (blockState.isFertile(level, pos.offset(i, 0, j))) {
                        speed1 = 3.0F;
                    }
                }

                // If block is diagonal or directly below, divide by 4
                if (i != 0 || j != 0) {
                    speed1 /= 4.0F;
                }

                // Set speed1 to 0.
                // If block can sustain plant, set speed1 to 1
                // If block fertile, set speed1 to 3
                // If block is diagonal or directly below, divide by 4
                // Add speed1 to speed
                speed += speed1;
            }
        }

        return speed;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return !isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    private int getBoneMealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 2, 5);
    }

    @Override
    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        int newAge = getAge(state) + getBoneMealAgeIncrease(level);
        if (newAge > getMaxAge()) {
            newAge = getMaxAge();
        }

        level.setBlock(pos, state.setValue(AGE, newAge), 2);
        level.setBlock(pos.above(), state.setValue(AGE, newAge).setValue(HALF, DoubleBlockHalf.UPPER), 2);
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
