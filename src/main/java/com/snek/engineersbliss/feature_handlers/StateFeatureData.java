package com.snek.engineersbliss.feature_handlers;

import java.util.HashSet;
import java.util.Set;

import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








/**
 * A simple container that keeps track of all features that affect a BlockState.
 * ! Servers keep one of these for each possible BlockState.
 * ! Clients don't keep their own copy. //TODO idk if this is correct
 */
public class StateFeatureData {
    private Set<Integer> activeFeatures = new HashSet<>();
    private Set<Integer> activeFeatureSets = new HashSet<>();



    public boolean hasFeatures() {
        return !activeFeatures.isEmpty();
    }

    public boolean hasFeaturesFromSet(__base_ServerFeatureSet featureSet) {
        return activeFeatureSets.contains(featureSet.getHash());
    }

    @SuppressWarnings("unchecked")
    public <T> boolean hasFeature(__base_ServerFeature<T> feature) {
        return activeFeatures.contains(feature.getHash());
    }

    public <T> void addFeature(__base_ServerFeature<T> feature) {
        activeFeatures.add(feature.getHash());
        activeFeatureSets.add(feature.getFeatureSet().getHash());
    }
}
