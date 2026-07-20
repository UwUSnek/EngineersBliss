package com.snek.engineersbliss.client.screens.base;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiButton;
import com.snek.engineersbliss.client.screens.parts.UiFeatureButton;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.texture_atlases.TextureAtlasTracker;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;








/**
 * A special __base_UiScreen that can properly handle UiFeatureButton, UiSteppedFeatureSlider and UiAnalogueFeatureSlider elements.
 * It comes with left and a right sidebars and a feature previews.
 */
public abstract class __base_UiFeatureSetScreen extends __base_UiScreen {

    // Elements and layout
    protected static UiWidgetList leftSidebar;
    protected static UiWidgetList rightSidebar;
    protected static UiTextWidget descriptionWidget;
    public static final float LEFT_SIDEBAR_WIDTH = 0.25f;
    public static final float RIGHT_SIDEBAR_WIDTH = 0.25f;
    public static final float DESCRIPTION_WIDTH = 1f - LEFT_SIDEBAR_WIDTH - RIGHT_SIDEBAR_WIDTH - 0.01f;
    public static final float DESCRIPTION_HEIGHT = 0.2f;
    public static final float PREVIEW_WIDTH = 0.25f;

    // Hover data cache
    private static Identifier[] hoveredPreviewAtlasIds = null;
    private static UiButton lastHoveredButton = null;




    // Parent feature set and constructor
    protected __base_UiFeatureSetScreen() {
        super();
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


        final int descriptionWidth = (int)(width * DESCRIPTION_WIDTH);
        final int descriptionHeight = (int)(height * DESCRIPTION_HEIGHT);
        final int descriptionX = (width - descriptionWidth) / 2;
        descriptionWidget = new UiTextWidget(
            descriptionX, height - descriptionHeight, descriptionWidth, descriptionHeight,
            new Txt(), TextAlignment.LEFT, Layout.fgColor, true
        );
        addRenderableWidget(descriptionWidget);
    }








    // Rendering
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {


        // Handle tab
        if(tabPressed) return;




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
            descriptionWidget.setLabel(new Txt());
        }
        else {
            final AbstractWidget widget = entry.getWidget();
            //TODO maybe draw one preview for each setting step? or something like that? idk yet
            if(widget instanceof UiFeatureButton button) {
                if(button != lastHoveredButton) {
                    lastHoveredButton = button;
                    final __base_ServerFeature<?> serverFeature = button.getServerFeature();
                    final String featureSetId = serverFeature.getFeatureSet().getId();
                    final String fatureId = serverFeature.getId();
                    final String atlasPathOff = String.format("textures/gui/feature_previews/%s/%s_off_0.png", featureSetId, fatureId); //FIXME indices?
                    final String atlasPathOn  = String.format("textures/gui/feature_previews/%s/%s_on_0.png",  featureSetId, fatureId); //FIXME indices?
                    hoveredPreviewAtlasIds = new Identifier[] {
                        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathOff),
                        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathOn)
                    };

                    // Update description text
                    final Txt description = button.getClientFeature().calcDesc();
                    descriptionWidget.setLabel(description);
                }


                // Render ON/OFF text
                {
                    final int scale = 5;
                    final int textOffX = xOff + w / 2;
                    final int textOnX  = xOn  + w / 2;
                    final int textY    = minecraft.font.lineHeight * scale;
                    RenderingUtils.extractTxt(graphics, new UiTxt("OFF", scale).withBoldFont(), textOffX, textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
                    RenderingUtils.extractTxt(graphics, new UiTxt("ON",  scale).withBoldFont(), textOnX,  textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
                }


                // Render the feature preview
                {
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


        // Normal element rendering
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }
}
