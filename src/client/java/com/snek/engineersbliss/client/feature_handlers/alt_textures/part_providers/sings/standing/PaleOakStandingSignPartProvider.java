package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class PaleOakStandingSignPartProvider extends __base_StandingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "pale_oak";
    }

    @Override
    public Block getBlock() {
        return Blocks.PALE_OAK_SIGN;
    }
}
