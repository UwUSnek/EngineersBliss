package com.snek.engineersbliss.client.ui.renderer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;








/**
 * A GuiElementRenderState for multiline draws.
 */
public record MultilineAreaRenderState(
    RenderPipeline pipeline, Matrix3x2f pose,
    float x0, float y0, float x1, float y1,
    float[] xs, float[] ys,
    int color,
    @Nullable ScreenRectangle scissorArea
) implements GuiElementRenderState {




    @Override
    public void buildVertices(VertexConsumer vc) {
        for(int i = 0; i < xs.length - 1; ++i) {
            emit(vc, xs[i],   ys[i]);
            emit(vc, xs[i+1], ys[i+1]);
            emit(vc, xs[i+1], y1);
            emit(vc, xs[i],   y1);
        }
    }




    private void emit(VertexConsumer vc, float x, float y) {

        //! Name      Type  Norm  Count
        // POSITION   FLOAT false   3   |  xy needed. z unused      |  -
        // UV0        FLOAT false   2   |  unused                   |  -
        // LINE_WIDTH FLOAT false   1   |  unused                   |  -
        // UV1        SHORT false   2   |  unused                   |  -
        // UV2        SHORT false   2   |  unused                   |  -
        // COLOR      UBYTE true    4   |  Holds color              |  1x int -> 4x byte //! auto
        // NORMAL     BYTE  true    3   |  Unusable. Bad alignment  |  -

        vc.addVertexWith2DPose(pose, x, y);
        vc.setColor(color);
    }




    @Override
    public @Nullable ScreenRectangle bounds() { //FIXME this might clip a few edge pixels
		ScreenRectangle bounds = new ScreenRectangle((int)x0, (int)y0, (int)x1 - (int)x0, (int)y1 - (int)y0).transformMaxBounds(pose);
		return scissorArea != null ? scissorArea.intersection(bounds) : bounds;
	}



    @Override
    public @NotNull TextureSetup textureSetup() {
        return TextureSetup.noTexture();
    }
}