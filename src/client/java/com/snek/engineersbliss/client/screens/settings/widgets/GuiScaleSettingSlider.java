package com.snek.engineersbliss.client.screens.settings.widgets;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSteppedFeatureSlider;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;




public class GuiScaleSettingSlider extends UiSteppedFeatureSlider<Float> {
    public GuiScaleSettingSlider(Screen screen, ClientFeature<?> feature, BiConsumer<Integer, Float> afterChangeCallback, ValueFormatter<Float> valueFormatter, int leftPreviewIndex, int rightPreviewIndex) {
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
        if(getScreen() instanceof @NotNull __base_UiScreen uiScreen) uiScreen.resize(0, 0);
        return result;
    }


    //! No-op fireChangeCallback stops keys from working properly. This restores that behaviour.
    @Override
    public boolean keyPressed(KeyEvent event) {
        final boolean r = super.keyPressed(event);
        super.fireChangeCallback();
        if(getScreen() instanceof @NotNull __base_UiScreen uiScreen) uiScreen.resize(0, 0);
        return r;
    }
}
