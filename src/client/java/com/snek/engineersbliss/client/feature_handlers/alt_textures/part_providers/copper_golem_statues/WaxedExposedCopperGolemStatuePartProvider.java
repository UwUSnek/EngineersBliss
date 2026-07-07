package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;


public class WaxedExposedCopperGolemStatuePartProvider extends __base_CopperGolemStatuePartProvider {

    @Override
    protected String getGolemName() {
        return "exposed";
    }


    @Override
    public Block getBlock() {
        return Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE;
    }
}
