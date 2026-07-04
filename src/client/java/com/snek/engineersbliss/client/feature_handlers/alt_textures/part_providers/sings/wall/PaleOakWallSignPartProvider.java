package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class PaleOakWallSignPartProvider extends __base_WallSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "pale_oak";
    }

    @Override
    public Block getBlock() {
        return Blocks.PALE_OAK_WALL_SIGN;
    }
}
