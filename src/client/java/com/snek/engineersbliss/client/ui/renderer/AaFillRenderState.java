package com.snek.engineersbliss.client.ui.renderer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;




/**
 * A GuiElementRenderState for antialiased rects.
 * This is used by UiGraphics to produce antialiased regions without multiple draw calls, improving performance.
 * Supports 4-vertex gradients.
 */
public record AaFillRenderState(
    RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose,
    float x0, float y0, float x1, float y1,
    int color0, int color1, int color2, int color3,
    @Nullable ScreenRectangle scissorArea
) implements GuiElementRenderState {

    @Override
    public void buildVertices(VertexConsumer vc) {
        final float ex0 = x0 - 1;
        final float ey0 = y0 - 1;
        final float ex1 = x1 + 1;
        final float ey1 = y1 + 1;
        final float w = x1 - x0;
        final float h = y1 - y0;
        emit(vc, ex1, ey0, color0, ex1 - x0, ey0 - y0, w, h);
        emit(vc, ex1, ey1, color1, ex1 - x0, ey1 - y0, w, h);
        emit(vc, ex0, ey1, color2, ex0 - x0, ey1 - y0, w, h);
        emit(vc, ex0, ey0, color3, ex0 - x0, ey0 - y0, w, h);
    }


    private void emit(VertexConsumer vc, float x, float y, int color, float localX, float localY, float w, float h) {

        //! Name      Type  Norm  Count
        // POSITION   FLOAT false   3   |  xy needed. z holds X position     |  1x float
        // LINE_WIDTH FLOAT false   1   |  Holds Y position                  |  1x float
        // UV0        FLOAT false   2   |  Holds width and height            |  2x float
        // UV1        SHORT false   2   |  X holds dithering strength        |  1x int
        // UV2        SHORT false   2   |  unused                            |  -
        // COLOR      UBYTE true    4   |  Holds color                       |  1x int -> 4x byte //! auto
        // NORMAL     BYTE  true    3   |  Unusable. Bad alignment           |  -

        // Position & local position
        final @NotNull Vector2f pos = pose.transformPosition(x, y, new Vector2f());
        vc.addVertex(pos.x, pos.y, localX);
        vc.setLineWidth(localY);

        // Width and Height
        vc.setUv(w, h);

        // Color
        vc.setColor(color);

        // Dithering strength
        vc.setUv1((int)(255 * ClientFeatureSync.getFeatureD(SettingsServerFeatureSet.DITHERING_STRENGTH)), 0);
    }


    @Override
    public @Nullable ScreenRectangle bounds() {
        final @NotNull Vector2f p0 = pose.transformPosition(x0, y0, new Vector2f());
        final @NotNull Vector2f p1 = pose.transformPosition(x1, y1, new Vector2f());

        final int ix0 = (int)Math.floor(Math.min(p0.x, p1.x));
        final int iy0 = (int)Math.floor(Math.min(p0.y, p1.y));
        final int ix1 = (int)Math.ceil (Math.max(p0.x, p1.x));
        final int iy1 = (int)Math.ceil (Math.max(p0.y, p1.y));

        final ScreenRectangle bounds = new ScreenRectangle(ix0, iy0, ix1 - ix0, iy1 - iy0);
        return scissorArea != null ? scissorArea.intersection(bounds) : bounds;
    }
}