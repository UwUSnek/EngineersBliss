package com.snek.engineersbliss.feature_handlers.creative_tweaks;




public enum CreativeTweakFeature {
    PHASE_THROUGH_BLOCKS_FLY       (true,  "Phase through blocks while flying"),
    PHASE_THROUGH_ENTITIES         (true,  "Phase through entities"),
    DISABLE_FIRE_EFFECT            (true,  "Disable being on fire"),
    DISABLE_FREEZING_EFFECT        (true,  "Disable freezing effect"),
    DISABLE_HONEY_JUMP             (true,  "Disable not being able to jump from Honey Blocks"), //TODO
    DISABLE_HONEY_SLIDING          (true,  "Disable sliding on the sides of Honey Blocks"), //TODO
    DISABLE_HONEY_SLOWDOWN         (true,  "Disable being slowed down by Honey Blocks"),
    DISABLE_SLIME_BOUNCE           (true,  "Disable bouncing on Slime Blocks"),
    DISABLE_BED_BOUNCE             (true,  "Disable bouncing on beds"),
    DISABLE_ICE_SLIDING            (true,  "Disable sliding on Ice, Packed Ice and Blue Ice"),
    DISABLE_CURRENT_DRAG           (true,  "Disable being moved by Water and Lava currents"),
    DISABLE_BUBBLE_COLUMN_DRAG     (true,  "Disable being pushed or pulled by Bubble Columns"), //TODO

    DISABLE_SLIME_SLOWDOWN         (true,  "Disable being slowed down by Slime Blocks"),
    DISABLE_SOULSAND_SLOWDOWN      (true,  "Disable being slowed down by Soul Sand"),
    DISABLE_POWDER_SNOW_SLOWDOWN   (true,  "Disable being slowed down by Powder Snow"),
    DISABLE_WATER_SLOWDOWN         (true,  "Disable being slowed down by Water"),
    DISABLE_LAVA_SLOWDOWN          (true,  "Disable being slowed down by Lava"),
    DISABLE_COBWEB_SLOWDOWN        (true,  "Disable being slowed down by Cobwebs"),
    DISABLE_LADDER_SLOWDOWN        (true,  "Disable being slowed down by Ladders"),
    DISABLE_VINES_SLOWDOWN         (true,  "Disable being slowed down by Vines"),
    DISABLE_TWISTING_VINES_SLOWDOWN(true,  "Disable being slowed down by Twisting Vines"),
    DISABLE_WEEPING_VINES_SLOWDOWN (true,  "Disable being slowed down by Weeping Vines"),
    DISABLE_SWEET_BERRIES_SLOWDOWN (true,  "Disable being slowed down by Sweet Berries"),

    DISABLE_ITEM_CHANGE_ANIMATION  (false, "Disable item change animation"), //TODO
    DISABLE_HAND_SWING_ANIMATION   (false, "Disable hand swing animation"), //TODO
    DISABLE_NETHER_PORTAL_OVERLAY  (true,  "Disable loading screen and overlay effect of Nether Portals"), //TODO
    DISABLE_WATER_FOV_CHANGE       (true,  "Disable reduced FOV when inside a body of Water"), //TODO
    DISABLE_WATER_OVERLAY          (true,  "Disable overlay effect of Water"),
    DISABLE_LAVA_OVERLAY           (true,  "Disable overlay effect of Lava"); //TODO





    // Name and properties
    private String name;
    private long flagBit; //! Flag bit index is calculated from the order of declaration
    private boolean _default;


    // Getters and checks
    public String getName() { return name; }
    public long getFlagBit() { return flagBit; }
    public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }


    public static long DEFAULT_FLAGS = 0;
    static {
        for(var feature : values()) {
            if(feature._default) DEFAULT_FLAGS |= feature.getFlagBit();
        }
    }


    private CreativeTweakFeature(final boolean _default, String name) {
        this._default = _default;
        this.name = name;
        this.flagBit = 1 << ordinal();
    }
}
