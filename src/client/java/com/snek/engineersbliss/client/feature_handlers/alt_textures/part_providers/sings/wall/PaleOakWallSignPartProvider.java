package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class PaleOakWallSignPartProvider extends __base_WallSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "pale_oak";
    }

    @Override
    public Block getBlock() {
        return BlockSets.SIGN.standing().wall().paleOak();
    }
}
