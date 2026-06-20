package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class ActivatorRailPartProvider extends __base_RailPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.ACTIVATOR_RAIL;
    }


    @Override
    protected String getRailTypeName() {
        return "activator_rail";
    }
}
