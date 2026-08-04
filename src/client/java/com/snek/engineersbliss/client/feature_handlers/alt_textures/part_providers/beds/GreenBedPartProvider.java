package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class GreenBedPartProvider extends __base_BedPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.GREEN_BED;
    }

    protected String getColorName() {
        return "green";
    }
}
