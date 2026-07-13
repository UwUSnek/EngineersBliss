package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.powerable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class PoweredRailPartProvider extends __base_PowerableRailPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.POWERED_RAIL;
    }


    @Override
    protected String getRailTypeName() {
        return "powered_rail";
    }
}
