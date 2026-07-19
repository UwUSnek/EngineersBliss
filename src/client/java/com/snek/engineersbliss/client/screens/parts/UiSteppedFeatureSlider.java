package com.snek.engineersbliss.client.screens.parts;

import java.util.List;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;








public class UiSteppedFeatureSlider<T> extends UiSteppedSlider<T> {
    final                  ClientFeature<?> clientFeature;
    final @Nullable ServerSteppedFeature<T> serverFeature;


    public                  ClientFeature<?> getClientFeature() { return clientFeature; }
    public @Nullable ServerSteppedFeature<T> getServerFeature() { return serverFeature; }




    public UiSteppedFeatureSlider(final ClientFeature<?> feature) {
        this(0, 0, 0, 0, feature, null);
    }
    public UiSteppedFeatureSlider(final int x, final int y, final int w, final int h, final ClientFeature<?> feature) {
        this(x, y, w, h, feature, null);
    }
    public UiSteppedFeatureSlider(final ClientFeature<?> feature, final @Nullable BiConsumer<Integer, T> afterChangeCallback) {
        this(0, 0, 0, 0, feature, afterChangeCallback);
    }
    public UiSteppedFeatureSlider(final int x, final int y, final int w, final int h, final ClientFeature<?> feature, final @Nullable BiConsumer<Integer, T> afterChangeCallback) {

        // Call superconstructor in a safe way
        final boolean isSteppedFeature = feature.getServerFeature() instanceof ServerSteppedFeature<?>;
        final ServerSteppedFeature<T> _serverFeature = isSteppedFeature ? (ServerSteppedFeature<T>)feature.getServerFeature() : null;
        final List<T>                  _values = _serverFeature == null ? List.<T>of() : _serverFeature.getValues();
        final Integer                 _default = _serverFeature == null ?            0 : _serverFeature.getDefault();
        final BiConsumer<Integer, T> _callback = _serverFeature == null ? null : (i, n) -> onChange(_serverFeature, i, n, afterChangeCallback);
        super(x, y, w, h, feature.calcName(), _values, _default, _callback);
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;
        //! Passing an empty list as values will crash the client on first frame but it doesn't matter since this is never supposed to happen anyway
        //! The error will print just fine


        // Print error if needed
        if(serverFeature == null) {
            EngineerSBliss.LOGGER.error(
                "{} created using a feature of incompatible type: {}",
                getClass().getName(),
                feature.getServerFeature().getClass().getName(),
                new Throwable()
            );
        }
    }




    public static <T> void onChange(
        final __base_ServerFeature<Integer> feature,
        final int newIndex,
        final T newValue,
        final @Nullable BiConsumer<Integer, T> afterChangeCallback
    ) {
        ClientFeatureSync.setFeature(feature, newIndex);
        if (afterChangeCallback != null) afterChangeCallback.accept(newIndex, newValue);
    }
}