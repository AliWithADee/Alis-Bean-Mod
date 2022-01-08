package io.github.aliwithadee.alisbeanmod.core.world.dimension;

import io.github.aliwithadee.alisbeanmod.common.general.block.TestTeleportBlock;
import io.github.aliwithadee.alisbeanmod.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class BeaniverseTeleporter implements ITeleporter {

    public static BlockPos thisPos = BlockPos.ZERO;
    public static boolean insideBeaniverse = true;

    public BeaniverseTeleporter(BlockPos pos, boolean insideDimension) {
        thisPos = pos;
        insideBeaniverse = insideDimension;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destinationWorld,
                              float yaw, Function<Boolean, Entity> repositionEntity) {

        entity = repositionEntity.apply(false);
        double y = 61;

        if (!insideBeaniverse) {
            y = thisPos.getY();
        }

        BlockPos destinationPos = new BlockPos(thisPos.getX(), y, thisPos.getZ());

        int tries = 0;
        while ((destinationWorld.getBlockState(destinationPos).getMaterial() != Material.AIR) &&
                !destinationWorld.getBlockState(destinationPos).canBeReplaced(Fluids.WATER) &&
                destinationWorld.getBlockState(destinationPos.above()).getMaterial() != Material.AIR &&
                !destinationWorld.getBlockState(destinationPos.above()).canBeReplaced(Fluids.WATER) && tries < 25) {
            destinationPos = destinationPos.above(2);
            tries++;
        }

        entity.setPos(destinationPos.getX(), destinationPos.getY(), destinationPos.getZ());

        if (insideBeaniverse) {
            boolean doSetBlock = true;
            for (BlockPos checkPos : BlockPos.betweenClosed(destinationPos.below(10).west(10), destinationPos.above(10).east(10))) {
                if (destinationWorld.getBlockState(checkPos).getBlock() instanceof TestTeleportBlock) {
                    doSetBlock = false;
                    break;
                }
            }
            if (doSetBlock) {
                destinationWorld.setBlock(destinationPos, ModBlocks.TEST_TELEPORT_BLOCK.get().defaultBlockState(), 3);
            }
        }

        return entity;
    }
}
