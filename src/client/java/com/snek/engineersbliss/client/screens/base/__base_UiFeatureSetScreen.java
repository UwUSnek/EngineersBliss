package com.snek.engineersbliss.client.screens.base;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.screens.parts.UiButton;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.texture_atlases.TextureAtlasTracker;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.utils.Txt;

import net.caffeinemc.mods.sodium.client.gui.widgets.AbstractWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.resources.Identifier;




public abstract class __base_UiFeatureSetScreen<S extends __base_ClientFeatureSet<?>> extends __base_UiScreen {

    // Elements and layout
    protected static UiWidgetList leftSidebar;
    protected static UiWidgetList rightSidebar;
    public static final float LEFT_SIDEBAR_WIDTH = 0.25f;
    public static final float RIGHT_SIDEBAR_WIDTH = 0.25f;
    public static final float PREVIEW_WIDTH = 0.25f;

    // Hover data cache
    private static Identifier[] hoveredPreviewAtlasIds = null;
    private static UiButton lastHoveredButton = null;




    // Parent feature set and constructor
    protected final S featureSet;
    protected __base_UiFeatureSetScreen(S featureSet) {
        super();
        this.featureSet = featureSet;
    }




    // Initializer function
    @Override
    protected void init() {
        super.init();

        leftSidebar = new UiWidgetList((int)(width * LEFT_SIDEBAR_WIDTH), height, 0, 0, BUTTON_HEIGHT);
        addRenderableWidget(leftSidebar);

        final int rightSidebarWidth = (int)(width * RIGHT_SIDEBAR_WIDTH);
        rightSidebar = new UiWidgetList(rightSidebarWidth, height, width - rightSidebarWidth, 0, BUTTON_HEIGHT);
        addRenderableWidget(rightSidebar);
    }


    public static UiButton createCreativeTweakFeatureButton(final CreativeTweakFeature feature, final @Nullable String spriteName) {
        return createButton(
            getToggleText(feature),
            feature.getDetails(),
            b -> toggleFeature(feature, b),
            '\0',
            "creative_tweaks/" + spriteName,
            feature.name().toLowerCase()
        );
    }








    // Rendering
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        // Handle tab and normal element rendering
        if(tabPressed) return;
        super.extractRenderState(graphics, mouseX, mouseY, a);


        // Calculate feature preview position
        final float ratio = 9f / 4f; //! Vertical 4:9 for 1080x480 (1920/4) resolution
        final int w = (int)(width * PREVIEW_WIDTH);
        final int h = (int)(w * ratio);
        final int hPlaceholder = w;
        final int xOff = (width  - w) / 2 - w / 2 ;
        final int xOn  = (width  - w) / 2 + w / 2 ;
        final int y    = (height - h) / 2;
        final int yPlaceholder = (height - hPlaceholder) / 2;


        // Find the hovered feature and calculate the remaining preview data
        final @Nullable UiWidgetList.Entry entry = leftSidebar.getHoveredEntry();
        if(entry == null) {
            hoveredPreviewAtlasIds = null;
            lastHoveredButton = null;
        }
        else {
            final AbstractWidget widget = entry.getWidget();
            if(widget instanceof UiButton button) {
                if(button != lastHoveredButton) {
                    lastHoveredButton = button;
                    final String featureSetId = featureSet.getServerSet().getId();
                    final String fatureId = button.getFeatureId();
                    final String atlasPathOff = String.format("textures/gui/feature_previews/%s/%s_off_0.png", featureSetId, fatureId); //FIXME indices
                    final String atlasPathOn  = String.format("textures/gui/feature_previews/%s/%s_on_0.png",  featureSetId, fatureId); //FIXME indices
                    hoveredPreviewAtlasIds = new Identifier[] {
                        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathOff),
                        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathOn)
                    };
                }


        //TODO name of the feature at the top. also ON/OFF
        //TODO description at the bottom
                // Render the feature preview
                final Identifier atlasIdOff = hoveredPreviewAtlasIds[0];
                final Identifier atlasIdOn  = hoveredPreviewAtlasIds[1];
                if(!TextureAtlasTracker.isTextureReady(atlasIdOff)) {
                    graphics.blit(atlasIdOff, xOff, yPlaceholder, xOff + w, yPlaceholder + hPlaceholder, 0f, 1f, 0f, 1f);
                }
                else {
                    final float[] uvOff = TextureAtlasTracker.getUV(atlasIdOff, 0, System.currentTimeMillis());
                    graphics.blit(atlasIdOff, xOff, y, xOff + w, y + h, uvOff[0], uvOff[1], uvOff[2], uvOff[3]);
                }
                if(!TextureAtlasTracker.isTextureReady(atlasIdOn)) {
                    graphics.blit(atlasIdOn,  xOn, yPlaceholder, xOn + w, yPlaceholder + hPlaceholder, 0f, 1f, 0f, 1f);
                }
                else {
                    final float[] uvOn  = TextureAtlasTracker.getUV(atlasIdOn,  0, System.currentTimeMillis());
                    graphics.blit(atlasIdOn,  xOn, y, xOn + w, y + h, uvOn[0], uvOn[1], uvOn[2], uvOn[3]);
                }
            }
        }
    }








    /**
     * Creates a Txt with format "<feature_name>: [ON/OFF]" based on the provided state.
     * @param feature The toggle feature.
     * @param state The state to display. True for ON, false for OFF.
     * @return The created Txt.
     */
    public static Txt getToggleText(final ClientFeature<? extends ServerToggleFeature> feature, final boolean state) {
        return feature.calcName().cat(": " + (state ? "ON" : "OFF"));
    }


    /**
     * Creates a Txt with format "<feature_name>: [ON/OFF]" based on the current client-side state of the specified feature.
     * @param feature The toggle feature.
     * @return The created Txt.
     */
    public static Txt getToggleText(final ClientFeature<? extends ServerToggleFeature> feature) {
        final __base_ServerFeature serverFeature = feature.getServerFeature();
        if(serverFeature instanceof ServerToggleFeature stf) {
            return getToggleText(feature, ClientFeatureSync.getFeatureB(stf));
        }
        else {
            EngineerSBliss.LOGGER.error("getToggleText called on a server feature of non-toggle type: {}", serverFeature.getId(), new Throwable());
            return new Txt();
        }
    }


    /**
     * Toggles a toggle feature and its button.
     * @param feature The toggle feature to toggle.
     * @param b
     */
    //FIXME remove and move all the logic to UiToggleFeatureButton
    //FIXME add a custom UiSteppedFeatureSlider
    //FIXME add a custom UiAnalogueFeatureSlider
    public static void toggleFeature(final ClientFeature<?> feature, final Button b) {
        final boolean newState = !CreativeTweaksClientHandler.clientPlayerHasFeature(Minecraft.getInstance().player, feature);
        b.setMessage(getToggleText(feature, newState).get());
        CreativeTweaksClientHandler.setFeature(feature, newState);
    }
}
