package com.snek.engineersbliss.feature_handlers.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.EngineerSBliss;




/**
 * The base class for all server-side mod features sets.
 * A feature set is simply a collection of features of different types.
 * ! All features in a featur set must be initialized by calling __base_ServerFeature.onSetInit(set).
 * ! This forces static initialization of the feature set class and registers all of its features for later operations.
 */
public abstract class __base_ServerFeatureSet {
    protected final String id;
    protected final int hash;
    protected final Map<Integer, __base_ServerFeature<?>> features;
    protected boolean initialized = false;


    public String getId() { return id; }
    public int getHash() { return hash; }
    public Map<Integer, __base_ServerFeature<?>> getFeatures() {
        initializedOrThrow();
        return features;
    }


    protected __base_ServerFeatureSet(final String id) {
        this.id = id;
        this.hash = id.hashCode();
        this.features = new ConcurrentHashMap<>();
    }
    protected <F extends __base_ServerFeature<?>> F registerFeature(final @NotNull F feature) {
        __base_ServerFeature.onRegisterFeature(feature, this); //! This computes the feature's hash
        features.put(feature.getHash(), feature);
        return feature;
    }




    /**
     * Checks if the feature set is initialized. Throws an exception if not.
     */
    public void initializedOrThrow() {
        if(!initialized) {
            throw new IllegalStateException("Feature set \"" + getId() + "\" used before initialization");
        }
    }


    /**
     * This triggers the static initialization.
     * It stores all features defined by this set in a list shared with the other sets, initializing their hash value.
     */
    public void init() {
        if(initialized) {
            EngineerSBliss.LOGGER.error("init function of feature set {} called twice", getId(), new Throwable());
        }
        else {
            //! Features are registered and initialized by the static initializer
            initialized = true;
        }
    }
}