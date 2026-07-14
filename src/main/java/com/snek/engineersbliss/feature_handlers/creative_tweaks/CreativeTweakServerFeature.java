package com.snek.engineersbliss.feature_handlers.creative_tweaks;








public enum CreativeTweakServerFeature {

    //! Sliders are not toggle features
    NO_SIGN_GUI                    (false),
    OPEN_OBSTRUCTED_CONTAINERS     (false),

    PHASE_THROUGH_BLOCKS_FLY       (true),
    PHASE_THROUGH_ENTITIES         (false),
    DISABLE_FIRE_EFFECT            (true),
    DISABLE_FREEZING_EFFECT        (true),
    FIX_HONEY_JUMP             (true),
    DISABLE_HONEY_SLIDING          (true),
    DISABLE_SLIME_BOUNCE           (true),
    DISABLE_BED_BOUNCE             (true),
    DISABLE_ICE_SLIDING            (true),
    DISABLE_CURRENT_DRAG           (true),
    DISABLE_BUBBLE_COLUMN_DRAG     (true),

    DISABLE_HONEY_SLOWDOWN         (true),
    DISABLE_SLIME_SLOWDOWN         (true),
    DISABLE_SOULSAND_SLOWDOWN      (true),
    DISABLE_POWDER_SNOW_SLOWDOWN   (true),
    DISABLE_WATER_SLOWDOWN         (false),
    DISABLE_LAVA_SLOWDOWN          (false),
    DISABLE_COBWEB_SLOWDOWN        (true),
    DISABLE_LADDER_SLOWDOWN        (true),
    DISABLE_VINES_SLOWDOWN         (true),
    DISABLE_TWISTING_VINES_SLOWDOWN(true),
    DISABLE_WEEPING_VINES_SLOWDOWN (true),
    DISABLE_SWEET_BERRIES_SLOWDOWN (true),

    DISABLE_ITEM_CHANGE_ANIMATION  (false),
    DISABLE_HAND_SWING_ANIMATION   (false),
    DISABLE_DIMENSION_CHANGE_SCREEN(true),
    DISABLE_WATER_FOV_CHANGE       (true),
    DISABLE_WATER_OVERLAY          (true),
    DISABLE_LAVA_OVERLAY           (true),
    DISABLE_NETHER_PORTAL_OVERLAY  (true);




    // Name and properties
    private final long flagBit; //! Flag bit index is calculated from the order of declaration
    private final boolean _default;


    // Getters and checks
    public long getFlagBit() { return flagBit; }
    public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }


    public static long DEFAULT_FLAGS = 0;
    static {
        for(final var feature : values()) {
            if(feature._default) DEFAULT_FLAGS |= feature.getFlagBit();
        }
    }


    private CreativeTweakServerFeature(final boolean _default) {
        this._default = _default;
        this.flagBit = 1 << ordinal();
    }
}
