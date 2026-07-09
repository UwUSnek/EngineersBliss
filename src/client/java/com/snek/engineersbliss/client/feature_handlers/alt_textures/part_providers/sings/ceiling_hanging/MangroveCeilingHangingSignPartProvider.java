package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.ceiling_hanging;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class MangroveCeilingHangingSignPartProvider extends __base_CeilingHangingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "mangrove";
    }

    @Override
    public Block getBlock() {
        return Blocks.MANGROVE_HANGING_SIGN;
    }
}
