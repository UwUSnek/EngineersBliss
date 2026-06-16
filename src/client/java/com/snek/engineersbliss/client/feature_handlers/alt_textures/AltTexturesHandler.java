package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.EnumMap;
import java.util.Map;




public class AltTexturesHandler {
    private AltTexturesHandler() {}


    private static Map<AltTextureFeature, Boolean> features = new EnumMap<>(AltTextureFeature.class);


    public static void init(){
        for(AltTextureFeature feature : AltTextureFeature.values()) {
            features.put(feature, true);
        }
    }


    public static void setFeature(final AltTextureFeature feature, boolean value) {
        features.put(feature, value);
    }

    public static boolean getFeature(final AltTextureFeature feature) {
        return features.get(feature);
    }
}
