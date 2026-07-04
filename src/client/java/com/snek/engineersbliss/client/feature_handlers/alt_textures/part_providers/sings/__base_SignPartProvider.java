package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_SignPartProvider extends __base_PartProvider {
    protected static String ROOT = "signs/vanilla";
    private static final List<Direction> DIRECTION_INEDX_TO_DIR = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);


    protected abstract String getSignMaterialName();


    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.STATIC_SIGNS);
    }


    protected String getVariantSuffixFromRotationIndex(final int rotation) {
        final int quadrantRotation = rotation % 4;
        final int quadrantIndex    = rotation / 4;
        return "_" + quadrantRotation + getVariantSuffixFromDirection(DIRECTION_INEDX_TO_DIR.get(quadrantIndex));
    }
}
