package com.snek.engineersbliss.client.feature_handlers.settings;

import java.util.function.Supplier;

import com.snek.engineersbliss.EngineerSBliss;
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




    public static final ClientFeature<?> GUI_SCALE = new ClientFeature<>(
        SettingsServerFeatureSet.GUI_SCALE,
        s(() -> new UiTxt("Gui scale")),
        s(() -> (UiTxt)new UiTxt("The visual scale of GUI elements."))
    );
    public static final ClientFeature<?> GUI_BACKGROUND_OPACITY = new ClientFeature<>(
        SettingsServerFeatureSet.GUI_BACKGROUND_OPACITY,
        s(() -> new UiTxt("Gui background opacity")),
        s(() -> (UiTxt)new UiTxt("The opacity of the background color of screens."))
    );
    public static final ClientFeature<?> PAUSE_GAME_IN_PAUSE_MENU = new ClientFeature<>(
        SettingsServerFeatureSet.PAUSE_GAME_IN_PAUSE_MENU,
        s(() -> new UiTxt("Pause Menu pauses the game")),
        s(() -> new UiTxt("Pauses the game while the Pause Menu is open. This is the default behaviour in Vanilla.")),
        Notices.SINGLE_PLAYER_ONLY
    );
    public static final ClientFeature<?> PAUSE_GAME_IN_MOD_SCREENS = new ClientFeature<>(
        SettingsServerFeatureSet.PAUSE_GAME_IN_MOD_SCREENS,
        s(() -> new UiTxt("" + EngineerSBliss.MOD_NAME + " screens pause the game")),
        s(() -> new UiTxt("Pauses the game while any of the screens from the " + EngineerSBliss.MOD_NAME + " mod are open.")),
        Notices.SINGLE_PLAYER_ONLY
    );
    public static final ClientFeature<?> DEBUG_OVERLAYS = new ClientFeature<>(
        SettingsServerFeatureSet.DEBUG_OVERLAYS,
        s(() -> new UiTxt("Debug overlays")),
        s(() -> new UiTxt("Enable GUI debug overlays."))
    );




    public static final ClientFeature<?> STATUS_BAR_HEIGHT = new ClientFeature<>(
        SettingsServerFeatureSet.STATUS_BAR_HEIGHT,
        s(() -> new UiTxt("Status Bar height")),
        s(() -> new UiTxt("The height of the in-game status bar, in pixels."))
    );
    public static final ClientFeature<?> STATUS_BAR_POSITION = new ClientFeature<>(
        SettingsServerFeatureSet.STATUS_BAR_POSITION,
        s(() -> new UiTxt("Status Bar position")),
        s(() -> new UiTxt("The position of the in-game status bar."))
    );
    public static final ClientFeature<?> CHAT_HIDES_STATUS_BAR = new ClientFeature<>(
        SettingsServerFeatureSet.CHAT_HIDES_STATUS_BAR,
        s(() -> new UiTxt("Chat hides Status Bar")),
        s(() -> new UiTxt("Hides the Status Bar when the chat is open."))
    );




    public static final ClientFeature<?> PLAYER_MODEL_IN_PAUSE_SCREEN = new ClientFeature<>(
        SettingsServerFeatureSet.PLAYER_MODEL_IN_PAUSE_SCREEN,
        s(() -> new UiTxt("Display player model in pause screen")),
        s(() -> new UiTxt("Renders your player's skin, name, and playtime in the pause menu."))
    );
    public static final ClientFeature<?> BLOCK_SHADERS = new ClientFeature<>(
        SettingsServerFeatureSet.BLOCK_SHADERS,
        s(() -> new UiTxt("Enable Block Shaders.")),
        s(() -> new UiTxt("Whether to use shaders for custom block modelsn")),
        s(() -> new UiTxt("This can be very laggy and resource intensive."))
    );
    public static final ClientFeature<?> BLOCK_SHADER_LIMIT = new ClientFeature<>(
        SettingsServerFeatureSet.BLOCK_SHADER_LIMIT,
        s(() -> new UiTxt("Block Shader limit")),
        s(() -> new UiTxt("The maximum number of shaded block models to display at once. This can help control lag in busy areasn")),
        s(() -> (UiTxt)new UiTxt("Only available when [Block Shaders] is ON.").Orange())
    );




    // Misc
    public static final ClientFeature<?> METAL_PIPE_SOUNDS = new ClientFeature<>(
        SettingsServerFeatureSet.METAL_PIPE_SOUNDS,
        s(() -> new UiTxt("Metal pipe UI sounds")),
        s(() -> new UiTxt("Replaces all UI sounds with the Metal Pipe Falling sound."))
    );








    private static class Notices {
        private static Supplier<UiTxt> SINGLE_PLAYER_ONLY = () -> (UiTxt)new UiTxt(
            "This only works in Single Player."
        ).red();
    }
}
