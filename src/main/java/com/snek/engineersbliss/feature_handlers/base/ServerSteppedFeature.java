package com.snek.engineersbliss.feature_handlers.base;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;




/**
 * A server feature that can assume a limited amount of predetermined values.
 * ! The type of this feature is technically Integer, as the default and current values
 * ! represent the index of the selected value instead of the actual numerical value.
 */
public class ServerSteppedFeature<T> extends __base_ServerFeature<Integer> {
    protected final List<T> values;

    public List<T> getValues() { return values; }


    public ServerSteppedFeature(final String id, final List<T> values, final int defaultIndex) {
        this(id, values, defaultIndex, null);
    }
    public ServerSteppedFeature(final String id, final List<T> values, final int defaultIndex, final @Nullable BiConsumer<Player, Integer> afterChangeCallback) {
        super(id, defaultIndex, afterChangeCallback);
        this.values = values;
    }
}
