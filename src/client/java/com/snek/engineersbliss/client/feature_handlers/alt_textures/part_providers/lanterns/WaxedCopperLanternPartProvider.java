package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedCopperLanternPartProvider extends __base_LanternPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.COPPER_LANTERN.waxed();
        //? } else {
            /*return Blocks.COPPER_LANTERN.waxed().unaffected();
        *///? }
    }

    @Override
    protected String getLanternName() {
        return "copper";
    }
}