package com.snek.engineersbliss.feature_handlers.overlays;

import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








public class OverlayServerFeatureSet extends __base_ServerFeatureSet {
    private OverlayServerFeatureSet(final String id, final String displayName) { super(id, displayName); }
    public static final OverlayServerFeatureSet INSTANCE = new OverlayServerFeatureSet("overlays", "Overlays");




    public static final ServerToggleFeature REDSTONE_WIRE_POWER_LEVELS    = INSTANCE.registerFeature(new ServerToggleFeature("redstone_wire_power_levels",    "Redstone Wire power levels",    true));
    public static final ServerToggleFeature RAIL_POWER_LEVELS             = INSTANCE.registerFeature(new ServerToggleFeature("rail_power_levels",             "Rail power levels",             true));
    public static final ServerToggleFeature COMPARATOR_POWER_LEVELS       = INSTANCE.registerFeature(new ServerToggleFeature("comparator_power_levels",       "Comparator power levels",       true));


    //TODO implement as custom arrows
    public static final ServerToggleFeature COMPARATOR_LOGIC_SNIPPET      = INSTANCE.registerFeature(new ServerToggleFeature("comparator_logic_snippet",      "Comparator logic snippet",      false));
    public static final ServerToggleFeature REDSTONE_WIRE_POWER_SOURCE    = INSTANCE.registerFeature(new ServerToggleFeature("redstone_wire_power_source",    "Redstone Wire power source",    false));
    public static final ServerToggleFeature RAIL_POWER_SOURCE             = INSTANCE.registerFeature(new ServerToggleFeature("rail_power_source",             "Rail power source",             false));


    public static final ServerToggleFeature BETTER_BARRIER_DISPLAY        = INSTANCE.registerFeature(new ServerToggleFeature("better_barrier_display",        "Better Barrier display",        true));
    public static final ServerToggleFeature BETTER_STRUCTURE_VOID_DISPLAY = INSTANCE.registerFeature(new ServerToggleFeature("better_structure_void_display", "Better Structure Void display", true));
    public static final ServerToggleFeature BETTER_LIGHT_BLOCK_DISPLAY    = INSTANCE.registerFeature(new ServerToggleFeature("better_light_block_display",    "Better Light Block display",    true));
}
