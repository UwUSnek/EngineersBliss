package com.snek.engineersbliss.feature_handlers.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;








/**
 * The base class for all server-side mod features.
 */
public abstract class __base_ServerFeature<T> {



    // A callback called when the feature is changed by the client. Can be null.
    private final @Nullable BiConsumer<Player, T> afterChangeCallback;
    public @Nullable BiConsumer<Player, T> getAfterChangeCallback() { return afterChangeCallback; }




    // The feature set that defines this feature. This is set during feature registration.
    //! This works like a tree node's parent reference.
    private __base_ServerFeatureSet featureSet;
    public __base_ServerFeatureSet getFeatureSet() { return featureSet; }
    public void setFeatureSet(final __base_ServerFeatureSet featureSet) { this.featureSet = featureSet; }




    // A numerical ID for fast lookup and network packets.
    //! This MUST be a hash calculated from the string ID in order to avoid mismatches between minor mod versions.
    protected int hash = -1;
    private static final Map<Integer, __base_ServerFeature<?>> registered = new ConcurrentHashMap<>();
    public  static final Map<Integer, __base_ServerFeature<?>> getAllFeatures() { return registered; }
    protected static boolean initialized = false;

    /**
     * Must be called by the feature set whenever a new feature is registered.
     * @param feature The feature to register.
     * @param featureSet The feature set that is registering this feature.
     */
    public static <F extends __base_ServerFeature<?>> void onRegisterFeature(final F feature, final __base_ServerFeatureSet featureSet) {
        feature.setFeatureSet(featureSet);
        feature.hash = (featureSet.getId() + "." + feature.getId()).hashCode();
        registered.put(feature.hash, feature);
    }




    // The ID and default value of the feature
    protected final String id;
    protected final T defaultValue;


    // Getters
    public String getId() { return id; }
    public T getDefault() { return defaultValue; }
    public int getHash() { return hash; }




    protected __base_ServerFeature(final String id, final T defaultValue) {
        this(id, defaultValue, null);
    }
    protected __base_ServerFeature(final String id, final T defaultValue, final @Nullable BiConsumer<Player, T> afterChangeCallback) {
        this.id = id;
        this.defaultValue = defaultValue;
        this.afterChangeCallback = afterChangeCallback;
    }



//TODO remove
    // /**
    //  * Initializes the numerical IDs of all registered features.
    //  * ! This must be called during server side mod initialization, after calling onSetInit(set) on all feature sets.
    //  */
    // public static void finalizeSetInits() {
    //     if(initialized) {
    //         EngineerSBliss.LOGGER.error("__base_ServerFeature.finalizeSetInits called twice.", new Throwable());
    //     }
    //     else {
    //         initialized = true;
    //         registered.sort(Comparator.comparing(f -> f.id));
    //         for(int i = 0; i < registered.size(); i++) {
    //             registered.get(i).index = i;
    //         }
    //     }
    // }

//TODO remove
    // /**
    //  * Checks if the server feature set initialization phase has been finalized. Throws an exception if it hasn't.
    //  * @param message The message to display in the exception.
    //  */
    // public static void finalizedOrThrow(final String message) {
    //     if(!initialized) {
    //         throw new IllegalStateException(message);
    //     }
    // }

//TODO remove
    // /**
    //  * Checks if the server feature set initialization phase has been finalized. Throws an exception if it has.
    //  * @param message The message to display in the exception.
    //  */
    // public static void notFinalizedOrThrow(final String message) {
    //     if(initialized) {
    //         throw new IllegalStateException(message);
    //     }
    // }
}
