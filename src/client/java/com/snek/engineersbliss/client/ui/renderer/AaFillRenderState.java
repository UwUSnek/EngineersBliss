package com.snek.engineersbliss.client.ui.renderer;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;




/**
 * A GuiElementRenderState for antialiased rects.
 * This is used by UiGraphics to produce antialiased regions without multiple draw calls, improving performance.
 */
public record AaFillRenderState(
    RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose,
    float x0, float y0, float x1, float y1,
    int color, @Nullable ScreenRectangle scissorArea
) implements GuiElementRenderState {

    @Override
    public void buildVertices(VertexConsumer vc) {
        final float ex0 = x0 - 1;
        final float ey0 = y0 - 1;
        final float ex1 = x1 + 1;
        final float ey1 = y1 + 1;
        final int w = Math.round(x1 - x0);
        final int h = Math.round(y1 - y0);
        emit(vc, ex1, ey0, ex1 - x0, ey0 - y0, w, h);
        emit(vc, ex1, ey1, ex1 - x0, ey1 - y0, w, h);
        emit(vc, ex0, ey1, ex0 - x0, ey1 - y0, w, h);
        emit(vc, ex0, ey0, ex0 - x0, ey0 - y0, w, h);
    }


    private void emit(VertexConsumer vc, float x, float y, float localX, float localY, int w, int h) {
        vc.addVertexWith2DPose(pose, x, y);
        vc.setColor(color);
        vc.setUv(localX, localY);
        vc.setUv1(w, h); // rect size data
    }


    @Override
    public @Nullable ScreenRectangle bounds() {
		ScreenRectangle bounds = new ScreenRectangle((int)x0, (int)y0, (int)x1 - (int)x0, (int)y1 - (int)y0).transformMaxBounds(pose);
		return scissorArea != null ? scissorArea.intersection(bounds) : bounds;
    }
}