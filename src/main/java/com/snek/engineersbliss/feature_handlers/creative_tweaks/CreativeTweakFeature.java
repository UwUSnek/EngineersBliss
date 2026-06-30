package com.snek.engineersbliss.feature_handlers.creative_tweaks;




public enum CreativeTweakFeature {
    PHASE_THROUGH_BLOCKS_FLY       ("Phase through blocks while flying"),
    PHASE_THROUGH_ENTITIES         ("Phase through entities"),
    DISABLE_FIRE_EFFECT            ("Disable being on fire"),
    DISABLE_FREEZING_EFFECT        ("Disable freezing effect"),
    DISABLE_HONEY_JUMP             ("Disable not being able to jump from Honey Blocks"), //TODO
    DISABLE_HONEY_SLIDING          ("Disable sliding on the sides of Honey Blocks"), //TODO
    DISABLE_HONEY_SLOWDOWN         ("Disable being slowed down by Honey Blocks"),
    DISABLE_SLIME_BOUNCE           ("Disable bouncing on Slime Blocks"),
    DISABLE_BED_BOUNCE             ("Disable bouncing on beds"), //TODO
    DISABLE_ICE_SLIDING            ("Disable sliding on Ice, Packed Ice and Blue Ice"),
    DISABLE_CURRENT_DRAG           ("Disable being moved by Water and Lava currents"),
    DISABLE_BUBBLE_COLUMN_DRAG     ("Disable being pushed or pulled by Bubble Columns"), //TODO

    DISABLE_SLIME_SLOWDOWN         ("Disable being slowed down by Slime Blocks"),
    DISABLE_SOULSAND_SLOWDOWN      ("Disable being slowed down by Soul Sand"),
    DISABLE_POWDER_SNOW_SLOWDOWN   ("Disable being slowed down by Powder Snow"),
    DISABLE_WATER_SLOWDOWN         ("Disable being slowed down by Water"),
    DISABLE_LAVA_SLOWDOWN          ("Disable being slowed down by Lava"),
    DISABLE_COBWEB_SLOWDOWN        ("Disable being slowed down by Cobwebs"), //TODO
    DISABLE_LADDER_SLOWDOWN        ("Disable being slowed down by Ladders"), //TODO
    DISABLE_VINES_SLOWDOWN         ("Disable being slowed down by Vines"), //TODO
    DISABLE_TWISTING_VINES_SLOWDOWN("Disable being slowed down by Twisting Vines"), //TODO
    DISABLE_WEEPING_VINES_SLOWDOWN ("Disable being slowed down by Weeping Vines"), //TODO
    DISABLE_SWEET_BERRIES_SLOWDOWN ("Disable being slowed down by Sweet Berries"), //TODO

    DISABLE_ITEM_CHANGE_ANIMATION  ("Disable item change animation"), //TODO
    DISABLE_HAND_SWING_ANIMATION   ("Disable hand swing animation"), //TODO
    DISABLE_NETHER_PORTAL_OVERLAY  ("Disable loading screen and overlay effect of Nether Portals"), //TODO
    DISABLE_WATER_FOV_CHANGE       ("Disable reduced FOV when inside a body of Water"), //TODO
    DISABLE_WATER_OVERLAY          ("Disable overlay effect of Water"),
    DISABLE_LAVA_OVERLAY           ("Disable overlay effect of Lava"); //TODO


    private String name;
    private long flagBit; //! Flag bit index is calculated from the order of declaration


    public String getName() { return name; }
    public long getFlagBit() { return flagBit; }
    public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }


    private CreativeTweakFeature(String name) {
        this.name = name;
        this.flagBit = 1 << ordinal();
    }


    public static long DEFAULT_FLAGS = 0
    |   CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY       .getFlagBit()
    |   CreativeTweakFeature.PHASE_THROUGH_ENTITIES         .getFlagBit()
    |   CreativeTweakFeature.DISABLE_FIRE_EFFECT            .getFlagBit()
    |   CreativeTweakFeature.DISABLE_FREEZING_EFFECT        .getFlagBit()
    |   CreativeTweakFeature.DISABLE_HONEY_JUMP             .getFlagBit()
    |   CreativeTweakFeature.DISABLE_HONEY_SLIDING          .getFlagBit()
    |   CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN         .getFlagBit()
    |   CreativeTweakFeature.DISABLE_SLIME_BOUNCE           .getFlagBit()
    |   CreativeTweakFeature.DISABLE_BED_BOUNCE             .getFlagBit()
    |   CreativeTweakFeature.DISABLE_ICE_SLIDING            .getFlagBit()
    |   CreativeTweakFeature.DISABLE_CURRENT_DRAG           .getFlagBit()

    |   CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN         .getFlagBit()
    |   CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN      .getFlagBit()
    |   CreativeTweakFeature.DISABLE_POWDER_SNOW_SLOWDOWN   .getFlagBit()
    |   CreativeTweakFeature.DISABLE_COBWEB_SLOWDOWN        .getFlagBit()
    |   CreativeTweakFeature.DISABLE_LADDER_SLOWDOWN        .getFlagBit()
    |   CreativeTweakFeature.DISABLE_VINES_SLOWDOWN         .getFlagBit()
    |   CreativeTweakFeature.DISABLE_TWISTING_VINES_SLOWDOWN.getFlagBit()
    |   CreativeTweakFeature.DISABLE_WEEPING_VINES_SLOWDOWN .getFlagBit()
    |   CreativeTweakFeature.DISABLE_SWEET_BERRIES_SLOWDOWN .getFlagBit()
    |   CreativeTweakFeature.DISABLE_WATER_SLOWDOWN         .getFlagBit()
    |   CreativeTweakFeature.DISABLE_LAVA_SLOWDOWN          .getFlagBit()

    |   CreativeTweakFeature.DISABLE_ITEM_CHANGE_ANIMATION  .getFlagBit() &0
    |   CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION   .getFlagBit() &0
    |   CreativeTweakFeature.DISABLE_NETHER_PORTAL_OVERLAY  .getFlagBit()
    |   CreativeTweakFeature.DISABLE_WATER_FOV_CHANGE       .getFlagBit()
    |   CreativeTweakFeature.DISABLE_WATER_OVERLAY          .getFlagBit()
    |   CreativeTweakFeature.DISABLE_LAVA_OVERLAY           .getFlagBit()
    ;
}
