package com.snek.engineersbliss.client.mixin.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.EngineerSBlissClient;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.screens.alt_textures.AltTexturesScreen;
import com.snek.engineersbliss.client.screens.creative_tweaks.CreativeTweaksScreen;
import com.snek.engineersbliss.client.screens.julia_set.JuliaSetScreen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiButton;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiPauseScreenButton;
import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;
import com.snek.engineersbliss.client.ui.widgets.misc.PlayerMannequin;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;
import com.snek.engineersbliss.client.screens.rendering.RenderingScreen;
import com.snek.engineersbliss.client.screens.settings.SettingsScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;




@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    private static final int BUTTON_HEIGHT = Layout.BUTTON_HEIGHT;
    private static final int BUTTON_MARGIN = Layout.BORDER_HEIGHT;


    // Vanilla button dimensions and position. Calculated before any custom element is added.
    private static int clusterCenterY;
    private static int clusterCenterX;
    private static int clusterSizeX;
    private static int clusterSizeY;
    private static int clusterLeft;
    private static int clusterRight;
    private static int clusterTop;
    private static int clusterBottom;


    private static UiWidgetList leftSidebar;
    private static final float leftSidebarWidth = 0.2f;




    protected PauseScreenMixin(final Component title) {
        super(title);
    }

    @Override
    public boolean isPauseScreen() {
        return ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.PAUSE_GAME_IN_PAUSE_MENU);
    }






    @Override
    public boolean keyPressed(final KeyEvent event) {
        boolean r = false;
        if(event.isEscape()) {
            if(shouldCloseOnEsc()) {
                onClose();
                return true;
            }
        }
        for(final var c : children()) {
            if(c.keyPressed(event)) r = true;
        }
        return r;
    }
    @Override
    public boolean charTyped(CharacterEvent event) {
        boolean r = false;
        for(final var c : children()) {
            if(c.charTyped(event)) r = true;
        }
        return r;
    }








    /**
     * Computes the center X of the bounding box containing all vanilla buttons.
     */
    @Unique
    private OptionalInt eb$getButtonClusterCenterX() {
        List<Button> buttons = new ArrayList<>();
        for(final var e : this.children()) if(e instanceof Button button) buttons.add(button);
        if(buttons.isEmpty()) return OptionalInt.empty();

        int minX = buttons.stream().mapToInt(Button::getX).min().orElseThrow();
        int maxX = buttons.stream().mapToInt(b -> b.getX() + b.getWidth()).max().orElseThrow();

        return OptionalInt.of((minX + maxX) / 2);
    }

    /**
     * Computes the width of the bounding box containing all vanilla buttons.
     */
    @Unique
    private OptionalInt eb$getButtonClusterWidth() {
        List<Button> buttons = new ArrayList<>();
        for(final var e : this.children()) if(e instanceof Button button) buttons.add(button);
        if(buttons.isEmpty()) return OptionalInt.empty();
        int minX = buttons.stream().mapToInt(Button::getX).min().orElseThrow();
        int maxX = buttons.stream().mapToInt(b -> b.getX() + b.getWidth()).max().orElseThrow();
        return OptionalInt.of(maxX - minX);
    }

    /**
     * Computes the height of the bounding box containing all vanilla buttons.
     */
    @Unique
    private OptionalInt eb$getButtonClusterHeight() {
        List<Button> buttons = new ArrayList<>();
        for(final var e : this.children()) if(e instanceof Button button) buttons.add(button);
        if(buttons.isEmpty()) return OptionalInt.empty();
        int minY = buttons.stream().mapToInt(Button::getY).min().orElseThrow();
        int maxY = buttons.stream().mapToInt(b -> b.getY() + b.getHeight()).max().orElseThrow();
        return OptionalInt.of(maxY - minY);
    }

    /**
     * Computes the center Y of the bounding box containing all vanilla buttons.
     */
    @Unique
    private OptionalInt eb$getButtonClusterCenterY() {
        List<Button> buttons = new ArrayList<>();
        for(final var e : this.children()) if(e instanceof Button button) buttons.add(button);
        if(buttons.isEmpty()) return OptionalInt.empty();

        int minY = buttons.stream().mapToInt(Button::getY).min().orElseThrow();
        int maxY = buttons.stream().mapToInt(b -> b.getY() + b.getHeight()).max().orElseThrow();

        return OptionalInt.of((minY + maxY) / 2);
    }








    @Inject(method = "init", at = @At("TAIL"), cancellable = false, require = 1)
    public void eb$init(final CallbackInfo ci) {
        final Screen screen = (Screen)(Object)this;
        clusterCenterX = eb$getButtonClusterCenterX().getAsInt();
        clusterCenterY = eb$getButtonClusterCenterY().getAsInt();
        clusterSizeX   = eb$getButtonClusterWidth().getAsInt();
        clusterSizeY   = eb$getButtonClusterHeight().getAsInt();
        clusterLeft    = clusterCenterX - clusterSizeX / 2;
        clusterRight   = clusterCenterX + clusterSizeX / 2;
        clusterTop     = clusterCenterY - clusterSizeY / 2;
        clusterBottom  = clusterCenterY + clusterSizeY / 2;



        leftSidebar = new UiWidgetList(this, BUTTON_HEIGHT); {
            final UiTxt titleText   = new UiTxt(EngineerSBliss.MOD_NAME, Fonts.ui.light, 2f);
            final UiTxt versionText = new UiTxt(String.format("v%s", EngineerSBlissClient.getModVersion()), Fonts.ui.regular, 1f);
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, titleText,   TextAlignment.LEFT, Layout.fgColor), titleText.getScaledFont().getLineHeight());
            leftSidebar.addWidget(new UiTextWidget(this, versionText, TextAlignment.LEFT, Layout.fgColor), versionText.getScaledFont().getLineHeight());

            // Rendering
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Rendering", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Rendering filter"), "rendering_filter", RenderingScreen  ::new,    'R'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Overlays"        ), "overlays",         OverlaysScreen   ::new,    'O'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Alt textures"    ), "alt_textures",     AltTexturesScreen::new,    'T'), Layout.BORDER_HEIGHT);

            // Tools
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Tools", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor),      Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Action history"  ), "action_history",   ()->{return null;},        'U'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Version Control" ), "version_control",  ()->{return null;},        'V'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Calculator"      ), "calculator",       ()->{return null;},        'C'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Block Properties"), "block_properties", ()->{return null;},        'P'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Block Groups"    ), "block_groups",     ()->{return null;},        'G'), Layout.BORDER_HEIGHT);

            // QoL
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("QoL", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Creative tweaks" ), "creative_tweaks",  CreativeTweaksScreen::new, 'Y'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Gameplay tweaks" ), "gameplay_tweaks",  ()->{return null;},        'X'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Macros"          ), "macros",           ()->{return null;},        'Q'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Sound muffler"   ), "sound_muffler",    ()->{return null;},        'M'), Layout.BORDER_HEIGHT);

            // Preferences
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Preferences", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Settings"        ), "settings",         SettingsScreen::new,       'S'), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Keybinds"        ), "keybinds",         ()->{return null;},        'K'), Layout.BORDER_HEIGHT);

            // Info
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Info", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("Render stats"    ), "render_stats",     ()->{return null;}), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("World stats"     ), "world_stats",      ()->{return null;}), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiPauseScreenButton(screen, new UiTxt("About"           ), "about",            ()->{return null;}), Layout.BORDER_HEIGHT);
        }
        leftSidebar.setSize((int)(width * leftSidebarWidth), height); //TODO move to .layoutWidgets... though this mixin doesn't have that
        leftSidebar.setPosition(0, 0); //TODO move to .layoutWidgets... though this mixin doesn't have that
        leftSidebar.relayout(); //TODO move to .layoutWidgets... though this mixin doesn't have that
        addRenderableWidget(leftSidebar);


        //FIXME add a BIG disclaimer to "gameplay tweaks" screen that says it changes game mechanics
        //FIXME anything that changes game mechanics for anything that isn't the creative player is in there (write this too)
        //FIXME move no particles to alternative texture maybe?
        //FIXME move visible block overlays to alternative texture maybe?

        // Julia set
        final UiButton juliaScreenButton = new UiPauseScreenButton(screen, new UiTxt("??"), null, JuliaSetScreen::new, '\0');
        addRenderableWidget(juliaScreenButton);
        juliaScreenButton.setSize(BUTTON_HEIGHT, BUTTON_HEIGHT);
        juliaScreenButton.setX(width  - BUTTON_HEIGHT - BUTTON_MARGIN);
        juliaScreenButton.setY(height - BUTTON_HEIGHT - BUTTON_MARGIN);
    }








    @Inject(method = "extractRenderState", at = @At("TAIL"), cancellable = false, require = 1)
	public void eb$extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;




        // Draw player model
        if(ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.PLAYER_MODEL_IN_PAUSE_SCREEN)) {

            // Calculate dimensions and position
            int modelScale = 64;
            int boxSize = Math.max(width, height);
            int heightDiff = boxSize - clusterSizeY;
            int widthDiff = boxSize - clusterSizeX;
            int x0 = clusterRight - widthDiff / 2 + BUTTON_MARGIN;
            int x1 = x0 + boxSize;
            int y0 = clusterTop - heightDiff / 2;
            int y1 = y0 + boxSize;

            // Get mannequin
            final @Nullable PlayerMannequin model = PlayerMannequin.getMannequin();
            if(model != null) {
                InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, x0, y0, x1, y1, modelScale, 0.0f, mouseX, mouseY, model);
            }

            // Calculate play time
            final long ms = MinecraftUtils.getPlaytimeMs();
            final long hours   = TimeUnit.MILLISECONDS.toHours(ms);
            final long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
            final long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;

            // Calculate text dimensions and position
            final UiTxt playerName = new UiTxt(String.format("%s", player.getGameProfile().name()),             Fonts.ui.regular, Layout.HEADER_SCALE);
            final UiTxt playTime   = new UiTxt(String.format("Playtime: %dh %dm %ds", hours, minutes, seconds), Fonts.ui.light);
            int textCenterX = (x0 + x1) / 2;
            int nameY = clusterTop - 48;
            int titleY = nameY + playerName.getScaledFont().getLineHeight() + 2;

            // Draw player name an title
            RenderingUtils.extractTxt(graphics, playerName, textCenterX,  nameY, 0xFFFFC200, TextAlignment.CENTER_ANCHORED, 0, true);
            RenderingUtils.extractTxt(graphics,   playTime, textCenterX, titleY, 0xFFDDDDDD, TextAlignment.CENTER_ANCHORED, 0, true);
        }
    }
}




//TODO
//TODO
//TODO
//TODO
//TODO
//TODO
//TODO
//TODO
//TODO
//TODO add a placeholder "coming soon" overlay to more complex features
//TODO also add this to the reamde file
//TODO release a beta version without these features
//TODO full release will contain most of the main features and all the fixes