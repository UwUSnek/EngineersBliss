package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;


public class CopperGolemStatuePartProvider extends __base_CopperGolemStatuePartProvider {

    @Override
    protected String getGolemName() {
        return "normal";
    }


    @Override
    public Block getBlock() {
        return BlockSets.COPPER_GOLEM_STATUE.weathering().unaffected();
    }
}
