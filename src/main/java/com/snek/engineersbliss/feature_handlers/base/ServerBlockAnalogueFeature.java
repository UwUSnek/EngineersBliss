package com.snek.engineersbliss.feature_handlers.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;




/**
 * A ServerAnalogueFeature that keeps track of the blocks it affects the rendering of.
 * ! This is used to refresh chunk sections and optimize branches in hot paths.
 */
public class ServerBlockAnalogueFeature extends ServerAnalogueFeature implements __base_BlockFeatureInterface {
    private final Set<Block> affectedBlocks = new HashSet<>();


    public Set<Block> getAffectedBlocks() {
        return affectedBlocks;
    }


    public ServerBlockAnalogueFeature(final String id, final double min, final double max, final double defaultValue, final List<Block> affectedBlocks) {
        this(id, min, max, defaultValue, affectedBlocks, null);
    }
    public ServerBlockAnalogueFeature(final String id, final double min, final double max, final double defaultValue, final List<Block> affectedBlocks, final @Nullable BiConsumer<Player, Double> afterChangeCallback) {
        super(id, min, max, defaultValue, afterChangeCallback);
        this.affectedBlocks.addAll(affectedBlocks);
    }
}
