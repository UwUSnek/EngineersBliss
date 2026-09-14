package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.doublable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedExposedCopperChestPartProvider extends __base_DoublableChestPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.WAXED_EXPOSED_COPPER_CHEST;
        *///? } else {
            return Blocks.COPPER_CHEST.waxed().exposed();
        //? }
    }

    @Override
    protected String getChestName() {
        return "exposed_copper";
    }
}