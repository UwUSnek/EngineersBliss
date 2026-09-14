package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;


public class WeatheredCopperGolemStatuePartProvider extends __base_CopperGolemStatuePartProvider {

    @Override
    protected String getGolemName() {
        return "weathered";
    }


    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.WEATHERED_COPPER_GOLEM_STATUE;
        //? } else {
            /*return Blocks.COPPER_GOLEM_STATUE.weathering().weathered();
        *///? }
    }
}
