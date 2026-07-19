package com.snek.engineersbliss.feature_handlers.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;




/**
 * A ServerSteppedFeature that keeps track of the blocks it affects the rendering of.
 * ! This is used to refresh chunk sections and optimize branches in hot paths.
 */
public class ServerBlockSteppedFeature<T> extends ServerSteppedFeature<T> implements __base_BlockFeatureInterface {
    private final Set<Block> affectedBlocks = new HashSet<>();


    public Set<Block> getAffectedBlocks() {
        return affectedBlocks;
    }


    public ServerBlockSteppedFeature(final String id, final List<T> values, final int defaultIndex, final List<Block> affectedBlocks) {
        this(id, values, defaultIndex, affectedBlocks, null);
    }
    public ServerBlockSteppedFeature(final String id, final List<T> values, final int defaultIndex, final List<Block> affectedBlocks, final @Nullable BiConsumer<Player, Integer> afterChangeCallback) {
        super(id, values, defaultIndex, afterChangeCallback);
        this.affectedBlocks.addAll(affectedBlocks);
    }
}
