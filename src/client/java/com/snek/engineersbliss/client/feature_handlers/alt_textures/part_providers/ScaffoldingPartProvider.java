package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

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
    public @Nullable List<String> calcPartNames(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING) ?
            List.of("scaffolding/unobstructive/" + (state.getValue(ScaffoldingBlock.BOTTOM).booleanValue() ? "unstable" : "stable") + "_n") :
            null
        ;
    }

    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING);
    }
}