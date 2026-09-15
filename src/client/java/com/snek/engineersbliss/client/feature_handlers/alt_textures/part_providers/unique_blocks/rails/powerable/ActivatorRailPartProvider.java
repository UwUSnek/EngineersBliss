package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.unique_blocks.rails.powerable;

import net.minecraft.world.level.block.Block;




public class ActivatorRailPartProvider extends __base_PowerableRailPartProvider {
    public ActivatorRailPartProvider(Block targetBlock) {
        super(targetBlock);
    }


    @Override
    protected String getRailTypeName() {
        return "activator_rail";
    }
}
