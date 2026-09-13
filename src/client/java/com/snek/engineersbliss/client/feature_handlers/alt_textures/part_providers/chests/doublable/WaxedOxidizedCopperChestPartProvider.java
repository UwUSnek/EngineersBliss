package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.doublable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedOxidizedCopperChestPartProvider extends __base_DoublableChestPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            // return Blocks.WAXED_OXIDIZED_COPPER_CHEST;
        //? } else {
            return Blocks.COPPER_CHEST.waxed().oxidized();
        //? }
    }

    @Override
    protected String getChestName() {
        return "oxidized_copper";
    }
}