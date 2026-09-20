package com.snek.engineersbliss.client.ui.renderer.render_states;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;








/**
 * A GuiElementRenderState for multiline draws.
 */
public record AaMultilineRenderState(
    RenderPipeline pipeline, Matrix3x2f pose,
    float x0, float y0, float x1, float y1,
    float[] xs, float[] ys,
    float thickness, int color,
    @Nullable ScreenRectangle scissorArea
) implements GuiElementRenderState {



    @Override
    public void buildVertices(VertexConsumer vc) {
        final float halfT = thickness * 0.5f;
        final float aaMargin = 1.0f;
        final float halfExt = halfT + aaMargin;

        for(int i = 0; i < xs.length - 1; ++i) {
            final float ax = xs[i],     ay = ys[i];
            final float bx = xs[i + 1], by = ys[i + 1];

            float dx = bx - ax;
            float dy = by - ay;
            final float len = (float)Math.sqrt(dx * dx + dy * dy);
            if(len < 1e-6f) continue;

            dx /= len;
            dy /= len;

            final float px = -dy * halfExt;
            final float py =  dx * halfExt;

            emit(vc, ax + px, ay + py,  halfExt);
            emit(vc, ax - px, ay - py, -halfExt);
            emit(vc, bx - px, by - py, -halfExt);
            emit(vc, bx + px, by + py,  halfExt);
        }
    }

    private void emit(VertexConsumer vc, float x, float y, float halfExt) {
        vc.addVertexWith2DPose(pose, x, y);
        vc.setColor(color);
        vc.setLineWidth(thickness);
        vc.setUv(halfExt, 0f);
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



    @Override
    public @NotNull TextureSetup textureSetup() {
        return TextureSetup.noTexture();
    }
}