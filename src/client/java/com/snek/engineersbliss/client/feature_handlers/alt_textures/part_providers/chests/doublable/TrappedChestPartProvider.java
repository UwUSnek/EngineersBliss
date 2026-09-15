package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.doublable;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class TrappedChestPartProvider extends __base_DoublableChestPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.CHEST.trapped();
    }

    @Override
    protected String getChestName() {
        return "trapped";
    }
}