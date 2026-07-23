package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class SpruceStandingSignPartProvider extends __base_StandingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "spruce";
    }

    @Override
    public Block getBlock() {
        return Blocks.SPRUCE_SIGN;
    }
}
