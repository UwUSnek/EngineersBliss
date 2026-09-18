package com.snek.engineersbliss.feature_handlers.base;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;




/**
 * A server feature that can assume an unlimited number of possible values.
 */
public class ServerAnalogueFeature extends __base_ServerFeature<Double> {
    protected final double min;
    protected final double max;

    public double getMin() { return min; }
    public double getMax() { return max; }


    public ServerAnalogueFeature(final String id, final double min, final double max, final double defaultValue) {
        this(id, min, max, defaultValue, null);
    }
    public ServerAnalogueFeature(final String id, final double min, final double max, final double defaultValue, final @Nullable BiConsumer<Player, Double> afterChangeCallback) {
        super(id, defaultValue, afterChangeCallback);
        this.min = min;
        this.max = max;
    }
}
