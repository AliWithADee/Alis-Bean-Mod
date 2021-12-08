package io.github.aliwithadee.alisbeanmod.core.util;

import io.github.aliwithadee.alisbeanmod.AlisBeanMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static class Blocks {

        // Create new tag under Alis Bean Mod name space
        private static Tags.IOptionalNamedTag<Block> createTag(String name) {
            return BlockTags.createOptional(new ResourceLocation(AlisBeanMod.MOD_ID, name));
        }

        // Create new tag under Forge name space
        private static Tags.IOptionalNamedTag<Block> createForgeTag(String name) {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }
    }

    public static class Items {

        // To add items to this tag go to -> data/forge/tags/items/ingots/tin.json
        public static final Tags.IOptionalNamedTag<Item> TIN_INGOT = createForgeTag("ingots/tin");

        // Create new tag under Alis Bean Mod name space
        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(AlisBeanMod.MOD_ID, name));
        }

        // Create new tag under Forge name space
        private static Tags.IOptionalNamedTag<Item> createForgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }
}
