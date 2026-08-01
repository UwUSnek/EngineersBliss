package com.snek.engineersbliss.feature_handlers.settings;

import java.util.stream.IntStream;

import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








public class SettingsServerFeatureSet extends __base_ServerFeatureSet {
    public static SettingsServerFeatureSet INSTANCE = new SettingsServerFeatureSet();
    private SettingsServerFeatureSet() { super("settings"); }




    public static ServerSteppedFeature<Integer> STATUS_BAR_HEIGHT = INSTANCE.registerFeature(new ServerSteppedFeature<Integer>(
        "status_bar_height",
        IntStream.range(10, 101).boxed().toList(), 24 - 10
    ));
    public static ServerToggleFeature STATUS_BAR_POSITION = INSTANCE.registerFeature(new ServerToggleFeature(
        "status_bar_position",
        false
    ));
    public static ServerToggleFeature PLAYER_MODEL_IN_PAUSE_SCREEN = INSTANCE.registerFeature(new ServerToggleFeature(
        "player_model_in_pause_screen",
        false
    ));
}
