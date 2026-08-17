package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds.__base_BedPartProvider;



public class YellowBedPartProvider extends __base_BedPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.YELLOW_BED;
    }

    protected String getColorName() {
        return "yellow";
    }
}
