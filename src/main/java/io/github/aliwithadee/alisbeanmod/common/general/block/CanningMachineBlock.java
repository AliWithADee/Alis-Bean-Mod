package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.common.general.container.CanningMachineContainer;
import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class CanningMachineBlock extends Block implements EntityBlock {

    public CanningMachineBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .strength(8f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return GeneralBlockEntities.CANNING_MACHINE_BE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        } else {

            // Returns a BlockEntityTicker (functional interface) as a lambda expression.
            // Whatever called getTicker() will then call the tick() method on this BlockEntityTicker,
            // which will then call tickServer() on our block entity.
            return (level1, pos, state1, blockEntity) -> {
                if (blockEntity instanceof CanningMachineBE canningMachine) {
                    canningMachine.tickServer(blockEntity);
                }
            };
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flags) {
        list.add(new TranslatableComponent("tooltip.canning_machine"));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        // Only want to do something on server side
        // Only want to open container if player is not crouching
        if(!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if(blockEntity instanceof CanningMachineBE) {
                MenuProvider menuProvider = createMenuProvider(level, pos);

                NetworkHooks.openGui(((ServerPlayer) player), menuProvider, blockEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    private MenuProvider createMenuProvider(Level level, BlockPos pos) {
        return new MenuProvider() {

            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("screen.alisbeanmod.canning_machine");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                return new CanningMachineContainer(windowId, level, pos, playerInventory, playerEntity);
            }
        };
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof CanningMachineBE) {
                Containers.dropContents(level, pos, (CanningMachineBE) blockEntity);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
