package com.snek.engineersbliss.client.ui.renderer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;








/**
 * A GuiElementRenderState for antialiased sprite and texture blits.
 */
public record AaBlitRenderState(
    RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose,
    float x0, float y0, float x1, float y1,
    float u0, float v0, float u1, float v1,
    float alpha, @Nullable ScreenRectangle scissorArea
) implements GuiElementRenderState {




    @Override
    public void buildVertices(VertexConsumer vc) {
        final float ex0 = x0 - 1;
        final float ey0 = y0 - 1;
        final float ex1 = x1 + 1;
        final float ey1 = y1 + 1;
        final int w = Math.round(x1 - x0);
        final int h = Math.round(y1 - y0);

        emit(vc, ex1, ey0, w, h);
        emit(vc, ex1, ey1, w, h);
        emit(vc, ex0, ey1, w, h);
        emit(vc, ex0, ey0, w, h);
    }




    private void emit(VertexConsumer vc, float x, float y, int w, int h) {

        //! Name      Type  Norm  Count
        // POSITION   FLOAT false   3   |  xy needed. z holds X position     |  1x float
        // LINE_WIDTH FLOAT false   1   |  Holds Y position                  |  1x float
        // UV0        FLOAT false   2   |  Holds texture UVs                 |  2x float
        // UV1        SHORT false   2   |  Holds width                       |  1x float -> 2x short
        // UV2        SHORT false   2   |  Holds height                      |  1x float -> 2x short
        // COLOR      UBYTE true    4   |  x Holds alpha                     |  1x float -> 1x byte //! auto
        // NORMAL     BYTE  true    3   |  Unusable. Bad alignment           |  -


        // Position
        final @NotNull Vector2f pos = pose.transformPosition(x, y, new Vector2f());
        vc.addVertex(pos.x, pos.y, x - x0);
        vc.setLineWidth(y - y0);

        // UVs
        final float u = u0 + (u1 - u0) * (x - x0) / (x1 - x0);
        final float v = v0 + (v1 - v0) * (y - y0) / (y1 - y0);
        vc.setUv(u, v);

        // Width and Height
        int wBits = Float.floatToRawIntBits(w);
        int hBits = Float.floatToRawIntBits(h);
        vc.setUv1(wBits >>> 16, wBits & 0xFFFF);
        vc.setUv2(hBits >>> 16, hBits & 0xFFFF);

        // Alpha
        vc.setColor(0f, 0f, 0f, alpha); //! Minecraft converts to 0-255 byte on its own.
    }




    @Override
    public @Nullable ScreenRectangle bounds() {
		ScreenRectangle bounds = new ScreenRectangle((int)x0, (int)y0, (int)x1 - (int)x0, (int)y1 - (int)y0).transformMaxBounds(pose);
		return scissorArea != null ? scissorArea.intersection(bounds) : bounds;
	}
}