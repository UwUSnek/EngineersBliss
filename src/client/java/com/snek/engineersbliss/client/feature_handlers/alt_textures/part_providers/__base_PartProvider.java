package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_PartProvider {
    public abstract Block getBlock();
    public abstract @Nullable List<String> calcPartNames(BlockState state);
    public abstract boolean shouldKeepVanilla(BlockState state);


    protected static String getVariationSuffixFromDirection(final Direction direction) {
        return switch(direction) {
            case NORTH -> "_n";
            case EAST  -> "_e";
            case SOUTH -> "_s";
            case WEST  -> "_w";
            default    -> "";
        };
    }
}