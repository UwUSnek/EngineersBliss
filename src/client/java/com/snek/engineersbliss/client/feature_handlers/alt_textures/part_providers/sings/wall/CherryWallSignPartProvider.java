package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class CherryWallSignPartProvider extends __base_WallSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "cherry";
    }

    @Override
    public Block getBlock() {
        return Blocks.CHERRY_WALL_SIGN;
    }
}
