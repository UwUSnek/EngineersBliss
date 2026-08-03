package com.snek.engineersbliss.client.mixin.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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
import net.minecraft.resources.Identifier;




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
        clusterCenterX = eb$getButtonClusterCenterX().getAsInt();
        clusterCenterY = eb$getButtonClusterCenterY().getAsInt();
        clusterSizeX   = eb$getButtonClusterWidth().getAsInt();
        clusterSizeY   = eb$getButtonClusterHeight().getAsInt();
        clusterLeft    = clusterCenterX - clusterSizeX / 2;
        clusterRight   = clusterCenterX + clusterSizeX / 2;
        clusterTop     = clusterCenterY - clusterSizeY / 2;
        clusterBottom  = clusterCenterY + clusterSizeY / 2;



        leftSidebar = new UiWidgetList(this, (int)(width * leftSidebarWidth), height, 0, 0, BUTTON_HEIGHT); {
            final UiTxt titleText   = new UiTxt(EngineerSBliss.MOD_NAME, Fonts.ui.light, 2f);
            final UiTxt versionText = new UiTxt(String.format("v%s", EngineerSBlissClient.getModVersion()), Fonts.ui.regular, 1f);
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, titleText,   TextAlignment.LEFT, Layout.fgColor), titleText.getScaledFont().getLineHeight());
            leftSidebar.addWidget(new UiTextWidget(this, versionText, TextAlignment.LEFT, Layout.fgColor), versionText.getScaledFont().getLineHeight());

            // Rendering
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Rendering", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Render filter",    RenderingScreen  ::new, 'R', "pause_screen/render_filter"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Overlays",         OverlaysScreen   ::new, 'O', "pause_screen/overlays"),      Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Alt textures",     AltTexturesScreen::new, 'T', "pause_screen/alt_textures"),  Layout.BORDER_HEIGHT);

            // Tools
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Tools", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor),      Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Action history",   ()->{return null;}, 'U', "pause_screen/action_history"),   Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Version Control",  ()->{return null;}, 'V', "pause_screen/version_control"),  Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Block Properties", ()->{return null;}, 'P', "pause_screen/block_properties"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Block Groups",     ()->{return null;}, 'G', "pause_screen/block_groups"),     Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Container tools",  ()->{return null;}, 'C', "pause_screen/container_tools"),  Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Custom items",     ()->{return null;}, 'I', "pause_screen/custom_items"),     Layout.BORDER_HEIGHT);

            // QoL
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("QoL", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Creative tweaks",  CreativeTweaksScreen::new, 'Y', "pause_screen/creative_tweaks"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Gameplay tweaks",  ()->{return null;},      'X', "pause_screen/gameplay_tweaks"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Sound muffler",    ()->{return null;},      'M', "pause_screen/sound_muffler"),   Layout.BORDER_HEIGHT);

            // Preferences
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Preferences", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Settings",         SettingsScreen::new, 'S', "pause_screen/settings"),         Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Keybinds",         ()->{return null;}, 'K', "pause_screen/keybinds"),         Layout.BORDER_HEIGHT);

            // Info
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Info", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Render stats", ()->{return null;}, '\0', "pause_screen/render_stats"),  Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("World stats",  ()->{return null;}, '\0', "pause_screen/world_stats"),   Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("About",        ()->{return null;}, '\0', "pause_screen/about"),         Layout.BORDER_HEIGHT);
        }
        addRenderableWidget(leftSidebar);


        //FIXME add a BIG disclaimer to "gameplay tweaks" screen that says it changes game mechanics
        //FIXME anything that changes game mechanics for anything that isn't the creative player is in there (write this too)
        //FIXME move no particles to alternative texture maybe?
        //FIXME move visible block overlays to alternative texture maybe?

        // Julia set
        final Button juliaScreenButton = eb$createButton("??", JuliaSetScreen::new, '\0', null);
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




    //TODO remove. use the new system or something, idk
    private UiButton eb$createButton(final String label, final Supplier<Screen> screenFactory, char keybind, final @Nullable String spriteName) {
        final Identifier bgSpriteId = spriteName == null ? null : Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, spriteName);
        return new UiButton(this, new UiTxt(label), b -> {
            minecraft.setScreen(screenFactory.get());
            b.setFocused(false);
        }, keybind, TextAlignment.LEFT).withSpriteBg(bgSpriteId, 4f, BUTTON_HEIGHT);
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