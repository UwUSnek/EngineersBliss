package com.snek.engineersbliss.client.feature_handlers.settings;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;








public class SettingsFeatureHandler {
    private SettingsFeatureHandler() {}


    public static int getGuiScalesNumber() {
        return SettingsServerFeatureSet.GUI_SCALE.getValues().size();
    }

    public static float getCurrentGuiScale() {
        return SettingsServerFeatureSet.GUI_SCALE.getValues().get(getCurrentGuiScaleIndex());
    }

    public static int getCurrentGuiScaleIndex() {
        return ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.GUI_SCALE);
    }
}
