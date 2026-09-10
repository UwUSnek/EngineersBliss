package com.snek.engineersbliss.client.screens.pause_screen;

import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.EngineerSBlissClient;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.screens.alt_textures.AltTexturesScreen;
import com.snek.engineersbliss.client.screens.creative_tweaks.CreativeTweaksScreen;
import com.snek.engineersbliss.client.screens.macros.MacrosScreen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.screens.rendering.RenderingScreen;
import com.snek.engineersbliss.client.screens.settings.SettingsScreen;
import com.snek.engineersbliss.client.ui.base.__base_UiSidebarScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiPauseScreenButton;
import com.snek.engineersbliss.client.ui.widgets.misc.PlayerMannequin;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;








public class PauseScreenContent extends __base_UiSidebarScreen {
    private final float vanillaClusterRight;
    private final float vanillaClusterCenterY;


    public PauseScreenContent(final float vanillaClusterRight, final float vanillaClusterCenterY) {
        super(DEFAULT_SIDEBAR_WIDTH, null);
        this.vanillaClusterRight = vanillaClusterRight;
        this.vanillaClusterCenterY = vanillaClusterCenterY;
    }

//FIXME add a BIG disclaimer to "gameplay tweaks" screen that says it changes game mechanics
//FIXME anything that changes game mechanics for anything that isn't the creative player is in there (write this too)
//FIXME move no particles to alternative texture maybe?
//FIXME move visible block overlays to alternative texture maybe?






    @Override
    public void init() {
        super.init();

        // Mod name and version
        final UiTxt titleText   = new UiTxt(EngineerSBliss.MOD_NAME, Fonts.ui.light, 2f);
        final UiTxt versionText = new UiTxt(String.format("v%s", EngineerSBlissClient.getModVersion()), Fonts.ui.regular, 1f);
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, titleText,   TextAlignment.LEFT, Layout.fgColor), titleText.getScaledFont().getLineHeight());
        leftSidebar.addWidget(new UiTextWidget(this, versionText, TextAlignment.LEFT, Layout.fgColor), versionText.getScaledFont().getLineHeight());

        // Rendering
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Rendering", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Rendering filter"), "rendering_filter", RenderingScreen  ::new,    'R'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Overlays"        ), "overlays",         OverlaysScreen   ::new,    'O'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Alt textures"    ), "alt_textures",     AltTexturesScreen::new,    'T'), Layout.BORDER_HEIGHT);

        // Tools
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Tools", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor),      Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Action history"  ), "action_history",   ()->{return null;},        'U'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Version Control" ), "version_control",  ()->{return null;},        'V'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Calculator"      ), "calculator",       ()->{return null;},        'C'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Block Properties"), "block_properties", ()->{return null;},        'P'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Block Groups"    ), "block_groups",     ()->{return null;},        'G'), Layout.BORDER_HEIGHT);

        // QoL
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("QoL", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Creative tweaks" ), "creative_tweaks",  CreativeTweaksScreen::new, 'Y'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Gameplay tweaks" ), "gameplay_tweaks",  ()->{return null;},        'X'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Macros"          ), "macros",           MacrosScreen::new,         'Q'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Sound muffler"   ), "sound_muffler",    ()->{return null;},        'M'), Layout.BORDER_HEIGHT);

        // Preferences
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Preferences", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Settings"        ), "settings",         SettingsScreen::new,       'S'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Keybinds"        ), "keybinds",         ()->{return null;},        'K'), Layout.BORDER_HEIGHT);

        // Info
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Info", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Render stats"    ), "render_stats",     ()->{return null;}), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("World stats"     ), "world_stats",      ()->{return null;}), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("About"           ), "about",            ()->{return null;}), Layout.BORDER_HEIGHT);

        // // Julia set
        // final UiButton juliaScreenButton = new UiPauseScreenButton(screen, new UiTxt("??"), null, JuliaSetScreen::new, '\0');
        // addRenderableWidget(juliaScreenButton);
        // juliaScreenButton.setSize(Layout.BUTTON_HEIGHT, Layout.BUTTON_HEIGHT);
        // juliaScreenButton.setX(width  - Layout.BUTTON_HEIGHT - Layout.BUTTON_HEIGHT);
        // juliaScreenButton.setY(height - Layout.BUTTON_HEIGHT - Layout.BUTTON_HEIGHT);
    }








    @Override
    public void extractRenderState(final UiGraphics graphics, final float mouseX, final float mouseY, final float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);


        // Draw player model and name
        final LocalPlayer player = Minecraft.getInstance().player;
        if(player != null && ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.PLAYER_MODEL_IN_PAUSE_SCREEN)) {

            // Calculate dimensions and position
            float modelScale = 64;
            float boxSize = Math.max(width, height);
            final float x0 = vanillaClusterRight   - boxSize / 2f + Layout.BUTTON_HEIGHT;
            final float y0 = vanillaClusterCenterY - boxSize / 2f;
            final float x1 = x0 + boxSize;
            final float y1 = y0 + boxSize;

            // Get mannequin
            final @Nullable PlayerMannequin model = PlayerMannequin.getMannequin();
            if(model != null) {
                graphics.entity(x0, y0, x1, y1, modelScale, 0.0f, mouseX, mouseY, model);
            }

            // Calculate play time
            final long ms = MinecraftUtils.getPlaytimeMs();
            final long hours   = TimeUnit.MILLISECONDS.toHours  (ms);
            final long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
            final long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;

            // Calculate text dimensions and position
            final UiTxt playerName = new UiTxt(String.format("%s", player.getGameProfile().name()),             Fonts.ui.regular, Layout.HEADER_SCALE);
            final UiTxt playTime   = new UiTxt(String.format("Playtime: %dh %dm %ds", hours, minutes, seconds), Fonts.ui.light);
            int textCenterX = Math.round((x0 + x1) / 2f);
            int nameY = Math.round(vanillaClusterCenterY - modelScale - playerName.getScaledFont().getLineHeight() - playTime.getScaledFont().getLineHeight());
            int titleY = nameY + playerName.getScaledFont().getLineHeight() + 2;

            // Draw player name an title
            graphics.text(playerName, textCenterX,  nameY, 0xFFFFC200, TextAlignment.CENTER_ANCHORED, 0, true);
            graphics.text(playTime,   textCenterX, titleY, 0xFFDDDDDD, TextAlignment.CENTER_ANCHORED, 0, true);
        }
    }
}