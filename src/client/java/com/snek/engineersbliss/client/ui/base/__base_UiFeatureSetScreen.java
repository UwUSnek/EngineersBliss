package com.snek.engineersbliss.client.ui.base;

import java.time.Clock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.TextAlignmentY;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiButton;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.texture_atlases.TextureAtlasTracker;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;








/**
 * A special __base_UiScreen that can properly handle UiFeatureButton, UiSteppedFeatureSlider and UiAnalogueFeatureSlider elements.
 * It comes with left and a right sidebars and a feature previews.
 */
public abstract class __base_UiFeatureSetScreen extends __base_UiSidebarScreen {

    // Elements and layout
    protected static UiTextWidget descriptionTextWidget;
    protected static UiTextWidget descriptionNameWidget;
    public static final float DESCRIPTION_HEIGHT = 0.15f;
    public static final float DESCRIPTION_NAME_HEIGHT = 0.06f;
    public static final float DESCRIPTION_TEXT_HEIGHT = DESCRIPTION_HEIGHT - DESCRIPTION_NAME_HEIGHT;
    public static final float PREVIEW_WIDTH = 0.25f;
    private final float descriptionWidth;

    // Hover data cache
    public static final long HOVER_OFF_DELAY_MS = 250;
    private long lastHoverTime = 0;
    private Identifier[] hoveredPreviewAtlasIds = null;
    private UiButton lastHoveredButton = null;




    // Parent feature set and constructor
    protected final __base_ClientFeatureSet<?> featureSet;
    protected __base_UiFeatureSetScreen(final __base_ClientFeatureSet<?> featureSet) {
        super();
        this.descriptionWidth = 1f - leftSidebarWidth - rightSidebarWidth;
        this.featureSet = featureSet;
    }




    // Initializer function
    @Override
    protected void init() {
        super.init();


        // Add left sidebar title
        final UiTxt titleText = featureSet.calcName();
        leftSidebar.addWidget(new UiSpacer(Layout.BORDER_HEIGHT));
        leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleText.get(), 2f), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);


        // Add description name and text elements
        //! Preview is added dynamically
        final int descriptionWidthPx = (int)(width * descriptionWidth);
        final int descriptionX = (width - descriptionWidthPx) / 2;
        final int descriptionNameHeight = (int)(height * DESCRIPTION_NAME_HEIGHT);
        final int descriptionTextHeight = (int)(height * DESCRIPTION_TEXT_HEIGHT);
        descriptionNameWidget = new UiTextWidget(
            descriptionX, height - descriptionTextHeight - descriptionNameHeight, descriptionWidthPx, descriptionNameHeight,
            new UiTxt(), TextAlignment.CENTER, Layout.fgColor, true, Layout.bgColorSolid
        );
        descriptionTextWidget = new UiTextWidget(
            descriptionX, height - descriptionTextHeight, descriptionWidthPx, descriptionTextHeight,
            new UiTxt(), TextAlignment.CENTER, Layout.fgColor, true, Layout.bgColorSolid
        ).withVerticalAlignment(TextAlignmentY.TOP);
        addRenderableWidget(descriptionNameWidget);
        addRenderableWidget(descriptionTextWidget);
    }







    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if(tabPressed) return;


        // Find hovered entry
        final @Nullable UiWidgetList.Entry entry = leftSidebar.getHoveredEntry();
        final AbstractWidget widget = entry != null ? entry.getWidget() : null;


        // If hovering an element with feature preview
        if(widget instanceof UiFeatureButton button) {

            // Update hover timestamp
            // Update preview elements if the hovered element has changed
            lastHoverTime = Clock.systemDefaultZone().millis();
            if(button != lastHoveredButton) {
                updateToggleFeaturePreviewElements(button);
            }
        }

        // Try to clear the preview elements if not
        else {
            tryClearPreview();
        }


        // Draw immediate preview geometry if needed
        final float ratio = 9f / 4f;
        final int w = (int)(width * PREVIEW_WIDTH);
        final int h = (int)(w * ratio);
        final int hPlaceholder = w;
        final int xOff = (width  - w) / 2 - w / 2;
        final int xOn  = (width  - w) / 2 + w / 2;
        final int y    = (height - h) / 2;
        final int yPlaceholder = (height - hPlaceholder) / 2;
        if(hoveredPreviewAtlasIds != null && isPreviewOffOnCooldown()) {
            renderImmediateToggleFeaturePreview(graphics, w, h, hPlaceholder, xOff, xOn, y, yPlaceholder);
        }


        // Normal rendering
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }




    private void renderImmediateToggleFeaturePreview(GuiGraphicsExtractor graphics, final int w, final int h, final int hPlaceholder, final int xOff, final int xOn, final int y, final int yPlaceholder) {
        // Render ON/OFF text
        {
            final int descriptionWidthPx = (int)(width * descriptionWidth);
            final int descriptionX = (width - descriptionWidthPx) / 2;
            final int descriptionHeight = (int)(height * DESCRIPTION_HEIGHT);
            graphics.fill(descriptionX, 0, descriptionX + descriptionWidthPx, descriptionHeight, Layout.bgColorSolid);

            final int scale = 5;
            final @NotNull FontFamily fontFamily = Fonts.ui.bold;
            final @NotNull ScaledFont scaledFont = fontFamily.get(scale);
            final int textOffX = xOff + w / 2;
            final int textOnX  = xOn  + w / 2;
            final int textY    = (descriptionHeight - scaledFont.getLineHeight()) / 2;
            RenderingUtils.extractTxt(graphics, new UiTxt("OFF", fontFamily, scale), textOffX, textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
            RenderingUtils.extractTxt(graphics, new UiTxt("ON",  fontFamily, scale), textOnX,  textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
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




    private void updateToggleFeaturePreviewElements(UiFeatureButton button) {
        //TODO maybe draw one preview for each setting step? or something like that? idk yet

        // Draw feature preview
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

        // Update description name text
        final UiTxt descriptionName = new UiTxt(button.getClientFeature().calcName().get(), 2f);
        descriptionNameWidget.setLabel(descriptionName);
        descriptionNameWidget.setBgColor(Layout.bgColorSolid);

        // Update description text
        final UiTxt description = button.getClientFeature().calcDesc();
        descriptionTextWidget.setLabel(description);
        descriptionTextWidget.setBgColor(Layout.bgColorSolid);
    }




    private void tryClearPreview() {
        if(!isPreviewOffOnCooldown()) {
            hoveredPreviewAtlasIds = null;
            lastHoveredButton = null;
            descriptionNameWidget.setLabel(new UiTxt());
            descriptionNameWidget.setBgColor(0x0);
            descriptionTextWidget.setLabel(new UiTxt());
            descriptionTextWidget.setBgColor(0x0);
        }
    }



    private boolean isPreviewOffOnCooldown() {
        final long now = Clock.systemDefaultZone().millis();
        return now - lastHoverTime < HOVER_OFF_DELAY_MS;
    }
}
