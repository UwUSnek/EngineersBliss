package com.snek.engineersbliss.feature_handlers.base;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;




/**
 * A server feature that can assume an unlimited number of possible values.
 */
public class ServerAnalogueFeature<T> extends __base_ServerFeature<T> {
    protected final T min;
    protected final T max;

    public T getMin() { return min; }
    public T getMax() { return max; }


    public ServerAnalogueFeature(final String id, final T min, final T max, final T defaultValue) {
        this(id, min, max, defaultValue, null);
    }
    public ServerAnalogueFeature(final String id, final T min, final T max, final T defaultValue, final @Nullable BiConsumer<Player, T> afterChangeCallback) {
        super(id, defaultValue, afterChangeCallback);
        this.min = min;
        this.max = max;
    }
}
