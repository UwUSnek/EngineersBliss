package com.snek.engineersbliss.feature_handlers.creative_tweaks;

import java.util.List;

import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








public class CreativeTweaksServerFeatureSet extends __base_ServerFeatureSet {
    public static CreativeTweaksServerFeatureSet INSTANCE = new CreativeTweaksServerFeatureSet();
    private CreativeTweaksServerFeatureSet() { super("creative_tweaks"); }




    public static ServerSteppedFeature<Float> FLYING_SPEED = INSTANCE.registerFeature(new ServerSteppedFeature<Float>(
        "flying_speed",
        List.of(0.05f, 0.125f, 0.25f, 0.5f, 1f, 2f, 4f, 8f, 16f, 32f, 64f), 4
        //! Flying speed is fully client side
    ));
    public static ServerSteppedFeature<Float> WALKING_SPEED = INSTANCE.registerFeature(new ServerSteppedFeature<Float>(
        "walking_speed",
        List.of(0.05f, 0.125f, 0.25f, 0.5f, 1f, 2f, 4f, 8f, 16f, 32f, 64f), 4,
        CreativeTweaksServerHandler::updateWalkingSpeed
    ));
    public static ServerSteppedFeature<Float> INTERACTION_DISTANCE = INSTANCE.registerFeature(new ServerSteppedFeature<Float>(
        "reach_distance",
        List.of(5f, 8f, 16f, 32f, 64f, 128f, 256f, 8192f), 0,
        CreativeTweaksServerHandler::updateInteractionDistance
    ));
    public static ServerSteppedFeature<Integer> INTERACTION_RADIUS = INSTANCE.registerFeature(new ServerSteppedFeature<Integer>(
        "interaction_radius",
        List.of(1, 2, 3, 4, 5, 10, 20, 50), 0,
        CreativeTweaksServerHandler::updateInteractionRadius
    ));
    public static ServerToggleFeature NO_SIGN_GUI = INSTANCE.registerFeature(new ServerToggleFeature(
        "no_sign_gui",
        false
    ));
    public static ServerToggleFeature OPEN_OBSTRUCTED_CONTAINERS = INSTANCE.registerFeature(new ServerToggleFeature(
        "open_obstructed_containers",
        false
    ));




    public static final ServerToggleFeature PHASE_THROUGH_BLOCKS_FLY        = INSTANCE.registerFeature(new ServerToggleFeature("phase_through_blocks_fly",        true));
    public static final ServerToggleFeature PHASE_THROUGH_ENTITIES          = INSTANCE.registerFeature(new ServerToggleFeature("phase_through_entities",          false));
    public static final ServerToggleFeature DISABLE_FIRE_EFFECT             = INSTANCE.registerFeature(new ServerToggleFeature("disable_fire_effect",             true));
    public static final ServerToggleFeature DISABLE_FREEZING_EFFECT         = INSTANCE.registerFeature(new ServerToggleFeature("disable_freezing_effect",         true));
    public static final ServerToggleFeature FIX_HONEY_JUMP                  = INSTANCE.registerFeature(new ServerToggleFeature("fix_honey_jump",                  true));
    public static final ServerToggleFeature DISABLE_HONEY_SLIDING           = INSTANCE.registerFeature(new ServerToggleFeature("disable_honey_sliding",           true));
    public static final ServerToggleFeature DISABLE_SLIME_BOUNCE            = INSTANCE.registerFeature(new ServerToggleFeature("disable_slime_bounce",            true));
    public static final ServerToggleFeature DISABLE_BED_BOUNCE              = INSTANCE.registerFeature(new ServerToggleFeature("disable_bed_bounce",              true));
    public static final ServerToggleFeature DISABLE_ICE_SLIDING             = INSTANCE.registerFeature(new ServerToggleFeature("disable_ice_sliding",             true));
    public static final ServerToggleFeature DISABLE_CURRENT_DRAG            = INSTANCE.registerFeature(new ServerToggleFeature("disable_current_drag",            true));
    public static final ServerToggleFeature DISABLE_BUBBLE_COLUMN_DRAG      = INSTANCE.registerFeature(new ServerToggleFeature("disable_bubble_column_drag",      true));

