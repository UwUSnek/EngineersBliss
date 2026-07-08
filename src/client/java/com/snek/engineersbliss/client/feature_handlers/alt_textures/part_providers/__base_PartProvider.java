package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_PartProvider {
    public abstract Block getBlock();
    public abstract List<String> calcPartNames(BlockState state, final boolean suffix);
    public abstract boolean shouldUseCustom(BlockState state);
    public abstract boolean shouldKeepVanilla(BlockState state);



    /**
     * Calculates all the IDs of the Json model parts required to display the provided BlockState.
     * @param state The BlockState.
     * @param suffix Whether to add the rotation/orientation suffixes.
     *      All models stored in memory have suffixes. Raw, concrete Json files don't.
     *      Passing false will also make the function run a deduplication step after calculating the IDs.
     */
    public final List<Identifier> calcPartIds(final BlockState state, final boolean suffix) {
        final List<Identifier> r = new ArrayList<>();
        for(final String name : calcPartNames(state, suffix)) {
            r.add(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block/" + name));
        }
        return suffix ? r : r.stream().distinct().toList();
    }




    protected static String getSingleVariantSuffix(final boolean suffix) {
        return suffix ? "_n" : "";
    }
    protected static String getVariantSuffixFromDirection(final Direction direction, final boolean suffix) {
        if(!suffix) return "";
        return switch(direction) {
            case NORTH -> "_n";
            case EAST  -> "_e";
            case SOUTH -> "_s";
            case WEST  -> "_w";
            default    -> "";
        };
    }
    protected static String getVariantSuffixFromAxis(final Axis axis, final boolean suffix) {
        if(!suffix) return "";
        return switch(axis) {
            case X  -> "_x";
            case Y  -> "_y";
            case Z  -> "_z";
            default -> "";
        };
    }
    protected String getVariantSuffixFromRotationIndex(final int rotation, final boolean suffix) {
        final int quadrantRotation = rotation % 4;
        final int quadrantIndex    = rotation / 4;
        final Direction direction = switch(quadrantIndex) {
            case 0  -> Direction.NORTH;
            case 1  -> Direction.EAST;
            case 2  -> Direction.SOUTH;
            default -> Direction.WEST; //! 3, no other value is possible here
        };
        return "_" + quadrantRotation + getVariantSuffixFromDirection(direction, suffix);
    }
}