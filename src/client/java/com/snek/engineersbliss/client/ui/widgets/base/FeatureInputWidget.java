package com.snek.engineersbliss.client.ui.widgets.base;

import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;

import org.jetbrains.annotations.Nullable;




public interface FeatureInputWidget {
    public                  ClientFeature<?> getClientFeature();
    public @Nullable __base_ServerFeature<?> getServerFeature();
}
