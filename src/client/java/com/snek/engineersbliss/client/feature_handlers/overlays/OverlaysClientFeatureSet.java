package com.snek.engineersbliss.client.feature_handlers.overlays;

import com.snek.engineersbliss.utils.Txt;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.base.ClientBlockFeature;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.overlays.OverlaysServerFeatureSet;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.level.block.Blocks;








public class OverlaysClientFeatureSet extends __base_ClientFeatureSet<OverlaysServerFeatureSet> {
    public static final OverlaysClientFeatureSet INSTANCE = new OverlaysClientFeatureSet();
    private OverlaysClientFeatureSet() {
        super(OverlaysServerFeatureSet.INSTANCE, () -> new UiTxt("Overlays"));
    }




    public static final ClientBlockFeature<?> REDSTONE_WIRE_POWER_LEVELS = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.REDSTONE_WIRE_POWER_LEVELS,
        () -> new UiTxt("Redstone Wire power levels"),
        () -> new UiTxt("Displays the power level of powered Redstone Wires."),
        List.of(Blocks.REDSTONE_WIRE)
    );
    public static final ClientBlockFeature<?> RAIL_POWER_LEVELS = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.RAIL_POWER_LEVELS,
        () -> new UiTxt("Rail power levels"),
        () -> new Txt()
            .cat(new UiTxt("Displays the power level of powered Activator Rails and Powered Rails.\n"))
            .cat(new UiTxt("This follows Minecraft Vanilla's quirky rail update logic, so the displayed power levels might at times seem counterintuitive.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE.get())
        ,
        List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)
    );
    public static final ClientBlockFeature<?> COMPARATOR_POWER_LEVELS = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.COMPARATOR_POWER_LEVELS,
        () -> new UiTxt("Comparator power levels"),
        () -> new Txt()
            .cat(new UiTxt("Displays the output power level of Comparators.\n"))
            .cat(new UiTxt("Unlike other power level overlays, this is also shown on Comparators with output 0.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE.get())
        ,
        List.of(Blocks.COMPARATOR)
    );




    public static final ClientBlockFeature<?> COMPARATOR_LOGIC_SNIPPET = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.COMPARATOR_LOGIC_SNIPPET,
        () -> new UiTxt("Comparator logic snippet"),
        () -> new Txt()
            .cat(new UiTxt("Displays the logic Comparators use to calculate their output signal as an expression.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE.get())
        ,
        List.of(Blocks.COMPARATOR)
    ); //TODO implement these as custom arrows
    public static final ClientBlockFeature<?> REDSTONE_WIRE_POWER_SOURCE = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.REDSTONE_WIRE_POWER_SOURCE,
        () -> new UiTxt("Redstone Wire power source"),
        () -> new Txt()
            .cat(new UiTxt("Shows arrows connecting each Redstone Wire to the blocks that are currently powering it.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE.get())
        ,
        List.of(Blocks.REDSTONE_WIRE)
    ); //TODO implement these as custom arrows
    public static final ClientBlockFeature<?> RAIL_POWER_SOURCE = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.RAIL_POWER_SOURCE,
        () -> new UiTxt("Rail power source"),
        () -> new Txt()
            .cat(new UiTxt("Shows arrows connecting each Activator Rail and Powered Rail to the block that is currently powering it.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE.get())
        ,
        List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)
    ); //TODO implement these as custom arrows




    public static final ClientBlockFeature<?> BETTER_BARRIER_DISPLAY = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.BETTER_BARRIER_DISPLAY,
        () -> new UiTxt("Better Barrier display"),
        () -> new Txt()
            .cat(new UiTxt("Removes the Vanilla Barrier particles that spawn when holding a Barrier item, replacing them with a proper overlay.\n"))
            .cat(Notices.OVERLAY_PROS_NOTICE.get())
        ,
        List.of(Blocks.BARRIER)
    );
    public static final ClientBlockFeature<?> BETTER_STRUCTURE_VOID_DISPLAY = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.BETTER_STRUCTURE_VOID_DISPLAY,
        () -> new UiTxt("Better Structure Void display"),
        () -> new Txt()
            .cat(new UiTxt("Displays placed Structure Void blocks while holding a Structure Void item, similarly to how Barriers work in Minecraft Vanilla, "))
            .cat(new UiTxt("but instead of particles, this uses a proper overlay.\n"))
            .cat(Notices.OVERLAY_PROS_NOTICE.get())
        ,
        List.of(Blocks.STRUCTURE_VOID)
    );
    public static final ClientBlockFeature<?> BETTER_LIGHT_BLOCK_DISPLAY = new ClientBlockFeature<>(
        OverlaysServerFeatureSet.BETTER_LIGHT_BLOCK_DISPLAY,
        () -> new UiTxt("Better Light Block display"),
        () -> new Txt()
            .cat(new UiTxt("Removes the Vanilla Light particles that spawn when holding a Light item, replacing them with a proper overlay.\n"))
            .cat(Notices.OVERLAY_PROS_NOTICE.get())
        ,
        List.of(Blocks.LIGHT)
    );




    private class Notices {
        public static final Supplier<Txt> OVERLAY_PROS_NOTICE = () -> new UiTxt(
            "Overlays are shown and removed instantly, don't have a view distance limit and can be seen through walls."
        ).green();

        public static final Supplier<Txt> MULTIPLAYER_NOTICE = () -> new UiTxt(
            "This overlay isn't available on servers without the " + EngineerSBliss.MOD_NAME + " mod installed."
        ).red();
    }
}
