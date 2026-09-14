package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.doublable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedWeatheredCopperChestPartProvider extends __base_DoublableChestPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.WAXED_WEATHERED_COPPER_CHEST;
        //? } else {
            /*return Blocks.COPPER_CHEST.waxed().weathered();
        *///? }
    }

    @Override
    protected String getChestName() {
        return "weathered_copper";
    }
}