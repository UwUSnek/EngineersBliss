package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class BlackBedPartProvider extends __base_BedPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.BLACK_BED;
    }

    protected String getColorName() {
        return "black";
    }
}
