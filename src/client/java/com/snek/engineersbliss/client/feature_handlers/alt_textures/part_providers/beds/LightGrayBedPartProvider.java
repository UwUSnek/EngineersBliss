package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class LightGrayBedPartProvider extends __base_BedPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.LIGHT_GRAY_BED;
        *///? } else {
            return Blocks.BED.lightGray();
        //? }
    }

    protected String getColorName() {
        return "light_gray";
    }
}
