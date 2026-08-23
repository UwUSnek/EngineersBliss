package com.snek.engineersbliss.utils;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;








public class ServerMinecraftUtils {
    private ServerMinecraftUtils() {}


    /**
     * Creates a list containing all the blocks in the game. This includes modded blocks.
     * @return The list of blocks.
     */
    public static List<Block> fetchAllBlocks() {
        final List<Block> r = new ArrayList<>(BuiltInRegistries.BLOCK.size());
        for(final var b : BuiltInRegistries.BLOCK) r.add(b);
        if(r.isEmpty()) {
            throw new IllegalStateException("ServerMinecraftUtils.fetchAllBlocks() called before block registration");
        }
        return r;
    }


    /**
     * Returns a list containing all the possible block states for the specified block.
     * This is a simple wrapper for Minecraft's Block.getStateDefinition().getPossibleState(). O(1).
     * @param block The block to fetch all states of.
     * @return The list of possible block states.
     */
    public static ImmutableList<BlockState> fetchAllBlockStates(final @NotNull Block block) {
        return block.getStateDefinition().getPossibleStates();
    }


    /**
     * Returns a list containing all the possible block states for all blocks in the game. This includes modded blocks.
     * @return The list of blocks.
     */
    public static List<BlockState> fetchAllBlockStates() {
        final List<BlockState> r = new ArrayList<>();
        for(final var b : fetchAllBlocks()) r.addAll(fetchAllBlockStates(b));
        return r;
    }
}





//TODO move to library
