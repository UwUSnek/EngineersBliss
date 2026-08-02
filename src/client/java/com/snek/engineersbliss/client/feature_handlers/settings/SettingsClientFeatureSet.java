package com.snek.engineersbliss.client.feature_handlers.settings;

import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;








@SuppressWarnings("java:S1905")
public class SettingsClientFeatureSet extends __base_ClientFeatureSet<SettingsServerFeatureSet> {
    public static final SettingsClientFeatureSet INSTANCE = new SettingsClientFeatureSet();
    private SettingsClientFeatureSet() {
        super(SettingsServerFeatureSet.INSTANCE, () -> new UiTxt("Settings"));
    }



    public static final ClientFeature<?> STATUS_BAR_HEIGHT = new ClientFeature<>(
        SettingsServerFeatureSet.STATUS_BAR_HEIGHT,
        () -> new UiTxt("Status Bar height"),
        () -> new UiTxt("The height of the in-game status bar, in pixels.")
    );
    public static final ClientFeature<?> STATUS_BAR_POSITION = new ClientFeature<>(
        SettingsServerFeatureSet.STATUS_BAR_POSITION,
        () -> new UiTxt("Status Bar position"),
        () -> new UiTxt("The position of the in-game status bar.")
    );
    public static final ClientFeature<?> CHAT_HIDES_STATUS_BAR = new ClientFeature<>(
        SettingsServerFeatureSet.CHAT_HIDES_STATUS_BAR,
        () -> new UiTxt("Chat hides Status Bar"),
        () -> new UiTxt("Hides the Status Bar when the chat is open.")
    );




    public static final ClientFeature<?> PLAYER_MODEL_IN_PAUSE_SCREEN = new ClientFeature<>(
        SettingsServerFeatureSet.PLAYER_MODEL_IN_PAUSE_SCREEN,
        () -> new UiTxt("Display player model in pause screen"),
        () -> new UiTxt("Whether to render your player's skin in the pause menu.")
    );
}