    public static final ServerToggleFeature DISABLE_HONEY_SLOWDOWN          = INSTANCE.registerFeature(new ServerToggleFeature("disable_honey_slowdown",          true));
    public static final ServerToggleFeature DISABLE_SLIME_SLOWDOWN          = INSTANCE.registerFeature(new ServerToggleFeature("disable_slime_slowdown",          true));
    public static final ServerToggleFeature DISABLE_SOULSAND_SLOWDOWN       = INSTANCE.registerFeature(new ServerToggleFeature("disable_soulsand_slowdown",       true));
    public static final ServerToggleFeature DISABLE_POWDER_SNOW_SLOWDOWN    = INSTANCE.registerFeature(new ServerToggleFeature("disable_powder_snow_slowdown",    true));
    public static final ServerToggleFeature DISABLE_WATER_SLOWDOWN          = INSTANCE.registerFeature(new ServerToggleFeature("disable_water_slowdown",          false));
    public static final ServerToggleFeature DISABLE_LAVA_SLOWDOWN           = INSTANCE.registerFeature(new ServerToggleFeature("disable_lava_slowdown",           false));
    public static final ServerToggleFeature DISABLE_COBWEB_SLOWDOWN         = INSTANCE.registerFeature(new ServerToggleFeature("disable_cobweb_slowdown",         true));
    public static final ServerToggleFeature DISABLE_LADDER_SLOWDOWN         = INSTANCE.registerFeature(new ServerToggleFeature("disable_ladder_slowdown",         true));
    public static final ServerToggleFeature DISABLE_VINES_SLOWDOWN          = INSTANCE.registerFeature(new ServerToggleFeature("disable_vines_slowdown",          true));
    public static final ServerToggleFeature DISABLE_TWISTING_VINES_SLOWDOWN = INSTANCE.registerFeature(new ServerToggleFeature("disable_twisting_vines_slowdown", true));
    public static final ServerToggleFeature DISABLE_WEEPING_VINES_SLOWDOWN  = INSTANCE.registerFeature(new ServerToggleFeature("disable_weeping_vines_slowdown",  true));
    public static final ServerToggleFeature DISABLE_SWEET_BERRIES_SLOWDOWN  = INSTANCE.registerFeature(new ServerToggleFeature("disable_sweet_berries_slowdown",  true));

    public static final ServerToggleFeature DISABLE_ITEM_CHANGE_ANIMATION   = INSTANCE.registerFeature(new ServerToggleFeature("disable_item_change_animation",   false));
    public static final ServerToggleFeature DISABLE_HAND_SWING_ANIMATION    = INSTANCE.registerFeature(new ServerToggleFeature("disable_hand_swing_animation",    false));
    public static final ServerToggleFeature DISABLE_DIMENSION_CHANGE_SCREEN = INSTANCE.registerFeature(new ServerToggleFeature("disable_dimension_change_screen", true));
    public static final ServerToggleFeature DISABLE_WATER_FOV_CHANGE        = INSTANCE.registerFeature(new ServerToggleFeature("disable_water_fov_change",        true));
    public static final ServerToggleFeature DISABLE_WATER_OVERLAY           = INSTANCE.registerFeature(new ServerToggleFeature("disable_water_overlay",           true));
    public static final ServerToggleFeature DISABLE_LAVA_OVERLAY            = INSTANCE.registerFeature(new ServerToggleFeature("disable_lava_overlay",            true));
    public static final ServerToggleFeature DISABLE_NETHER_PORTAL_OVERLAY   = INSTANCE.registerFeature(new ServerToggleFeature("disable_nether_portal_overlay",   true));
}
