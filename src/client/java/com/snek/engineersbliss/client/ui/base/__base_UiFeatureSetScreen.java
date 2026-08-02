package com.snek.engineersbliss.client.ui.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.TextAlignmentY;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.base.FeatureInputWidget;
import com.snek.engineersbliss.client.ui.widgets.base.DualPreviewFeatureInputWidget;
import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.textures.atlases.TextureAtlasTracker;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;








/**
 * A special __base_UiScreen that can properly handle UiToggleFeatureButton, UiSteppedFeatureSlider and UiAnalogueFeatureSlider elements.
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
    private FeatureInputWidget lastHoveredFeatureWidget = null;




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
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt(titleText.get(), 2f), TextAlignment.LEFT, Layout.fgColor), titleText.getScaledFont().getLineHeight());


        // Add description name and text elements
        //! Preview is added dynamically
        final int descriptionWidthPx = (int)(width * descriptionWidth);
        final int descriptionX = (width - descriptionWidthPx) / 2;
        final int descriptionNameHeight = (int)(height * DESCRIPTION_NAME_HEIGHT);
        final int descriptionTextHeight = (int)(height * DESCRIPTION_TEXT_HEIGHT);
        descriptionNameWidget = new UiTextWidget(
            this,
            descriptionX, height - descriptionTextHeight - descriptionNameHeight, descriptionWidthPx, descriptionNameHeight,
            new UiTxt(), TextAlignment.CENTER, Layout.fgColor, true, Layout.bgColor
        );
        descriptionTextWidget = new UiTextWidget(
            this,
            descriptionX, height - descriptionTextHeight, descriptionWidthPx, descriptionTextHeight,
            new UiTxt(), TextAlignment.CENTER, Layout.fgColor, true, Layout.bgColor
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
        if(widget instanceof FeatureInputWidget featureWidget) {

            // Update hover timestamp
            // Update preview elements if the hovered element has changed
            lastHoverTime = System.currentTimeMillis();
            if(featureWidget != lastHoveredFeatureWidget) {
                switch(featureWidget) {
                    case DualPreviewFeatureInputWidget dpw -> updateToggleFeaturePreviewElements(dpw);
                    default -> EngineerSBliss.LOGGER.error("Invalid feature preview widget type", new Throwable());
                }
            }

            // Draw immediate preview geometry if needed
            final float ratio = 9f / 4f;
            final int w = (int)(width * PREVIEW_WIDTH);
            final int h = (int)(w * ratio);
            final int hPlaceholder = w;
            final int xL = (width  - w) / 2 - w / 2;
            final int xR = (width  - w) / 2 + w / 2;
            final int y    = (height - h) / 2;
            final int yPlaceholder = (height - hPlaceholder) / 2;
            if(hoveredPreviewAtlasIds != null && isPreviewOffOnCooldown()) {
                switch(featureWidget) {
                    case DualPreviewFeatureInputWidget dpw -> renderImmediateToggleFeaturePreview(graphics, dpw, w, h, hPlaceholder, xL, xR, y, yPlaceholder);
                    default -> EngineerSBliss.LOGGER.error("Invalid feature preview widget type", new Throwable());
                }
            }
        }

        // Try to clear the preview elements if not
        else {
            tryClearPreview();
        }


        // Normal rendering
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }




    private void renderImmediateToggleFeaturePreview(GuiGraphicsExtractor graphics, final DualPreviewFeatureInputWidget featureInputWidget, final int w, final int h, final int hPlaceholder, final int xL, final int xR, final int y, final int yPlaceholder) {
        // Render background text
        {
            final int descriptionWidthPx = (int)(width * descriptionWidth);
            final int descriptionX = (width - descriptionWidthPx) / 2;
            final int descriptionHeight = (int)(height * DESCRIPTION_HEIGHT);
            graphics.fill(descriptionX, 0, descriptionX + descriptionWidthPx, descriptionHeight, Layout.bgColor);

            final int scale = 5;
            final @NotNull FontFamily fontFamily = Fonts.ui.bold;
            final @NotNull ScaledFont scaledFont = fontFamily.get(scale);
            final int textXL = xL + w / 2;
            final int textXR = xR + w / 2;
            final int textY    = (descriptionHeight - scaledFont.getLineHeight()) / 2;
            final String textL = featureInputWidget.getLeftTitle();
            final String textR = featureInputWidget.getRightTitle();
            RenderingUtils.extractTxt(graphics, new UiTxt(textL, fontFamily, scale), textXL, textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
            RenderingUtils.extractTxt(graphics, new UiTxt(textR, fontFamily, scale), textXR, textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
            //FIXME replace on/off text with something from the interface
        }


        // Render the feature preview
        {
            final Identifier atlasIdL = hoveredPreviewAtlasIds[0];
            final Identifier atlasIdR = hoveredPreviewAtlasIds[1];
            if(!TextureAtlasTracker.isTextureReady(atlasIdL)) {
                graphics.blit(atlasIdL, xL, yPlaceholder, xL + w, yPlaceholder + hPlaceholder, 0f, 1f, 0f, 1f);
            }
            else {
                final float[] uv = TextureAtlasTracker.getUV(atlasIdL, 0, System.currentTimeMillis());
                graphics.blit(atlasIdL, xL, y, xL + w, y + h, uv[0], uv[1], uv[2], uv[3]);
            }
            if(!TextureAtlasTracker.isTextureReady(atlasIdR)) {
                graphics.blit(atlasIdR,  xR, yPlaceholder, xR + w, yPlaceholder + hPlaceholder, 0f, 1f, 0f, 1f);
            }
            else {
                final float[] uv  = TextureAtlasTracker.getUV(atlasIdR,  0, System.currentTimeMillis());
                graphics.blit(atlasIdR,  xR, y, xR + w, y + h, uv[0], uv[1], uv[2], uv[3]);
            }
        }
    }




    private void updateToggleFeaturePreviewElements(DualPreviewFeatureInputWidget featureInputWidget) {
        //TODO maybe draw one preview for each setting step? or something like that? idk yet

        // Draw feature preview
        lastHoveredFeatureWidget = featureInputWidget;
        final __base_ServerFeature<?> serverFeature = featureInputWidget.getServerFeature();
        final String featureSetId = serverFeature.getFeatureSet().getId();
        final String fatureId = serverFeature.getId();
        final String atlasPathL = String.format("textures/gui/feature_previews/%s/%s_%s_0.png", featureSetId, featureInputWidget.getLeftPreviewSuffix(), fatureId); //FIXME indices?
        final String atlasPathR = String.format("textures/gui/feature_previews/%s/%s_%s_0.png", featureSetId, featureInputWidget.getRightPreviewSuffix(), fatureId); //FIXME indices?
        hoveredPreviewAtlasIds = new Identifier[] {
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathL),
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathR)
        };

        // Update description name text
        final UiTxt descriptionName = new UiTxt(featureInputWidget.getClientFeature().calcName().get(), 2f);
        descriptionNameWidget.setLabel(descriptionName);
        descriptionNameWidget.setBgColor(Layout.bgColor);

        // Update description text
        final UiTxt description = featureInputWidget.getClientFeature().calcDesc();
        descriptionTextWidget.setLabel(description);
        descriptionTextWidget.setBgColor(Layout.bgColor);
    }




    private void tryClearPreview() {
        if(!isPreviewOffOnCooldown()) {
            hoveredPreviewAtlasIds = null;
            lastHoveredFeatureWidget = null;
            descriptionNameWidget.setLabel(new UiTxt());
            descriptionNameWidget.setBgColor(0x0);
            descriptionTextWidget.setLabel(new UiTxt());
            descriptionTextWidget.setBgColor(0x0);
        }
    }



    private boolean isPreviewOffOnCooldown() {
        final long now = System.currentTimeMillis();
        return now - lastHoverTime < HOVER_OFF_DELAY_MS;
    }
}
