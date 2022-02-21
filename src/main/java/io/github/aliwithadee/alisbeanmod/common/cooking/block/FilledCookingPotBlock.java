package io.github.aliwithadee.alisbeanmod.common.cooking.block;

import io.github.aliwithadee.alisbeanmod.core.init.ModBlockEntities;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class FilledCookingPotBlock extends CookingPotBlock implements EntityBlock {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 3;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", MIN_LEVEL, MAX_LEVEL);
    public static final BooleanProperty BOILING = BooleanProperty.create("boiling");

    @Override
    protected InteractionResult emptyBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (!level.isClientSide && isFull(state)) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));

            level.setBlockAndUpdate(pos, ModBlocks.COOKING_POT.get().defaultBlockState());

            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult fillBottle(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (!level.isClientSide) {
            FilledCookingPotBE blockEntity = (FilledCookingPotBE) level.getBlockEntity(pos);

            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, blockEntity.getDrinkResult()));
            lowerFillLevel(state, level, pos);

            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult fillBowl(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (!level.isClientSide) {
            FilledCookingPotBE blockEntity = (FilledCookingPotBE) level.getBlockEntity(pos);

            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, blockEntity.getDishResult()));
            lowerFillLevel(state, level, pos);

            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult addIngredient(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (isFull(state)) {
            if (!level.isClientSide) {
                FilledCookingPotBE blockEntity = (FilledCookingPotBE) level.getBlockEntity(pos);
                blockEntity.addIngredient(new ItemStack(stack.getItem(), 1));

                player.getItemInHand(hand).shrink(1);
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public void lowerFillLevel(BlockState state, Level level, BlockPos pos) {
        int newLevel = state.getValue(LEVEL) - 1;
        level.setBlockAndUpdate(pos, newLevel < MIN_LEVEL ? ModBlocks.COOKING_POT.get().defaultBlockState() : state.setValue(LEVEL, newLevel));
    }

    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == MAX_LEVEL;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity.isOnFire() && entityInsideContent(state, pos, entity)) {
            entity.clearFire();
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
        builder.add(LEVEL, BOILING);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(ModBlocks.COOKING_POT.get());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.FILLED_COOKING_POT_BE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        } else {
            return (level1, pos, state1, blockEntity) -> {
                if (blockEntity instanceof FilledCookingPotBE cookingPotBE) {
                    cookingPotBE.tickServer(cookingPotBE);
                }
            };
        }
    }

    private void spawnBubble(Level level, BlockPos pos, Random random) {

        double h_offset = 0.25d;
        double v_offset = 1.15d;

        level.addParticle(ParticleTypes.SMOKE, pos.getX() + h_offset,
                pos.getY() + v_offset, pos.getZ() + h_offset + random.nextDouble(1 - h_offset*2),
                0d, 0.015d + random.nextDouble(0.075d), 0d);

        level.addParticle(ParticleTypes.BUBBLE_POP, pos.getX() + h_offset,
                pos.getY() + v_offset, pos.getZ() + h_offset + random.nextDouble(1 - h_offset*2),
                0d, 0.015d + random.nextDouble(0.075d), 0d);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (state.getValue(BOILING)) {
            float chance = 0.65f;

            for (int i = 0; i < 4; i++) {
                if (chance > random.nextFloat()) {
                    spawnBubble(level, pos, random);
                }
            }
        }
    }
}
