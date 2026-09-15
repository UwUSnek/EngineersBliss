package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;


public class OxidizedCopperGolemStatuePartProvider extends __base_CopperGolemStatuePartProvider {

    @Override
    protected String getGolemName() {
        return "oxidized";
    }


    @Override
    public Block getBlock() {
        return BlockSets.COPPER_GOLEM_STATUE.weathering().oxidized();
    }
}
