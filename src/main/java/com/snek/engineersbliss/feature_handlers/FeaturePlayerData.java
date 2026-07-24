package com.snek.engineersbliss.feature_handlers;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;








/**
 * A simple container for the current state of all features of a player.
 * ! Servers keep one of these for each connected player.
 * ! Clients keep their own independent local copy.
 */
public class FeaturePlayerData {
    private List<Object> values = null;


    private void ensureInitialized() {
        if(values == null) {
            values = new ArrayList<>();
            for(final __base_ServerFeature<?> feature : __base_ServerFeature.getAllFeatures()) {
                values.add(feature.getDefault());
            }
        }
    }



    public <T> T getValue(__base_ServerFeature<T> feature) {
        ensureInitialized();
        return (T)values.get(feature.getIndex());
    }

    public <T> void setValue(__base_ServerFeature<T> feature, T value) {
        ensureInitialized();
        values.set(feature.getIndex(), value);
    }
}
