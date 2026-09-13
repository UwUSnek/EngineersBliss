package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedExposedCopperLanternPartProvider extends __base_LanternPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            // return Blocks.COPPER_LANTERN.waxedExposed();
        //? } else {
            return Blocks.COPPER_LANTERN.waxed().exposed();
        //? }
    }

    @Override
    protected String getLanternName() {
        return "exposed_copper";
    }
}