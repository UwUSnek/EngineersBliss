package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;


//FIXME
//FIXME
//FIXME
//FIXME

public class OakHangingSignPartProvider extends __base_PartProvider {

    @Override
    public Block getBlock() {
        return Blocks.OAK_HANGING_SIGN;
    }


    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK) ?
            List.of("honey_block/transparent/block_n") :
            null
        ;
    }

    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK);
    }
}