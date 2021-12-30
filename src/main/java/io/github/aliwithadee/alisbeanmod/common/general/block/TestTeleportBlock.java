package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.core.world.dimension.BeaniverseTeleporter;
import io.github.aliwithadee.alisbeanmod.core.world.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("deprecation")
public class TestTeleportBlock extends Block {

    public TestTeleportBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            if (!player.isCrouching()) {
                MinecraftServer server = level.getServer();

                if (server != null) {
                    if (level.dimension() == ModDimensions.Beaniverse) {
                        ServerLevel overWorld = server.getLevel(Level.OVERWORLD);
                        if (overWorld != null) {
                            player.changeDimension(overWorld, new BeaniverseTeleporter(pos, false));
                        }
                    } else {
                        ServerLevel beaniverse = server.getLevel(ModDimensions.Beaniverse);
                        if (beaniverse != null) {
                            player.changeDimension(beaniverse, new BeaniverseTeleporter(pos, true));
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.use(state, level, pos, player, hand, hit);
    }


}
