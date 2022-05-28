package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class PlantedObeliskBlock extends ObeliskBlock implements BonemealableBlock, IPlantable {
    private static final int MAX_AGE = 14;
    private static final IntegerProperty AGE = IntegerProperty.create("age", 0, 14);

    public PlantedObeliskBlock() {
        super();
        registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, AGE);
    }

    private static void giveItems(Level level, BlockState state, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            List<ItemStack> drops = getDrops(state.setValue(HALF, DoubleBlockHalf.LOWER), (ServerLevel) level, pos, null);
            for (ItemStack drop : drops) {
                if (!drop.is(ModBlocks.OBELISK.get().asItem())) {
                    player.addItem(drop);
                }
            }
        }
    }

    @NotNull
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (isMaxAge(state)) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.is(Items.AIR)) {
                giveItems(level, state, pos, player);
                updateAge(level, pos, 7);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) >= MAX_AGE;
    }

    protected void updateAge(Level level, BlockPos pos, int newAge) {
        BlockState state = level.getBlockState(pos);
        if(!state.is(this)) return;

        level.setBlockAndUpdate(pos, state.setValue(AGE, newAge));

        BlockPos posOther = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
        BlockState stateOther = level.getBlockState(posOther);
        if (stateOther.is(state.getBlock())) {
            level.setBlockAndUpdate(posOther, stateOther.setValue(AGE, newAge));
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) return;

        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.is(Blocks.FARMLAND) && stateBelow.getValue(FarmBlock.MOISTURE) > 0) {
            int curAge = state.getValue(AGE);
            if (level.getRawBrightness(pos, 0) >= 9 && curAge < MAX_AGE) {
                float growthSpeed = getGrowthSpeed(this, level, pos);
                if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int)(25.0F / growthSpeed) + 1) == 0)) {
                    updateAge(level, pos, curAge + 1);
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
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !isMaxAge(state);
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
        int newAge = state.getValue(AGE) + getBoneMealAgeIncrease(level);
        if (newAge > MAX_AGE) {
            newAge = MAX_AGE;
        }
        updateAge(level, pos, newAge);
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
