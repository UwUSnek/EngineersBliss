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




    public static final ClientFeature<?> PAUSE_GAME_IN_PAUSE_MENU = new ClientFeature<>(
        SettingsServerFeatureSet.PAUSE_GAME_IN_PAUSE_MENU,
        () -> new UiTxt("Pause Menu pauses the game"),
        () -> (UiTxt)new UiTxt()
            .cat(new UiTxt("Pauses the game while the Pause Menu is open. This is the default behaviour in Vanilla\n."))
            .cat(Notices.SINGLE_PLAYER_ONLY.get())
    );
    public static final ClientFeature<?> PAUSE_GAME_IN_MOD_SCREENS = new ClientFeature<>(
        SettingsServerFeatureSet.PAUSE_GAME_IN_MOD_SCREENS,
        () -> new UiTxt("" + EngineerSBliss.MOD_NAME + " screens pause the game"),
        () -> new UiTxt()
            .cat(new UiTxt("Pauses the game while any of the screens from the " + EngineerSBliss.MOD_NAME + " mod are open\n."))
            .cat(Notices.SINGLE_PLAYER_ONLY.get())
    );




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
        () -> new UiTxt("Renders your player's skin, name, and playtime in the pause menu.")
    );
    public static final ClientFeature<?> BLOCK_SHADERS = new ClientFeature<>(
        SettingsServerFeatureSet.BLOCK_SHADERS,
        () -> new UiTxt("Enable Block Shaders."),
        () -> new UiTxt()
            .cat(new UiTxt("Whether to use shaders for custom block models.\n"))
            .cat(new UiTxt("This can be very laggy and resource intensive."))
    );
    public static final ClientFeature<?> BLOCK_SHADER_LIMIT = new ClientFeature<>(
        SettingsServerFeatureSet.BLOCK_SHADER_LIMIT,
        () -> new UiTxt("Block Shader limit"),
        () -> (UiTxt)new UiTxt()
            .cat(new UiTxt("The maximum number of shaded block models to display at once. This can help control lag in busy areas.\n"))
            .cat(new UiTxt("Only available when [Block Shaders] is ON.").Orange())
    );








    private static class Notices {
        private static Supplier<UiTxt> SINGLE_PLAYER_ONLY = () -> (UiTxt)new UiTxt(
            "This only works in Single Player."
        ).red();
    }
}
