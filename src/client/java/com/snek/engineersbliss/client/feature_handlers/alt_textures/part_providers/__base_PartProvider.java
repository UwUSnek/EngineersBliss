package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_PartProvider {
    public abstract Block getBlock();
    public abstract @Nullable List<String> calcPartNames(BlockState state);
    public abstract boolean shouldKeepVanilla(BlockState state);




    protected static String getVariantSuffixFromDirection(final Direction direction) {
        return switch(direction) {
            case NORTH -> "_n";
            case EAST  -> "_e";
            case SOUTH -> "_s";
            case WEST  -> "_w";
            default    -> "";
        };
    }
    protected static String getVariantSuffixFromAxis(final Axis axis) {
        return switch(axis) {
            case X  -> "_x";
            case Y  -> "_y";
            case Z  -> "_z";
            default -> "";
        };
    }
    protected String getVariantSuffixFromRotationIndex(final int rotation) {
        final int quadrantRotation = rotation % 4;
        final int quadrantIndex    = rotation / 4;
        final Direction direction = switch(quadrantIndex) {
            case 0  -> Direction.NORTH;
            case 1  -> Direction.EAST;
            case 2  -> Direction.SOUTH;
            default -> Direction.WEST; //! 3, no other value is possible here
        };
        return "_" + quadrantRotation + getVariantSuffixFromDirection(direction);
    }
}