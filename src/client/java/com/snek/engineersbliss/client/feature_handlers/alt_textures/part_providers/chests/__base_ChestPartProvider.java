package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;




public abstract class __base_ChestPartProvider extends __base_PartProvider {

    protected abstract String getChestName();


    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_CHESTS)) {
            final String dirName = getVariantSuffixFromDirection(state.getValue(ChestBlock.FACING));
            final String sideName = switch(state.getValue(ChestBlock.TYPE)) {
                case ChestType.SINGLE -> "single";
                case ChestType.LEFT   -> "left";
                case ChestType.RIGHT  -> "right";
            };
            return List.of("chests/vanilla/" + sideName + "/" + getChestName() + dirName);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.STATIC_CHESTS);
    }
}