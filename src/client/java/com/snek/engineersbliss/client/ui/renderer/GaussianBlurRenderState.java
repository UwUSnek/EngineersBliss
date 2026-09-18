package com.snek.engineersbliss.client.ui.renderer;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fc;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;




public record GaussianBlurRenderState(
    Matrix3x2fc pose,
    int x0, int y0, int x1, int y1,
    float radius,
    float scale,
    @Nullable ScreenRectangle scissorArea
) implements PictureInPictureRenderState {
    @Override public @Nullable ScreenRectangle bounds() {
        return PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea);
    }
}