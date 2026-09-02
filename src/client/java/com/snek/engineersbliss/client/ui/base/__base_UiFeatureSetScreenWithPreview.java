package com.snek.engineersbliss.client.ui.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.ui.UiGraphics;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.base.FeatureInputWidget;
import com.snek.engineersbliss.client.ui.widgets.base.DualPreviewFeatureInputWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.textures.atlases.TextureAtlasTracker;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;








/**
 * A __base_UiFeatureSetScreen that can display feature previews from UiToggleFeatureButton, UiSteppedFeatureSlider and UiAnalogueFeatureSlider elements.
 */
public abstract class __base_UiFeatureSetScreenWithPreview extends __base_UiFeatureSetScreen {

    // Preview data
    private Identifier[] hoveredPreviewAtlasIds = null;

    // Elements and layout
    public static final float PREVIEW_WIDTH = 0.25f;



    protected __base_UiFeatureSetScreenWithPreview(final __base_ClientFeatureSet<?> featureSet) {
        super(featureSet);
    }








    @Override
    protected void onFeatureHoverChange(final @Nullable FeatureInputWidget newWidget) {
        super.onFeatureHoverChange(newWidget);
        if(newWidget == null) {
            hoveredPreviewAtlasIds = null;
        }
        else switch(newWidget) {
            case DualPreviewFeatureInputWidget dpw -> updateToggleFeaturePreviewElements(dpw);
            default -> EngineerSBliss.LOGGER.error("Invalid feature preview widget type", new Throwable());
        }
    }




    @Override
    public void extractRenderState(UiGraphics graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        // Draw immediate feature preview elements if needed
        if(lastHoveredFeatureWidget != null && hoveredPreviewAtlasIds != null) {
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
        final int w = (int)(width * PREVIEW_WIDTH);
        final int h = (int)(w * ratio);
        final int hPlaceholder = w;
        final int xL = (width  - w) / 2 - w / 2;
        final int xR = (width  - w) / 2 + w / 2;
        final int y    = (height - h) / 2;
        final int yPlaceholder = (height - hPlaceholder) / 2;


        // Render background text
        {
            final int scale = 5;
            final @NotNull FontFamily fontFamily = Fonts.ui.bold;
            final @NotNull ScaledFont scaledFont = fontFamily.get(scale);
            final int textXL = xL + w / 2;
            final int textXR = xR + w / 2;
            final int textY    = ((int)(height * DESCRIPTION_HEIGHT) - scaledFont.getLineHeight()) / 2;
            final String textL = featureInputWidget.getLeftTitle();
            final String textR = featureInputWidget.getRightTitle();
            graphics.extractTxt(new UiTxt(textL, fontFamily, scale), textXL, textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
            graphics.extractTxt(new UiTxt(textR, fontFamily, scale), textXR, textY, Layout.fgColor, TextAlignment.CENTER_ANCHORED, 0);
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

        // Draw feature preview
        lastHoveredFeatureWidget = featureInputWidget;
        final @NotNull __base_ServerFeature<?> serverFeature = featureInputWidget.getServerFeature();
        final String featureSetId = serverFeature.getFeatureSet().getId();
        final String fatureId = serverFeature.getId();
        final String atlasPathL = String.format("textures/gui/feature_previews/%s/%s_%s_0.png", featureSetId, fatureId, featureInputWidget.getLeftPreviewSuffix()); //FIXME indices?
        final String atlasPathR = String.format("textures/gui/feature_previews/%s/%s_%s_0.png", featureSetId, fatureId, featureInputWidget.getRightPreviewSuffix()); //FIXME indices?
        hoveredPreviewAtlasIds = new Identifier[] {
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathL),
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathR)
        };
    }
}
