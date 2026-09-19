package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.DoubleConsumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerAnalogueFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.widgets.base.DualPreviewFeatureInputWidget;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.utils.Layout;

import net.minecraft.resources.Identifier;








public class UiAnalogueFeatureSlider extends UiAnalogueSlider implements DualPreviewFeatureInputWidget {
    final                  ClientFeature<?> clientFeature;
    final @Nullable __base_ServerFeature<?> serverFeature;
    private final double leftPreviewValue;
    private final double rightPreviewValue;


    @Override public                  ClientFeature<?> getClientFeature() { return clientFeature; }
    @Override public @Nullable __base_ServerFeature<?> getServerFeature() { return serverFeature; }
    @Override public String getLeftPreviewSuffix () { return "a";  }
    @Override public String getRightPreviewSuffix() { return "b"; }
    @Override public String getLeftTitle         () { return formatValueAt(leftPreviewValue,  true); }
    @Override public String getRightTitle        () { return formatValueAt(rightPreviewValue, true); }








    @SuppressWarnings("unchecked")
    public UiAnalogueFeatureSlider(
        final UiScreen screen,
        final ClientFeature<?> feature,
        final @Nullable DoubleConsumer afterChangeCallback,
        final @Nullable ValueFormatter<Double> valueFormatter,
        final double leftPreviewValue,
        final double rightPreviewValue
    ) {
        // Throw exception if not a ServerAnalogueFeature
        final @NotNull __base_ServerFeature<?> genericServerFeature = feature.getServerFeature();
        if(!(genericServerFeature instanceof ServerAnalogueFeature)) {
            throw new IllegalArgumentException(
                "UiAnalogueFeatureSlider created using a feature of incompatible type: " +
                genericServerFeature.getClass().getName()
            );
        }

        // Proceed with normal initialization
        final @NotNull ServerAnalogueFeature _serverFeature = (ServerAnalogueFeature)genericServerFeature;
        super(
            screen,
            feature.getName(),
            _serverFeature.getMin(), _serverFeature.getMax(),
            ClientFeatureSync.getFeatureD(_serverFeature),
            n -> onChange(_serverFeature, n, afterChangeCallback),
            valueFormatter
        );
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;
        this.leftPreviewValue = leftPreviewValue;
        this.rightPreviewValue = rightPreviewValue;


        // Calculate sprite id
        final String bgSpritePath = String.format("gui/sprites/%s/%s", serverFeature.getFeatureSet().getId(), serverFeature.getId());
        withSpriteBg(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, bgSpritePath), 1f);
        getLeftLabelMargin().clear().addHF(1f).addPx(Layout.textMarginPx);
    }








    public static void onChange(final __base_ServerFeature<Double> feature, final double newValue, final @Nullable DoubleConsumer afterChangeCallback) {

        // Set feature and send packets to the server
        ClientFeatureSync.setFeature(feature, newValue);

        // Call Slider UI callback
        if(afterChangeCallback != null) afterChangeCallback.accept(newValue);

        //! Feature change callback is called by the server when packets are received
    }
}