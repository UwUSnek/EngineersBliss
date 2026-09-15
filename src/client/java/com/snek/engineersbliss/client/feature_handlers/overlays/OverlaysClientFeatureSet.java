package com.snek.engineersbliss.client.feature_handlers.overlays;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.overlays.OverlaysServerFeatureSet;

import java.util.function.Supplier;








@SuppressWarnings("java:S1905")
public class OverlaysClientFeatureSet extends __base_ClientFeatureSet<OverlaysServerFeatureSet> {
    public static final OverlaysClientFeatureSet INSTANCE = new OverlaysClientFeatureSet();
    private OverlaysClientFeatureSet() {
        super(OverlaysServerFeatureSet.INSTANCE, () -> new UiTxt("Overlays"));
    }




    public static final ClientFeature<?> REDSTONE_WIRE_POWER_LEVELS = new ClientFeature<>(
        OverlaysServerFeatureSet.REDSTONE_WIRE_POWER_LEVELS,
        s(() -> new UiTxt("Redstone Wire power levels")),
        s(() -> new UiTxt("Displays the power level of powered Redstone Wires."))
    );
    public static final ClientFeature<?> RAIL_POWER_LEVELS = new ClientFeature<>(
        OverlaysServerFeatureSet.RAIL_POWER_LEVELS,
        s(() -> new UiTxt("Rail power levels")),
        s(() -> new UiTxt("Displays the power level of powered Activator Rails and Powered Rails.")),
        s(() -> new UiTxt("This follows Minecraft Vanilla's quirky rail update logic, so the displayed power levels might at times seem counterintuitive.")),
        Notices.MULTIPLAYER_NOTICE
    );
    public static final ClientFeature<?> COMPARATOR_POWER_LEVELS = new ClientFeature<>(
        OverlaysServerFeatureSet.COMPARATOR_POWER_LEVELS,
        s(() -> new UiTxt("Comparator power levels")),
        s(() -> new UiTxt("Displays the output power level of Comparators.")),
        s(() -> new UiTxt("Unlike other power level overlays, this is also shown on Comparators with output 0.")),
        Notices.MULTIPLAYER_NOTICE

    );




    public static final ClientFeature<?> COMPARATOR_LOGIC_SNIPPET = new ClientFeature<>(
        OverlaysServerFeatureSet.COMPARATOR_LOGIC_SNIPPET,
        s(() -> new UiTxt("Comparator logic snippet")),
        s(() -> new UiTxt("Displays the logic Comparators use to calculate their output signal as an expression.")),
        Notices.MULTIPLAYER_NOTICE
    ); //TODO implement these as custom arrows
    public static final ClientFeature<?> REDSTONE_WIRE_POWER_SOURCE = new ClientFeature<>(
        OverlaysServerFeatureSet.REDSTONE_WIRE_POWER_SOURCE,
        s(() -> new UiTxt("Redstone Wire power source")),
        s(() -> new UiTxt("Shows arrows connecting each Redstone Wire to the blocks that are currently powering it.")),
        Notices.MULTIPLAYER_NOTICE
    ); //TODO implement these as custom arrows
    public static final ClientFeature<?> RAIL_POWER_SOURCE = new ClientFeature<>(
        OverlaysServerFeatureSet.RAIL_POWER_SOURCE,
        s(() -> new UiTxt("Rail power source")),
        s(() -> new UiTxt("Shows arrows connecting each Activator Rail and Powered Rail to the block that is currently powering it.")),
        Notices.MULTIPLAYER_NOTICE
    ); //TODO implement these as custom arrows




    public static final ClientFeature<?> BETTER_BARRIER_DISPLAY = new ClientFeature<>(
        OverlaysServerFeatureSet.BETTER_BARRIER_DISPLAY,
        s(() -> new UiTxt("Better Barrier display")),
        s(() -> new UiTxt("Removes the Vanilla Barrier particles that spawn when holding a Barrier item, replacing them with a proper overlay.")),
        Notices.OVERLAY_PROS_NOTICE
    );
    public static final ClientFeature<?> BETTER_STRUCTURE_VOID_DISPLAY = new ClientFeature<>(
        OverlaysServerFeatureSet.BETTER_STRUCTURE_VOID_DISPLAY,
        s(() -> new UiTxt("Better Structure Void display")),
        s(() -> new UiTxt("Displays placed Structure Void blocks while holding a Structure Void item, similarly to how Barriers work in Minecraft Vanilla")),
        s(() -> new UiTxt("but instead of particles, this uses a proper overlay.")),
        Notices.OVERLAY_PROS_NOTICE
    );
    public static final ClientFeature<?> BETTER_LIGHT_BLOCK_DISPLAY = new ClientFeature<>(
        OverlaysServerFeatureSet.BETTER_LIGHT_BLOCK_DISPLAY,
        s(() -> new UiTxt("Better Light Block display")),
        s(() -> new UiTxt("Removes the Vanilla Light particles that spawn when holding a Light item, replacing them with a proper overlay.")),
        Notices.OVERLAY_PROS_NOTICE
    );








    private class Notices {
        public static final Supplier<UiTxt> OVERLAY_PROS_NOTICE = () -> (UiTxt)new UiTxt(
            "Overlays are shown and removed instantly, don't have a view distance limit and can be seen through walls."
        ).green();

        public static final Supplier<UiTxt> MULTIPLAYER_NOTICE = () -> (UiTxt)new UiTxt(
            "This overlay isn't available on servers without the " + EngineerSBliss.MOD_NAME + " mod installed."
        ).red();
    }
}
