package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class PinkBedPartProvider extends __base_BedPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            // return Blocks.RED_PINK;
        //? } else {
            return Blocks.BED.pink();
        //? }
    }

    protected String getColorName() {
        return "pink";
    }
}
