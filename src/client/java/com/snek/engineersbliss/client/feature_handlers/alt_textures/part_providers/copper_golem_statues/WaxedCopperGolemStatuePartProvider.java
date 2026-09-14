package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;


public class WaxedCopperGolemStatuePartProvider extends __base_CopperGolemStatuePartProvider {

    @Override
    protected String getGolemName() {
        return "normal";
    }


    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.WAXED_COPPER_GOLEM_STATUE;
        *///? } else {
            return Blocks.COPPER_GOLEM_STATUE.waxed().unaffected();
        //? }
    }
}
