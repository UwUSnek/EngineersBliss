package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class OxidizedCopperChestPartProvider extends __base_ChestPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.OXIDIZED_COPPER_CHEST;
    }

    @Override
    protected String getChestName() {
        return "oxidized_copper";
    }
}