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
 * A GuiElementRenderState for texture blits without antialiasing.
 */
public record RawBlitRenderState(
    RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose,
    int x0, int y0, int x1, int y1,
    float u0, float v0, float u1, float v1,
    float alpha, @Nullable ScreenRectangle scissorArea
) implements GuiElementRenderState {




    @Override
    public void buildVertices(VertexConsumer vc) {
        emit(vc, x1, y0);
        emit(vc, x1, y1);
        emit(vc, x0, y1);
        emit(vc, x0, y0);
    }




    private void emit(VertexConsumer vc, int x, int y) {

        //! Name      Type  Norm  Count
        // POSITION   FLOAT false   3   |  xy needed. z unused               |  -
        // LINE_WIDTH FLOAT false   1   |  unused                            |  -
        // UV0        FLOAT false   2   |  Holds texture UVs                 |  2x float
        // UV1        SHORT false   2   |  unused                            |  -
        // UV2        SHORT false   2   |  unused                            |  -
        // COLOR      UBYTE true    4   |  x Holds alpha                     |  1x float -> 1x byte //! auto
        // NORMAL     BYTE  true    3   |  Unusable. Bad alignment           |  -


        // Position
        vc.addVertexWith2DPose(pose, x, y);

        // UVs
        final float u = u0 + (u1 - u0) * (x - x0) / (x1 - x0);
        final float v = v0 + (v1 - v0) * (y - y0) / (y1 - y0);
        vc.setUv(u, v);

        // Alpha
        vc.setColor(0f, 0f, 0f, alpha); //! Minecraft converts to 0-255 byte on its own.
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