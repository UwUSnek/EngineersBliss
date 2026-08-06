package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_PartProvider {
    public abstract Block getBlock();
    public abstract List<String> calcDependencyNames();
    public abstract List<String> calcPartNames(BlockState state, final int modelSetIndex);
    public abstract boolean shouldUseCustom(BlockState state);
    public abstract boolean shouldKeepVanilla(BlockState state);



    /**
     * Calculates all the IDs of the Json model parts required to display the provided BlockState.
     * @param state The BlockState.
     * @param modelSetIndex The index of the model set to generate calculate part IDs for.
     */
    public final List<Identifier> calcPartIds(final BlockState state, final int modelSetIndex) {
        final List<Identifier> r = new ArrayList<>();
        for(final String name : calcPartNames(state, modelSetIndex)) {
            r.add(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block/vanilla/" + name));
        }
        return r;
    }


    /**
     * Calculates all the IDs of the actual Json models this block's model sets depend on.
     */
    public final List<Identifier> calcDependencyIds() {
        final List<Identifier> r = new ArrayList<>();
        for(final String name : calcDependencyNames()) {
            r.add(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block/vanilla/" + name));
        }
        return r;
    }




    @SuppressWarnings("java:S3400")
    protected static String getSingleVariantSuffix() {
        return "_n";
    }
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




    /**
     * Returns the amount of possible models for each possible BlockState of the affected block, aka model sets.
     * @return The total number of model sets.
     */
    public int getModelSetNumber() {
        return 1;
    }

    /**
     * Calculates the index of the model set to use based on the currently active features.
     * @return The index of the model set to use.
     */
    public int calcCurrentModelSetIndex() {
        return 0;
    }
}