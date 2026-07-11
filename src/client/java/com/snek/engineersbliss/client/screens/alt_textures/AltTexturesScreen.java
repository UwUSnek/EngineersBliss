package com.snek.engineersbliss.client.screens.alt_textures;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.EngineerSBlissClient;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;




public class AltTexturesScreen extends __base_Screen {
    private static UiWidgetList leftSidebar;
    private static final float LEFT_SIDEBAR_WIDTH = 0.25f;


    public AltTexturesScreen() {
        super();
    }




    @Override
    protected void init() {


        leftSidebar = new UiWidgetList((int)(width * LEFT_SIDEBAR_WIDTH), height, 0, 0, BUTTON_HEIGHT); {
            final String titleString = "Alternative Textures";
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString).getBold(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            // Visibility
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Visibility"), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.MINIMAL_REDSTONE_WIRE),         AltTextureFeature.MINIMAL_REDSTONE_WIRE       .getDetails(), b -> toggleFeature(AltTextureFeature.MINIMAL_REDSTONE_WIRE,        b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.NO_REDSTONE_DUST_PARTICLES),    AltTextureFeature.NO_REDSTONE_DUST_PARTICLES  .getDetails(), b -> toggleFeature(AltTextureFeature.NO_REDSTONE_DUST_PARTICLES,   b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.NO_CAMPFIRE_PARTICLES),         AltTextureFeature.NO_CAMPFIRE_PARTICLES       .getDetails(), b -> toggleFeature(AltTextureFeature.NO_CAMPFIRE_PARTICLES,        b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.NO_FIRE_PARTICLES),             AltTextureFeature.NO_FIRE_PARTICLES           .getDetails(), b -> toggleFeature(AltTextureFeature.NO_FIRE_PARTICLES,            b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.NO_LAVA_PARTICLES),             AltTextureFeature.NO_LAVA_PARTICLES           .getDetails(), b -> toggleFeature(AltTextureFeature.NO_LAVA_PARTICLES,            b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.NO_WATER_STREAM_PARTICLES),     AltTextureFeature.NO_WATER_STREAM_PARTICLES   .getDetails(), b -> toggleFeature(AltTextureFeature.NO_WATER_STREAM_PARTICLES,    b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.NO_DRIP_PARTICLES),             AltTextureFeature.NO_DRIP_PARTICLES           .getDetails(), b -> toggleFeature(AltTextureFeature.NO_DRIP_PARTICLES,            b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.TRANSPARENT_SLIME_BLOCK),       AltTextureFeature.TRANSPARENT_SLIME_BLOCK     .getDetails(), b -> toggleFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK,      b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.TRANSPARENT_HONEY_BLOCK),       AltTextureFeature.TRANSPARENT_HONEY_BLOCK     .getDetails(), b -> toggleFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK,      b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS),  AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS.getDetails(), b -> toggleFeature(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS, b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING),     AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING   .getDetails(), b -> toggleFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING,    b), '\0'), Layout.BORDER_HEIGHT);

            // Fixes
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Fixes & performance"), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.CONSISTENT_SLOPED_RAILS),      AltTextureFeature.CONSISTENT_SLOPED_RAILS     .getDetails(), b ->                toggleFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS,     b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(OverlaysScreen.getToggleText(OverlayFeature.BETTER_BARRIER_DISPLAY),          OverlayFeature.BETTER_BARRIER_DISPLAY         .getDetails(), b -> OverlaysScreen.toggleFeature(OverlayFeature.BETTER_BARRIER_DISPLAY,         b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(OverlaysScreen.getToggleText(OverlayFeature.BETTER_STRUCTURE_VOID_DISPLAY),   OverlayFeature.BETTER_STRUCTURE_VOID_DISPLAY  .getDetails(), b -> OverlaysScreen.toggleFeature(OverlayFeature.BETTER_STRUCTURE_VOID_DISPLAY,  b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(OverlaysScreen.getToggleText(OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY),      OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY     .getDetails(), b -> OverlaysScreen.toggleFeature(OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY,     b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.STATIC_CHESTS),                AltTextureFeature.STATIC_CHESTS               .getDetails(), b ->                toggleFeature(AltTextureFeature.STATIC_CHESTS,               b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.STATIC_SIGNS),                 AltTextureFeature.STATIC_SIGNS                .getDetails(), b ->                toggleFeature(AltTextureFeature.STATIC_SIGNS,                b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.STATIC_BANNERS),               AltTextureFeature.STATIC_BANNERS              .getDetails(), b ->                toggleFeature(AltTextureFeature.STATIC_BANNERS,              b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.STATIC_DECORATED_POTS),        AltTextureFeature.STATIC_DECORATED_POTS       .getDetails(), b ->                toggleFeature(AltTextureFeature.STATIC_DECORATED_POTS,       b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.STATIC_BELLS),                 AltTextureFeature.STATIC_BELLS                .getDetails(), b ->                toggleFeature(AltTextureFeature.STATIC_BELLS,                b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.STATIC_COPPER_GOLEM_STATUES),  AltTextureFeature.STATIC_COPPER_GOLEM_STATUES .getDetails(), b ->                toggleFeature(AltTextureFeature.STATIC_COPPER_GOLEM_STATUES, b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.STATIC_LECTERNS),              AltTextureFeature.STATIC_LECTERNS             .getDetails(), b ->                toggleFeature(AltTextureFeature.STATIC_LECTERNS,             b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.OPTIMIZED_SHELVES),            AltTextureFeature.OPTIMIZED_SHELVES           .getDetails(), b ->                toggleFeature(AltTextureFeature.OPTIMIZED_SHELVES,           b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(               getToggleText(AltTextureFeature.OPTIMIZED_CAMPFIRES),          AltTextureFeature.OPTIMIZED_CAMPFIRES         .getDetails(), b ->                toggleFeature(AltTextureFeature.OPTIMIZED_CAMPFIRES,         b), '\0'), Layout.BORDER_HEIGHT);

            // 3D models
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("3D models"), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.REDSTONE_WIRE_3D), AltTextureFeature.REDSTONE_WIRE_3D.getDetails(), b -> toggleFeature(AltTextureFeature.REDSTONE_WIRE_3D, b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.RAILS_3D),         AltTextureFeature.RAILS_3D        .getDetails(), b -> toggleFeature(AltTextureFeature.RAILS_3D,         b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.LADDERS_3D),       AltTextureFeature.LADDERS_3D      .getDetails(), b -> toggleFeature(AltTextureFeature.LADDERS_3D,       b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.CHAINS_3D),        AltTextureFeature.CHAINS_3D       .getDetails(), b -> toggleFeature(AltTextureFeature.CHAINS_3D,        b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.BARS_3D),          AltTextureFeature.BARS_3D         .getDetails(), b -> toggleFeature(AltTextureFeature.BARS_3D,          b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.VINES_3D),         AltTextureFeature.VINES_3D        .getDetails(), b -> toggleFeature(AltTextureFeature.VINES_3D,         b), '\0'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createButton(getToggleText(AltTextureFeature.GLOW_LICHEN_3D),   AltTextureFeature.GLOW_LICHEN_3D  .getDetails(), b -> toggleFeature(AltTextureFeature.GLOW_LICHEN_3D,   b), '\0'), Layout.BORDER_HEIGHT);
        }
        addRenderableWidget(leftSidebar);
    }




    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }





    public static Txt getToggleText(final AltTextureFeature feature, final boolean state) {
        return feature.getName().cat(": " + (state ? "ON" : "OFF"));
    }
    public static Txt getToggleText(final AltTextureFeature feature) {
        return getToggleText(feature, AltTexturesHandler.getFeature(feature));
    }


    public static void toggleFeature(final AltTextureFeature feature, final Button b) {
        final boolean newState = !AltTexturesHandler.getFeature(feature);
        b.setMessage(getToggleText(feature, newState).get());
        AltTexturesHandler.setFeature(feature, newState);
        MinecraftUtils.refreshSectionsContaining(feature.getAffectedBlocks());
    }
}