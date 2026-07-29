package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;








public class UiSteppedFeatureSlider<T> extends UiSteppedSlider<T> {
    final                  ClientFeature<?> clientFeature;
    final @Nullable ServerSteppedFeature<T> serverFeature;


    public                  ClientFeature<?> getClientFeature() { return clientFeature; }
    public @Nullable ServerSteppedFeature<T> getServerFeature() { return serverFeature; }




    public UiSteppedFeatureSlider(final Screen screen, final ClientFeature<?> feature) {
        this(screen, 50, 50, 50, 50, feature, null, null);
    }
    public UiSteppedFeatureSlider(final Screen screen, final int x, final int y, final int w, final int h, final ClientFeature<?> feature) {
        this(screen, x, y, w, h, feature, null, null);
    }
    public UiSteppedFeatureSlider(final Screen screen, final ClientFeature<?> feature, final @Nullable BiConsumer<Integer, T> afterChangeCallback) {
        this(screen, 50, 50, 50, 50, feature, afterChangeCallback, null);
    }
    public UiSteppedFeatureSlider(final Screen screen, final int x, final int y, final int w, final int h, final ClientFeature<?> feature, final @Nullable BiConsumer<Integer, T> afterChangeCallback) {
        this(screen, x, y, w, h, feature, afterChangeCallback, null);
    }

    public UiSteppedFeatureSlider(final Screen screen, final ClientFeature<?> feature, final @Nullable Function<UiSlider, UiTxt> valueFormatter) {
        this(screen, 50, 50, 50, 50, feature, null, valueFormatter);
    }
    public UiSteppedFeatureSlider(final Screen screen, final int x, final int y, final int w, final int h, final ClientFeature<?> feature, final @Nullable Function<UiSlider, UiTxt> valueFormatter) {
        this(screen, x, y, w, h, feature, null, valueFormatter);
    }
    public UiSteppedFeatureSlider(final Screen screen, final ClientFeature<?> feature, final @Nullable BiConsumer<Integer, T> afterChangeCallback, final @Nullable Function<UiSlider, UiTxt> valueFormatter) {
        this(screen, 50, 50, 50, 50, feature, afterChangeCallback, valueFormatter);
    }
    @SuppressWarnings("unchecked")
    public UiSteppedFeatureSlider(final Screen screen, final int x, final int y, final int w, final int h, final ClientFeature<?> feature, final @Nullable BiConsumer<Integer, T> afterChangeCallback, final @Nullable Function<UiSlider, UiTxt> valueFormatter) {

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
            (i, n) -> onChange(_serverFeature, i, n, afterChangeCallback),
            valueFormatter //! No special formatting by default
        );
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;

        // Calculate sprite id
        // final String bgSpritePath = String.format("textures/gui/sprite/%s/%s.svg", serverFeature.getFeatureSet().getId(), serverFeature.getId()); //TODO REMOVE
        final String bgSpritePath = String.format("%s/%s", serverFeature.getFeatureSet().getId(), serverFeature.getId());
        withSpriteBg(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, bgSpritePath), 1f);
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