package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;




public class ScaffoldingPartProvider extends __base_PartProvider {

    @Override
    public Block getBlock() {
        return Blocks.SCAFFOLDING;
    }




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String stabilityName = state.getValue(ScaffoldingBlock.BOTTOM).booleanValue() ? "unstable" : "stable";
        return List.of(String.format("scaffolding/unobstructive/%s%s", stabilityName, getSingleVariantSuffix()));
    }
    @Override
    public List<String> calcDependencyNames() {
        return List.of(
            "scaffolding/unobstructive/stable",
            "scaffolding/unobstructive/unstable"
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}