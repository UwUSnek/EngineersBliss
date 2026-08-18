package com.snek.engineersbliss.feature_handlers.overlays;

import java.util.List;

import com.snek.engineersbliss.feature_handlers.base.ServerBlockToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;

import net.minecraft.world.level.block.Blocks;








public class OverlaysServerFeatureSet extends __base_ServerFeatureSet {
    public static final OverlaysServerFeatureSet INSTANCE = new OverlaysServerFeatureSet();
    private OverlaysServerFeatureSet() { super("overlays"); }




    public static final ServerBlockToggleFeature REDSTONE_WIRE_POWER_LEVELS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "redstone_wire_power_levels", true,
        List.of(Blocks.REDSTONE_WIRE)
    ));
    public static final ServerBlockToggleFeature RAIL_POWER_LEVELS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "rail_power_levels", true,
        List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)
    ));
    public static final ServerBlockToggleFeature COMPARATOR_POWER_LEVELS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "comparator_power_levels", true,
        List.of(Blocks.COMPARATOR)
    ));




    //TODO implement as custom arrows
    public static final ServerBlockToggleFeature COMPARATOR_LOGIC_SNIPPET = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "comparator_logic_snippet", false,
        List.of(Blocks.COMPARATOR)
    ));
    public static final ServerBlockToggleFeature REDSTONE_WIRE_POWER_SOURCE = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "redstone_wire_power_source", false,
        List.of(Blocks.REDSTONE_WIRE)
    ));
    public static final ServerBlockToggleFeature RAIL_POWER_SOURCE = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "rail_power_source", false,
        List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)
    ));




    public static final ServerBlockToggleFeature BETTER_BARRIER_DISPLAY = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "better_barrier_display", true,
        List.of(Blocks.BARRIER)
    ));
    public static final ServerBlockToggleFeature BETTER_STRUCTURE_VOID_DISPLAY = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "better_structure_void_display", true,
        List.of(Blocks.STRUCTURE_VOID)
    ));
    public static final ServerBlockToggleFeature BETTER_LIGHT_BLOCK_DISPLAY = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "better_light_block_display", true,
        List.of(Blocks.LIGHT)
    ));
}
