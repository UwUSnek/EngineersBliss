package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class OakWallSignPartProvider extends __base_WallSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "oak";
    }

    @Override
    public Block getBlock() {
        return BlockSets.SIGN.standing().wall().oak();
    }
}
