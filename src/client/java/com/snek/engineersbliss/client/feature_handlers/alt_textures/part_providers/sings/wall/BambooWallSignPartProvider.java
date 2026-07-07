package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class BambooWallSignPartProvider extends __base_WallSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "bamboo";
    }

    @Override
    public Block getBlock() {
        return Blocks.BAMBOO_WALL_SIGN;
    }
}
