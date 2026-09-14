package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;


public class ExposedCopperGolemStatuePartProvider extends __base_CopperGolemStatuePartProvider {

    @Override
    protected String getGolemName() {
        return "exposed";
    }


    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.EXPOSED_COPPER_GOLEM_STATUE;
        //? } else {
            /*return Blocks.COPPER_GOLEM_STATUE.weathering().exposed();
        *///? }
    }
}
