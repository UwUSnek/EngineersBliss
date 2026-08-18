package com.snek.engineersbliss.feature_handlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;








/**
 * A simple container for the current state of all features of a Player.
 * ! Servers keep one of these for each connected Player.
 * ! Clients keep their own independent local copy for their LocalPlayer.
 */
public class PlayerFeatureData {
    private Map<Integer, Object> values = null;


    private void ensureInitialized() {
        if(values == null) {
            values = new ConcurrentHashMap<>();
            for(final @NotNull __base_ServerFeature<?> feature : __base_ServerFeature.getAllFeatures().values()) {
                values.put(feature.getHash(), feature.getDefault());
            }
        }
    }



    @SuppressWarnings("unchecked")
    public <T> T getValue(__base_ServerFeature<T> feature) {
        ensureInitialized();
        return (T)values.get(feature.getHash());
    }

    public <T> void setValue(__base_ServerFeature<T> feature, T value) {
        ensureInitialized();
        values.put(feature.getHash(), value);
    }
}
