package com.snek.engineersbliss.feature_handlers.creative_tweaks;








public enum CreativeTweakFeature {


    PHASE_THROUGH_BLOCKS_FLY(
        true, "Phase through blocks",
        "Lets you phase through blocks while flying, completely ignoring their collisions.\n"+
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    PHASE_THROUGH_ENTITIES(
        true, "Phase through entities",
        "Stops you from pushing and being pushed by other entities, even while not flying.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_FIRE_EFFECT(
        true, "Disable being on fire",
        "Stops you from being on fire while standing in Fire, Soul Fire, Lava, Lava Cauldrons, Campfires and Soul Campfires.\n" +
        "This also disables the Fire overlay that is normally shown when the player is on fire.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_FREEZING_EFFECT(
        true, "Disable freezing",
        "Stops you from freezing while inside Powder Snow.\n" +
        "This also disables the Freezing overlay and the FOV decrease that are normally applied when the player is freezing.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_HONEY_JUMP(
        true, "Fix Honey Block jump",
        "Lets you jump while standing on Honey Blocks.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ), //TODO
    DISABLE_HONEY_SLIDING(
        true, "Disable Honey Block sliding",
        "Stops you from sticking to the sides of Honey Blocks while falling or jumping, effectively removing their custom sliding physics.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ), //TODO
    DISABLE_SLIME_BOUNCE(
        true, "Disable bouncing on Slime Blocks",
        "Stops you from bouncing on Slime Blocks.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_BED_BOUNCE(
        true, "Disable bouncing on beds",
        "Stops you from bouncing on Beds.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_ICE_SLIDING(
        true, "Disable sliding on ice",
        "Stops you from sliding on Frosted Ice, Ice, Packed Ice, and Blue Ice.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_CURRENT_DRAG(
        true, "Disable fluid current drag",
        "Stops you from being moved by Water and Lava currents.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_BUBBLE_COLUMN_DRAG(
        true, "Disable Bubble Column drag",
        "Stops you from being pushed up or pulled down while standing in Bubble Columns.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ), //TODO




    DISABLE_HONEY_SLOWDOWN(
        true, "Disable Honey Blocks slowdown",
        "Prevents you from being slowed down while walking on Honey Blocks.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_SLIME_SLOWDOWN(
        true, "Disable Slime Blocks slowdown",
        "Prevents you from being slowed down while walking on Slime Blocks.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_SOULSAND_SLOWDOWN(
        true, "Disable Soul Sand slowdown",
        "Prevents you from being slowed down while walking on Soul Sand.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_POWDER_SNOW_SLOWDOWN(
        true, "Disable Powder Snow slowdown",
        "Prevents you from being slowed down while walking through Powder Snow.\n" +
        "This also lets you jump freely while inside Powder Snow.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_WATER_SLOWDOWN(
        true, "Disable Water slowdown",
        "Prevents you from being slowed down while walking through Water.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_LAVA_SLOWDOWN(
        true, "Disable Lava slowdown",
        "Prevents you from being slowed down while walking through Lava.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_COBWEB_SLOWDOWN(
        true, "Disable Cobwebs slowdown",
        "Prevents you from being slowed down while walking through Cobwebs.\n" +
        "This also lets you jump freely while inside Cobwebs.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_LADDER_SLOWDOWN(
        true, "Disable Ladders slowdown",
        "Prevents you from being slowed down while walking near Ladders.\n" +
        "This doesn't let you jump while inside Ladders: Being a Climbable block, the jump button initiates the Climb action.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_VINES_SLOWDOWN(
        true, "Disable Vines slowdown",
        "Prevents you from being slowed down while walking near Vines.\n" +
        "This doesn't let you jump while inside Vines: Being a Climbable block, the jump button initiates the Climb action.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_TWISTING_VINES_SLOWDOWN(
        true, "Disable Twisting Vines slowdown",
        "Prevents you from being slowed down while walking through Twisting Vines.\n" +
        "This doesn't let you jump while inside Twisting Vines: Being a Climbable block, the jump button initiates the Climb action.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_WEEPING_VINES_SLOWDOWN(
        true, "Disable Weeping Vines slowdown",
        "Prevents you from being slowed down while walking through Weeping Vines.\n" +
        "This doesn't let you jump while inside Weeping Vines: Being a Climbable block, the jump button initiates the Climb action.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),
    DISABLE_SWEET_BERRIES_SLOWDOWN(
        true, "Disable Sweet Berry Bush slowdown",
        "Prevents you from being slowed down while walking through Sweet Berry Bushes.\n" +
        "This also lets you jump freely while inside Sweet Berry Bushes.\n" +
        Notices.CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE
    ),




    DISABLE_ITEM_CHANGE_ANIMATION(
        false, "Disable item change animation",
        "Disables the animation that plays when you select a different item or the item you are holding changes.\n" +
        Notices.CREATIVE_MODE_ENTITIES_PLAYERS_NOTICE
    ), //TODO
    DISABLE_HAND_SWING_ANIMATION(
        false, "Disable hand swing animation",
        "Disables the animation that plays when you interact with or attack blocks or entities, or use certain items.\n" +
        Notices.CREATIVE_MODE_ENTITIES_PLAYERS_NOTICE
    ), //TODO
    DISABLE_DIMENSION_CHANGE_SCREEN(
        true, "Disable dimension change screen",
        "Disables the loading screen that appears when changing dimensions.\n" +
        Notices.CREATIVE_MODE_ENTITIES_PLAYERS_NOTICE
    ), //TODO
    DISABLE_WATER_FOV_CHANGE(
        true, "Disable Water FOV",
        "Stops you from having reduced FOV while inside Water.\n" +
        Notices.CREATIVE_MODE_ENTITIES_PLAYERS_NOTICE
    ), //TODO
    DISABLE_WATER_OVERLAY(
        true, "Disable overlay effect of Water",
        "Disables the overlay and tint effects that appear while inside Water.\n" +
        Notices.CREATIVE_MODE_ENTITIES_PLAYERS_NOTICE
    ),
    DISABLE_LAVA_OVERLAY(
        true, "Disable overlay effect of Lava",
        "Disables the overlay and tint effects that appear while inside Lava.\n" +
        Notices.CREATIVE_MODE_ENTITIES_PLAYERS_NOTICE
    ), //TODO
    DISABLE_NETHER_PORTAL_OVERLAY(
        true, "Disable Nether Portal overlay",
        "Disables the overlay effect that appears while inside Nether Portal blocks.\n" +
        Notices.CREATIVE_MODE_ENTITIES_PLAYERS_NOTICE
    ); //TODO




    private class Notices {
        public static final String CREATIVE_MODE_ENTITIES_UNAFFECTED_NOTICE = "This only works while in Creative Mode and doesn't affect other entities.";
        public static final String CREATIVE_MODE_ENTITIES_PLAYERS_NOTICE    = "This only works while in Creative Mode and doesn't affect other players.";
    }








    // Name and properties
    private String name;
    private String details;
    private long flagBit; //! Flag bit index is calculated from the order of declaration
    private boolean _default;


    // Getters and checks
    public String getName() { return name; }
    public String getDetails() { return details; }
    public long getFlagBit() { return flagBit; }
    public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }


    public static long DEFAULT_FLAGS = 0;
    static {
        for(var feature : values()) {
            if(feature._default) DEFAULT_FLAGS |= feature.getFlagBit();
        }
    }


    private CreativeTweakFeature(final boolean _default, String name, String details) {
        this._default = _default;
        this.name = name;
        this.details = details;
        this.flagBit = 1 << ordinal();
    }
}
