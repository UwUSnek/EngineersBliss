package com.snek.engineersbliss.feature_handlers.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.snek.engineersbliss.EngineerSBliss;








/**
 * The base class for all server-side mod features.
 */
public abstract class __base_ServerFeature<T> {


    // The feature set that defines this feature. This is set during feature registration.
    //! This works like a tree node's parent parameter.
    private __base_ServerFeatureSet featureSet;
    public __base_ServerFeatureSet getFeatureSet() { return featureSet; }
    public void setFeatureSet(final __base_ServerFeatureSet featureSet) { this.featureSet = featureSet; }




    // A numerical ID for fast lookup and network packets.
    //! The index is calculated automatically on server initialization.
    //! This is deterministic, so the indices calculated by the server always match the ones calculated by the clients
    //! (as long as they are running the same version of the mod), as each client feature requires a corresponding server feature.
    protected int index = -1;
    private static final List<__base_ServerFeature<?>> registered = new ArrayList<>();
    public static final List<__base_ServerFeature<?>> getAllFeatures() { return registered; }
    protected static boolean initialized = false;

    /**
     * Must be called by the feature set whenever a new feature is registered.
     * @param feature The feature to register.
     * @param featureSet The feature set that is registering this feature.
     */
    public static <F extends __base_ServerFeature<?>> void onRegisterFeature(final F feature, final __base_ServerFeatureSet featureSet) {
        feature.setFeatureSet(featureSet);
        registered.add(feature);
    }




    // The ID and default value of the feature
    protected final String id;
    protected final T defaultValue;


    // Getters
    public String getId() { return id; }
    public T getDefault() { return defaultValue; }
    public int getIndex() { return index; }


    protected __base_ServerFeature(final String id, final T defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }




    /**
     * Initializes the numerical IDs of all registered features.
     * ! This must be called during server side mod initialization, after calling onSetInit(set) on all feature sets.
     */
    public static void finalizeSetInits(){
        if(initialized) {
            EngineerSBliss.LOGGER.error("__base_ServerFeature.finalizeSetInits called twice.", new Throwable());
        }
        else {
            initialized = true;
            registered.sort(Comparator.comparing(f -> f.id));
            for (int i = 0; i < registered.size(); i++) {
                registered.get(i).index = i;
            }
        }
    }
}
