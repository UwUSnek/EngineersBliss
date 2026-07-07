package com.snek.engineersbliss.client.feature_handlers.alt_textures;




public class AltTexturesHandler {
    private AltTexturesHandler() {}
    private static long featureMask = AltTextureFeature.DEFAULT_FLAGS;




    public static void setFeature(final AltTextureFeature feature, final boolean value) {
        final long featureBit = feature.getFlagBit();
        if(value) featureMask |= featureBit; else featureMask &= ~featureBit;
    }

    public static boolean getFeature(final AltTextureFeature feature) {
        return feature.hasFlagBit(featureMask);
    }
}
