package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.ceiling_hanging;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class JungleCeilingHangingSignPartProvider extends __base_CeilingHangingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "jungle";
    }

    @Override
    public Block getBlock() {
        return Blocks.JUNGLE_HANGING_SIGN;
    }
}
