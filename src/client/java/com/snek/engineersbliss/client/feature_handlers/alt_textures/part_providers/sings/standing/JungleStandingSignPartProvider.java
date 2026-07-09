package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class JungleStandingSignPartProvider extends __base_StandingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "jungle";
    }

    @Override
    public Block getBlock() {
        return Blocks.JUNGLE_SIGN;
    }
}
