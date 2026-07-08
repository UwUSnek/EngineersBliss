package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.powerable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class DetectorRailPartProvider extends __base_PowerableRailPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.DETECTOR_RAIL;
    }


    @Override
    protected String getRailTypeName() {
        return "detector_rail";
    }
}
