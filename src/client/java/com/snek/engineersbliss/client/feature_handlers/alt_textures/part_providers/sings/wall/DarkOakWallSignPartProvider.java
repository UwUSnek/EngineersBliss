package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class DarkOakWallSignPartProvider extends __base_WallSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "dark_oak";
    }

    @Override
    public Block getBlock() {
        return Blocks.DARK_OAK_WALL_SIGN;
    }
}
