package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.doublable;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class WaxedCopperChestPartProvider extends __base_DoublableChestPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.CHEST.waxed().unaffected();
    }

    @Override
    protected String getChestName() {
        return "copper";
    }
}