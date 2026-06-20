package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class RailPartProvider extends __base_RailPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.RAIL;
    }


    @Override
    protected String getRailTypeName() {
        return "rail";
    }
}
