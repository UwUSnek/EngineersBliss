package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.doublable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedCopperChestPartProvider extends __base_DoublableChestPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.WAXED_COPPER_CHEST;
    }

    @Override
    protected String getChestName() {
        return "copper";
    }
}