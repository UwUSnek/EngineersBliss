package com.snek.engineersbliss.feature_handlers.overlays;

import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








public class OverlaysServerFeatureSet extends __base_ServerFeatureSet {
    public static final OverlaysServerFeatureSet INSTANCE = new OverlaysServerFeatureSet();
    private OverlaysServerFeatureSet() { super("overlays"); }




    public static final ServerToggleFeature REDSTONE_WIRE_POWER_LEVELS    = INSTANCE.registerFeature(new ServerToggleFeature("redstone_wire_power_levels",    true));
    public static final ServerToggleFeature RAIL_POWER_LEVELS             = INSTANCE.registerFeature(new ServerToggleFeature("rail_power_levels",             true));
    public static final ServerToggleFeature COMPARATOR_POWER_LEVELS       = INSTANCE.registerFeature(new ServerToggleFeature("comparator_power_levels",       true));

    //TODO implement as custom arrows
    public static final ServerToggleFeature COMPARATOR_LOGIC_SNIPPET      = INSTANCE.registerFeature(new ServerToggleFeature("comparator_logic_snippet",      false));
    public static final ServerToggleFeature REDSTONE_WIRE_POWER_SOURCE    = INSTANCE.registerFeature(new ServerToggleFeature("redstone_wire_power_source",    false));
    public static final ServerToggleFeature RAIL_POWER_SOURCE             = INSTANCE.registerFeature(new ServerToggleFeature("rail_power_source",             false));

    public static final ServerToggleFeature BETTER_BARRIER_DISPLAY        = INSTANCE.registerFeature(new ServerToggleFeature("better_barrier_display",        true));
    public static final ServerToggleFeature BETTER_STRUCTURE_VOID_DISPLAY = INSTANCE.registerFeature(new ServerToggleFeature("better_structure_void_display", true));
    public static final ServerToggleFeature BETTER_LIGHT_BLOCK_DISPLAY    = INSTANCE.registerFeature(new ServerToggleFeature("better_light_block_display",    true));
}
