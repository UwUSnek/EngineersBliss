package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class LightBlueBedPartProvider extends __base_BedPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.LIGHT_BLUE_BED;
    }

    protected String getColorName() {
        return "light_blue";
    }
}
