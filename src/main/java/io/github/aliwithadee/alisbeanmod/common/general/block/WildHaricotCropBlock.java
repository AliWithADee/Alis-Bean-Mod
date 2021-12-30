package io.github.aliwithadee.alisbeanmod.common.general.block;

import io.github.aliwithadee.alisbeanmod.core.init.general.GeneralItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

public class WildHaricotCropBlock extends CropBlock {

    public WildHaricotCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return GeneralItems.WILD_HARICOT_BEANS.get();
    }
}
