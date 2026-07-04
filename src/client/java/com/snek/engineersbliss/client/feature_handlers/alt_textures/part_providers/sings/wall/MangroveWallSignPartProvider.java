package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class MangroveWallSignPartProvider extends __base_WallSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "mangrove";
    }

    @Override
    public Block getBlock() {
        return Blocks.MANGROVE_WALL_SIGN;
    }
}
