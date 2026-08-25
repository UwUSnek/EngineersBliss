package com.snek.engineersbliss.feature_handlers.settings;

import java.util.List;

import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








public class SettingsServerFeatureSet extends __base_ServerFeatureSet {
    public static int BLOCK_SHADER_LIMIT_INFINITE = 0xC0FFEE & 0xFFF;

    public static SettingsServerFeatureSet INSTANCE = new SettingsServerFeatureSet();
    private SettingsServerFeatureSet() { super("settings"); }




    public static final ServerToggleFeature PAUSE_GAME_IN_PAUSE_MENU  = INSTANCE.registerFeature(new ServerToggleFeature("pause_game_in_pause_menu",  true));
    public static final ServerToggleFeature PAUSE_GAME_IN_MOD_SCREENS = INSTANCE.registerFeature(new ServerToggleFeature("pause_game_in_mod_screens", true));




    public static final ServerSteppedFeature<Float> STATUS_BAR_HEIGHT = INSTANCE.registerFeature(new ServerSteppedFeature<Float>(
        "status_bar_height",
        //! Multipliers of the default height
        List.of(0.75f, 0.875f, 1f, 1.125f, 1.25f, 1.375f, 1.5f, 1.625f, 1.75f, 1.875f, 2f), 1
    ));
    public static final ServerToggleFeature STATUS_BAR_POSITION   = INSTANCE.registerFeature(new ServerToggleFeature("status_bar_position",   false));
    public static final ServerToggleFeature CHAT_HIDES_STATUS_BAR = INSTANCE.registerFeature(new ServerToggleFeature("chat_hides_status_bar", true));




    public static final ServerSteppedFeature<Float> GUI_SCALE = INSTANCE.registerFeature(new ServerSteppedFeature<Float>(
        "gui_scale",
        List.of(1f, 1.5f, 2f, 2.5f, 3f), 2
    ));
    public static final ServerToggleFeature DEBUG_OVERLAYS               = INSTANCE.registerFeature(new ServerToggleFeature("debug_overlays", false));
    public static final ServerToggleFeature PLAYER_MODEL_IN_PAUSE_SCREEN = INSTANCE.registerFeature(new ServerToggleFeature("player_model_in_pause_screen", true));
    public static final ServerToggleFeature BLOCK_SHADERS                = INSTANCE.registerFeature(new ServerToggleFeature("block_shaders", true));
    public static final ServerSteppedFeature<Integer> BLOCK_SHADER_LIMIT = INSTANCE.registerFeature(new ServerSteppedFeature<Integer>(
        "block_shader_limit",
        List.of(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, BLOCK_SHADER_LIMIT_INFINITE), 6
    ));
}
