package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.client.ui.widgets.base.DualPreviewFeatureInputWidget;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.utils.Layout;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;








public class UiSteppedFeatureSlider<T> extends UiSteppedSlider<T> implements DualPreviewFeatureInputWidget {
    final                  ClientFeature<?> clientFeature;
    final @Nullable __base_ServerFeature<?> serverFeature;
    private final int leftPreviewIndex;
    private final int rightPreviewIndex;


    @Override public                  ClientFeature<?> getClientFeature() { return clientFeature; }
    @Override public @Nullable __base_ServerFeature<?> getServerFeature() { return serverFeature; }
    @Override public String getLeftPreviewSuffix () { return "a";  }
    @Override public String getRightPreviewSuffix() { return "b"; }
    @Override public String getLeftTitle         () { return formatValueAt(leftPreviewIndex,  true); }
    @Override public String getRightTitle        () { return formatValueAt(rightPreviewIndex, true); }








    @SuppressWarnings("unchecked")
    public UiSteppedFeatureSlider(
        final Screen screen,
        final ClientFeature<?> feature,
        final @Nullable BiConsumer<Integer, T> afterChangeCallback,
        final @Nullable ValueFormatter<T> valueFormatter,
        final int leftPreviewIndex,
        final int rightPreviewIndex
    ) {
        // Throw exception if not a ServerSteppedFeature
        final @NotNull __base_ServerFeature<?> genericServerFeature = feature.getServerFeature();
        if(!(genericServerFeature instanceof ServerSteppedFeature<?>)) {
            throw new IllegalArgumentException(
                "UiSteppedFeatureSlider created using a feature of incompatible type: " +
                genericServerFeature.getClass().getName()
            );
        }


        // Proceed with normal initialization
        final @NotNull ServerSteppedFeature<T> _serverFeature = (ServerSteppedFeature<T>)genericServerFeature;
        super(
            screen,
            feature.calcName(),
            _serverFeature.getValues(),
            ClientFeatureSync.getFeatureI(_serverFeature),
            (i, n) -> onChange(_serverFeature, i, n, afterChangeCallback),
            valueFormatter
        );
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;
        this.leftPreviewIndex = leftPreviewIndex;
        this.rightPreviewIndex = rightPreviewIndex;


        // Calculate sprite id
        final String bgSpritePath = String.format("%s/%s", serverFeature.getFeatureSet().getId(), serverFeature.getId());
        withSpriteBg(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, bgSpritePath), 1f);
        getLeftLabelMargin().clear().addHF(1f).addPx(Layout.textMarginPx);
    }








    public static <T> void onChange(
        final __base_ServerFeature<Integer> feature,
        final int newIndex,
        final T newValue,
        final @Nullable BiConsumer<Integer, T> afterChangeCallback
    ) {

        // Set feature and send packets to the server
        ClientFeatureSync.setFeature(feature, newIndex);

        // Call Slider UI callback
        if(afterChangeCallback != null) afterChangeCallback.accept(newIndex, newValue);

        //! Feature change callback is called by the server when packets are received
    }
}