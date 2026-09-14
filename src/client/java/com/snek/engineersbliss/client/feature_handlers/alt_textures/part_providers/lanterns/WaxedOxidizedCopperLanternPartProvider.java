package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedOxidizedCopperLanternPartProvider extends __base_LanternPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.COPPER_LANTERN.waxedOxidized();
        //? } else {
            /*return Blocks.COPPER_LANTERN.waxed().oxidized();
        *///? }
    }

    @Override
    protected String getLanternName() {
        return "oxidized_copper";
    }
}