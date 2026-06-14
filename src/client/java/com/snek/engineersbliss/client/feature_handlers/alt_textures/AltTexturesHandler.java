package com.snek.engineersbliss.client.feature_handlers.alt_textures;


public class AltTexturesHandler {
    private AltTexturesHandler() {}


    private static boolean transparentSlimeBlock;
    private static boolean transparentHoneyBlock;
    private static boolean unobstructiveMangroveRoots;
    private static boolean unobstructiveScaffolding;
    private static boolean lineRedstoneDust;


    public static void init(
        final boolean defaultTransparentSlimeBlock,
        final boolean defaultTransparentHoneyBlock,
        final boolean defaultUnobstructiveMangroveRoots,
        final boolean defaultUnobstructiveScaffolding,
        final boolean defaultLineRedstoneDust
    ){
        transparentSlimeBlock      = defaultTransparentSlimeBlock;
        transparentHoneyBlock      = defaultTransparentHoneyBlock;
        unobstructiveMangroveRoots = defaultUnobstructiveMangroveRoots;
        unobstructiveScaffolding   = defaultUnobstructiveScaffolding;
        lineRedstoneDust           = defaultLineRedstoneDust;
    }


    public static void setTransparentSlimeBlock     (boolean v) { transparentSlimeBlock      = v; }
    public static void setTransparentHoneyBlock     (boolean v) { transparentHoneyBlock      = v; }
    public static void setUnobstructiveMangroveRoots(boolean v) { unobstructiveMangroveRoots = v; }
    public static void setUnobstructiveScaffolding  (boolean v) { unobstructiveScaffolding   = v; }
    public static void setLineRedstoneDust          (boolean v) { lineRedstoneDust           = v; }

    public static boolean getTransparentSlimeBlock     () { return transparentSlimeBlock;      }
    public static boolean getTransparentHoneyBlock     () { return transparentHoneyBlock;      }
    public static boolean getUnobstructiveMangroveRoots() { return unobstructiveMangroveRoots; }
    public static boolean getUnobstructiveScaffolding  () { return unobstructiveScaffolding;   }
    public static boolean getLineRedstoneDust          () { return lineRedstoneDust;           }
}
