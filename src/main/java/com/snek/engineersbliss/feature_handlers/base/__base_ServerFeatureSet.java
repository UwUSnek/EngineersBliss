package com.snek.engineersbliss.feature_handlers.base;

import java.util.ArrayList;
import java.util.List;

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
    protected final List<__base_ServerFeature<?>> features;
    protected boolean initialized = false;


    public String getId() { return id; }
    public List<__base_ServerFeature<?>> getFeatures() { return features; }


    protected __base_ServerFeatureSet(final String id) {
        this.id = id;
        this.features = new ArrayList<>();
    }
    protected <F extends __base_ServerFeature<?>> F registerFeature(final @NotNull F feature) {
        features.add(feature);
        __base_ServerFeature.onRegisterFeature(feature, this);
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
     * Stores all features defined by this set in a list shared with the other sets,
     * in order to allow for calculating the numerical ID when finalizeSetInits is called.
     * ! This is not done during feature creation or static init in order to force all feature sets to be fully initialized during the mod initialization stage.
     * ! This is required in order to calculate the index properly.
     * ! This must be called on all sets during server side mod initialization, before calling finalizeSetInits().
     */
    public void init() {
        if(initialized) {
            EngineerSBliss.LOGGER.error("init function of feature set {} called twice", getId(), new Throwable());
        }
        else {
            // This only starts the static initializer and updates the initialization state.
            // ! Is also makes sure that the feature set initialization phase hasn't been finalized yet.
            __base_ServerFeature.notFinalizedOrThrow("Attempting to initialize feature set \"" + getId() + "\" after feature set finalization");
            initialized = true;
        }
    }
}