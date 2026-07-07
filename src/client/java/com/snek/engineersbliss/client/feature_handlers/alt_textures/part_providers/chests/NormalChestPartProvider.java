package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class NormalChestPartProvider extends __base_ChestPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.CHEST;
    }

    @Override
    protected String getChestName() {
        return "normal";
    }
}