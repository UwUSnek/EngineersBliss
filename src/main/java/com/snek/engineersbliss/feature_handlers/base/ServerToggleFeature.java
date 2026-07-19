package com.snek.engineersbliss.feature_handlers.base;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;




/**
 * A server feature that can only be toggled ON or OFF.
 */
public class ServerToggleFeature extends __base_ServerFeature<Boolean> {

    public ServerToggleFeature(final String id, final boolean defaultValue) {
        this(id, defaultValue, null);
    }
    public ServerToggleFeature(final String id, final boolean defaultValue, final @Nullable BiConsumer<Player, Boolean> afterChangeCallback) {
        super(id, defaultValue, afterChangeCallback);
    }
}
