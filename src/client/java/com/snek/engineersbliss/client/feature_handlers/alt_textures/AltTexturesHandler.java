package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AltTexturesHandler {
    private AltTexturesHandler() {}
    private static long featureMask = AltTextureFeature.DEFAULT_FLAGS;




    public static void setFeature(final AltTextureFeature feature, final boolean value) {
        final long featureBit = feature.getFlagBit();
        if(value) featureMask |= featureBit; else featureMask &= ~featureBit;
    }

    public static boolean getFeature(final AltTextureFeature feature) {
        return feature.hasFlagBit(featureMask);
    }



    /**
     * Checks if the block of the specified block state is currently using a static model instead of its dynamic block entity rendering.
     * @param state The block state to check.
     * @return True if the block entity is using static rendering, false otherwise. Always false for non-block-entity blocks.
     */
    public static boolean isUsingStaticModel(final BlockState state) {
        return state.hasBlockEntity() && (
            (
                state.is(BlockTags.COPPER_CHESTS) ||
                state.is(Blocks.CHEST)            ||
                state.is(Blocks.TRAPPED_CHEST)    ||
                state.is(Blocks.ENDER_CHEST)
            ) && AltTextureFeature.STATIC_CHESTS.hasFlagBit(featureMask) ||
            (
                state.is(BlockTags.ALL_SIGNS)
            ) && AltTextureFeature.STATIC_SIGNS.hasFlagBit(featureMask)
        );
    }
}
