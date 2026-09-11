package com.snek.engineersbliss.client.screens.settings.widgets;

import java.util.function.BiConsumer;

import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSteppedFeatureSlider;

import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;




public class GuiScaleSettingSlider extends UiSteppedFeatureSlider<Float> {
    public GuiScaleSettingSlider(UiScreen screen, ClientFeature<?> feature, BiConsumer<Integer, Float> afterChangeCallback, ValueFormatter<Float> valueFormatter, int leftPreviewIndex, int rightPreviewIndex) {
        super(screen, feature, afterChangeCallback, valueFormatter, leftPreviewIndex, rightPreviewIndex);
    }


    // Stop the superclass from changing GUI scale while sliding
    @Override
    protected void fireChangeCallback() {
        // Empty
    }


    // Manually set the feature when the mouse is released
    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        boolean result = super.mouseReleased(event);
        super.fireChangeCallback();
        getScreen().resize(0, 0);
        return result;
    }


    //! No-op fireChangeCallback stops keys from working properly. This restores that behaviour.
    @Override
    public boolean keyPressed(KeyEvent event) {
        final boolean r = super.keyPressed(event);
        super.fireChangeCallback();
        getScreen().resize(0, 0);
        return r;
    }
}
