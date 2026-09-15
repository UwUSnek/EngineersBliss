package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.doublable;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class WaxedWeatheredCopperChestPartProvider extends __base_DoublableChestPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.CHEST.waxed().weathered();
    }

    @Override
    protected String getChestName() {
        return "weathered_copper";
    }
}