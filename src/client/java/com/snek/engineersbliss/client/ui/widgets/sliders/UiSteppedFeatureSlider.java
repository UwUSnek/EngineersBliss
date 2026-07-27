package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;

import net.minecraft.client.gui.screens.Screen;








public class UiSteppedFeatureSlider<T> extends UiSteppedSlider<T> {
    final                  ClientFeature<?> clientFeature;
    final @Nullable ServerSteppedFeature<T> serverFeature;


    public                  ClientFeature<?> getClientFeature() { return clientFeature; }
    public @Nullable ServerSteppedFeature<T> getServerFeature() { return serverFeature; }




    public UiSteppedFeatureSlider(final Screen screen, final ClientFeature<?> feature) {
        this(screen, 50, 50, 50, 50, feature, null);
    }
    public UiSteppedFeatureSlider(final Screen screen, final int x, final int y, final int w, final int h, final ClientFeature<?> feature) {
        this(screen, x, y, w, h, feature, null);
    }
    public UiSteppedFeatureSlider(final Screen screen, final ClientFeature<?> feature, final @Nullable BiConsumer<Integer, T> afterChangeCallback) {
        this(screen, 50, 50, 50, 50, feature, afterChangeCallback);
    }

    @SuppressWarnings("unchecked")
    public UiSteppedFeatureSlider(final Screen screen, final int x, final int y, final int w, final int h, final ClientFeature<?> feature, final @Nullable BiConsumer<Integer, T> afterChangeCallback) {

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
            screen, x, y, w, h,
            feature.calcName(),
            _serverFeature.getValues(),
            ClientFeatureSync.getFeatureI(_serverFeature),
            (i, n) -> onChange(_serverFeature, i, n, afterChangeCallback)
        );
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;
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