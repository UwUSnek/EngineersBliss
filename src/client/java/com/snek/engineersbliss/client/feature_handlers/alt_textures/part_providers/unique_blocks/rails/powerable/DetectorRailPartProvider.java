package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.unique_blocks.rails.powerable;

import net.minecraft.world.level.block.Block;




public class DetectorRailPartProvider extends __base_PowerableRailPartProvider {
    public DetectorRailPartProvider(Block targetBlock) {
        super(targetBlock);
    }


    @Override
    protected String getRailTypeName() {
        return "detector_rail";
    }
}
