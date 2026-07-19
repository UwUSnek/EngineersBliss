package com.snek.engineersbliss.client.screens.parts;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;








public class UiSteppedFeatureSlider<T> extends UiSteppedSlider<T> {
    final                  ClientFeature<?> clientFeature;
    final @Nullable ServerSteppedFeature<T> serverFeature;


    public          ClientFeature<?>         getClientFeature() { return clientFeature; }
    public @Nullable ServerSteppedFeature<T> getServerFeature() { return serverFeature; }




    public UiSteppedFeatureSlider(final ClientFeature<?> feature) {
        this(0, 0, 0, 0, feature, null);
    }
    public UiSteppedFeatureSlider(final int x, final int y, final int w, final int h, final ClientFeature<?> feature) {
        this(x, y, w, h, feature, null);
    }
    public UiSteppedFeatureSlider(final ClientFeature<?> feature, final @Nullable Consumer<T> afterChangeCallback) {
        this(0, 0, 0, 0, feature, afterChangeCallback);
    }
    public UiSteppedFeatureSlider(final int x, final int y, final int w, final int h, final ClientFeature<?> feature, final @Nullable Consumer<T> afterChangeCallback) {

        // Call superconstructor in a safe way
        final boolean isSteppedFeature = feature.getServerFeature() instanceof ServerSteppedFeature<?>;
        final ServerSteppedFeature<T> _serverFeature = isSteppedFeature ? (ServerSteppedFeature<T>)feature.getServerFeature() : null;
        final List<T>  _values = _serverFeature == null ? List.<T>of() : _serverFeature.getValues();
        final Integer _default = _serverFeature == null ?            0 : _serverFeature.getDefault();
        super(x, y, w, h, feature.calcName(), _values, _default, afterChangeCallback);
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
}