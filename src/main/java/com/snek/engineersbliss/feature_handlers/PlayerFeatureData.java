package com.snek.engineersbliss.feature_handlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
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
                final Object validatedValue = validateFeatureValueForVersion(feature, feature.getDefault()); //TODO read value from configs, fallback to default if not present
                values.put(feature.getHash(), validatedValue);
            }
        }
    }
    /**
     * Makes sure the provided feature value is valid in the current Minecraft version.
     * @param feature The feature.
     * @param value The feature value to validate.
     * @return If the provided value is valid, it is returned unaltered. Otherwise, a value that is guaranteed to be valid is returned.
     */
    private static Object validateFeatureValueForVersion(final __base_ServerFeature<?> feature, final Object value) {
        //? if >=26.2 {
            if(feature == AltTexturesServerFeatureSet.STATIC_BEDS) {
                return Boolean.FALSE;
            }
        //? }
        return value;
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
