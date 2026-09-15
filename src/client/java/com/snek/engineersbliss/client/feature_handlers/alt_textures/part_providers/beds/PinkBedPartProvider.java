package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class PinkBedPartProvider extends __base_BedPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.BED.pink();
    }

    protected String getColorName() {
        return "pink";
    }
}
