package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.ceiling_hanging;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class PaleOakCeilingHangingSignPartProvider extends __base_CeilingHangingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "pale_oak";
    }

    @Override
    public Block getBlock() {
        return Blocks.PALE_OAK_HANGING_SIGN;
    }
}
