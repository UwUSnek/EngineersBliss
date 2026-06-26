package com.snek.engineersbliss.feature_handlers.creative_tweaks;



public enum CreativeTweakFeature {
    PHASE_THROUGH_BLOCKS         ("Phase through blocks while flying"), //TODO
    PHASE_THROUGH_ENTITIES       ("Phase through entities"), //TODO
    DISABLE_SLIME_BOUNCE         ("Disable bouncing on Slime Blocks"), //TODO
    DISABLE_HONEY_JUMP           ("Disable not being able to jump from Honey Blocks"), //TODO
    DISABLE_HONEY_SLIDING        ("Disable sliding on the sides of Honey Blocks"), //TODO
    DISABLE_SLIME_SLOWDOWN       ("Disable being slowed down by Slime Blocks"), //TODO
    DISABLE_HONEY_SLOWDOWN       ("Disable being slowed down by Honey Blocks"), //TODO
    DISABLE_SOULSAND_SLOWDOWN    ("Disable being slowed down by Soul Sand"), //TODO
    DISABLE_ICE_SLIDING          ("Disable sliding on Ice, Packed Ice and Blue Ice"), //TODO
    DISABLE_CURRENT_DRAG         ("Disable being moved by Water and Lava currents"), //TODO
    DISABLE_FIRE_EFFECT          ("Disable being on fire"), //TODO
    DISABLE_FREEZING_EFFECT      ("Disable freezing effect"), //TODO
    DISABLE_ITEM_CHANGE_ANIMATION("Disable item change animation"), //TODO
    DISABLE_HAND_SWING_ANIMATION ("Disable hand swing animation"); //TODO


    private String name;
    private long flagBit; //! Flag bit index is calculated from the order of declaration


    public String getName() { return name; }
    public long getFlagBit() { return flagBit; }
    public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }


    private CreativeTweakFeature(String name) {
        this.name = name;
        this.flagBit = 1 << ordinal();
    }
}
