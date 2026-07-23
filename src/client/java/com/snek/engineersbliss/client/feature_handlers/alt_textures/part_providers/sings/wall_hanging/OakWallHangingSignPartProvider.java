package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall_hanging;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class OakWallHangingSignPartProvider extends __base_WallHangingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "oak";
    }

    @Override
    public Block getBlock() {
        return Blocks.OAK_WALL_HANGING_SIGN;
    }
}
