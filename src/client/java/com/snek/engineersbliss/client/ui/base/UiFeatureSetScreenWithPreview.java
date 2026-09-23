package com.snek.engineersbliss.client.ui.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.FeatureInputWidget;
import com.snek.engineersbliss.client.ui.widgets.base.DualPreviewFeatureInputWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;

import net.minecraft.resources.Identifier;







//TODO remove "loading" image
//TODO might still be needed for other things, idk if it should be fully removed or not. it does look pretty.

/**
 * A UiFeatureSetScreen that can display feature previews from UiToggleFeatureButton, UiSteppedFeatureSlider and UiAnalogueFeatureSlider elements.
 */
public abstract class UiFeatureSetScreenWithPreview extends UiFeatureSetScreen {

    // Preview data
    private Identifier[] hoveredPreviewVideoIds = null;

    // Elements and layout
    public static final float PREVIEW_WIDTH = 0.25f;



    protected UiFeatureSetScreenWithPreview(final __base_ClientFeatureSet<?> featureSet) {
        super(featureSet);
    }








    @Override
    protected void onFeatureHoverChange(final @Nullable FeatureInputWidget newWidget) {
        super.onFeatureHoverChange(newWidget);
        if(newWidget == null) {
            hoveredPreviewVideoIds = null;
        }
        else switch(newWidget) {
            case DualPreviewFeatureInputWidget dpw -> updateToggleFeaturePreviewElements(dpw);
            default -> EngineerSBliss.LOGGER.error("Invalid feature preview widget type", new Throwable());
        }
    }




    @Override
    public void extractRenderState(UiGraphics graphics, float mouseX, float mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        // Draw immediate feature preview elements if needed
        if(lastHoveredFeatureWidget != null && hoveredPreviewVideoIds != null) {
            switch(lastHoveredFeatureWidget) {
                case DualPreviewFeatureInputWidget dpw -> renderImmediateToggleFeaturePreview(graphics, dpw);
                default -> EngineerSBliss.LOGGER.error("Invalid feature preview widget type", new Throwable());
            }
        }
    }




    private static final float DESCRIPTION_HEIGHT = 0.25f;
    private void renderImmediateToggleFeaturePreview(UiGraphics graphics, final DualPreviewFeatureInputWidget featureInputWidget) {

        // Calculate data
        final float ratio = 9f / 4f;
        final float w = width * PREVIEW_WIDTH;
        final float h = w * ratio;
        final float xL = (width  - w) / 2 - w / 2;
        final float xR = (width  - w) / 2 + w / 2;
        final float y  = (height - h) / 2;


        // Render background text
        {
            final float scale = 10;
            final @NotNull FontFamily fontFamily = Fonts.ui.invariant_bold;
            final @NotNull ScaledFont scaledFont = fontFamily.get(scale);
            final int textXL = (int)(xL + w / 2);
            final int textXR = (int)(xR + w / 2);
            final int textY    = ((int)(height * DESCRIPTION_HEIGHT) - scaledFont.getLineHeight()) / 2;
            final String textL = featureInputWidget.getLeftTitle();
            final String textR = featureInputWidget.getRightTitle();
            graphics.text.draw(new UiTxt(textL, fontFamily, scale), textXL, textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
            graphics.text.draw(new UiTxt(textR, fontFamily, scale), textXR, textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
        }


        // Render the feature preview
        {
            final @NotNull Identifier videoIdL = hoveredPreviewVideoIds[0];
            final @NotNull Identifier videoIdR = hoveredPreviewVideoIds[1];
            graphics.blit.wh(videoIdL, xL, y, w, h);
            graphics.blit.wh(videoIdR, xR, y, w, h);
        }
    }




    private void updateToggleFeaturePreviewElements(DualPreviewFeatureInputWidget featureInputWidget) {

        // Draw feature preview
        lastHoveredFeatureWidget = featureInputWidget;
        final @NotNull __base_ServerFeature<?> serverFeature = featureInputWidget.getServerFeature();
        final String featureSetId = serverFeature.getFeatureSet().getId();
        final String fatureId = serverFeature.getId();
        final String videoPathL = String.format("gui/feature_previews/%s/%s_%s", featureSetId, fatureId, featureInputWidget.getLeftPreviewSuffix());
        final String videoPathR = String.format("gui/feature_previews/%s/%s_%s", featureSetId, fatureId, featureInputWidget.getRightPreviewSuffix());
        hoveredPreviewVideoIds = new Identifier[] {
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, videoPathL),
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, videoPathR)
        };
    }
}
