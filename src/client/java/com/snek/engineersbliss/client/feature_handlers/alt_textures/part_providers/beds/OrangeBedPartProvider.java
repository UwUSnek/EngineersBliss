package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class OrangeBedPartProvider extends __base_BedPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.ORANGE_BED;
    }

    protected String getColorName() {
        return "orange";
    }
}
