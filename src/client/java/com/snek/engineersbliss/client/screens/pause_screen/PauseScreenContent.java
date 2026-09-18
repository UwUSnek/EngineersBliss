package com.snek.engineersbliss.client.screens.pause_screen;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.EngineerSBlissClient;
import com.snek.engineersbliss.client.screens.alt_textures.AltTexturesScreen;
import com.snek.engineersbliss.client.screens.creative_tweaks.CreativeTweaksScreen;
import com.snek.engineersbliss.client.screens.macros.MacrosScreen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.screens.rendering.RenderingScreen;
import com.snek.engineersbliss.client.screens.settings.SettingsScreen;
import com.snek.engineersbliss.client.ui.base.UiSidebarScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiPauseScreenButton;
import com.snek.engineersbliss.client.ui.widgets.misc.PlayerModelWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;








public class PauseScreenContent extends UiSidebarScreen {
    private final float vanillaClusterRight;
    private final float vanillaClusterCenterY;
    private PlayerModelWidget playerModel;





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
        addWidget(playerModel = new PlayerModelWidget(this));

        // Mod name and version
        leftSidebar.addSpacer(Layout.BIG_SEPARATOR_HEIGHT);
        final UiTxt titleText   = new UiTxt(EngineerSBliss.MOD_NAME, Fonts.ui.light, 2f);
        final UiTxt versionText = new UiTxt(String.format("v%s mc%s", EngineerSBlissClient.getModVersion(), EngineerSBlissClient.getMcVersion()), Fonts.ui.regular, 1f);
        //FIXME ^ the mod version will prob contain the minecraft version too, after setting up stonecutter
        leftSidebar.addWidget(new UiTextWidget(this, titleText,   TextAlignment.LEFT, Layout.fgColor), titleText.getScaledFont().getUnscaleLineHeight());
        leftSidebar.addWidget(new UiTextWidget(this, versionText, TextAlignment.LEFT, Layout.fgColor), versionText.getScaledFont().getUnscaleLineHeight());
        leftSidebar.setLockedRows(4); //! Top spacer + mod name + mode version + spacer

        // Rendering
        leftSidebar.addSpacer(Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Rendering", Layout.HEADER_TEXT_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Rendering filter"), "rendering_filter", RenderingScreen  ::new,    'R'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Overlays"        ), "overlays",         OverlaysScreen   ::new,    'O'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Alt textures"    ), "alt_textures",     AltTexturesScreen::new,    'T'), Layout.BORDER_HEIGHT);

        // Tools
        leftSidebar.addSpacer(Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Tools", Layout.HEADER_TEXT_SCALE), TextAlignment.LEFT, Layout.fgColor),      Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Action history"  ), "action_history",   ()->{return null;},        'U'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Version Control" ), "version_control",  ()->{return null;},        'V'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Calculator"      ), "calculator",       ()->{return null;},        'C'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Block Properties"), "block_properties", ()->{return null;},        'P'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Block Groups"    ), "block_groups",     ()->{return null;},        'G'), Layout.BORDER_HEIGHT);

        // QoL
        leftSidebar.addSpacer(Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("QoL", Layout.HEADER_TEXT_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Creative tweaks" ), "creative_tweaks",  CreativeTweaksScreen::new, 'Y'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Gameplay tweaks" ), "gameplay_tweaks",  ()->{return null;},        'X'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Macros"          ), "macros",           MacrosScreen::new,         'Q'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Sound muffler"   ), "sound_muffler",    ()->{return null;},        'M'), Layout.BORDER_HEIGHT);

        // Preferences
        leftSidebar.addSpacer(Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Preferences", Layout.HEADER_TEXT_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Settings"        ), "settings",         SettingsScreen::new,       'S'), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Keybinds"        ), "keybinds",         ()->{return null;},        'K'), Layout.BORDER_HEIGHT);

        // Info
        leftSidebar.addSpacer(Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Info", Layout.HEADER_TEXT_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("Render stats"    ), "render_stats",     ()->{return null;}), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("World stats"     ), "world_stats",      ()->{return null;}), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(this, new UiTxt("About"           ), "about",            ()->{return null;}), Layout.BORDER_HEIGHT);
    }




    @Override
    public void relayoutSelf() {
        super.relayoutSelf();

        final float boxSize = 500;
        final float xc = (vanillaClusterRight + width) / 2f;
        playerModel.setSize(boxSize, boxSize);
        playerModel.setPos(xc - boxSize / 2f, vanillaClusterCenterY - boxSize / 2f);
    }
}