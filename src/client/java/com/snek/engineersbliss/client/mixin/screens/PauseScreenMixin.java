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
import com.snek.engineersbliss.client.screens.alt_textures.AltTexturesScreen;
import com.snek.engineersbliss.client.screens.creative_tweaks.CreativeTweaksScreen;
import com.snek.engineersbliss.client.screens.julia_set.JuliaSetScreen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.screens.parts.PlayerMannequin;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiButton;
import com.snek.engineersbliss.client.screens.parts.UiSpacer;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.screens.rendering.RenderingScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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
    private static final int BUTTON_SPACING = BUTTON_HEIGHT + BUTTON_MARGIN;


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
    private static final float leftSidebarWidth = 0.25f;




    protected PauseScreenMixin(final Component title) {
        super(title);
    }






    @Override
    public boolean keyPressed(final KeyEvent event) {
        boolean r = false;
        for(final var c : children()) r = r || c.keyPressed(event);
        return r;
    }
    @Override
    public boolean charTyped(CharacterEvent event) {
        boolean r = false;
        for(final var c : children()) r = r || c.charTyped(event);
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



        leftSidebar = new UiWidgetList((int)(width * leftSidebarWidth), height, 0, 0, BUTTON_HEIGHT); {
            final String titleString = String.format("%s v%s", EngineerSBliss.MOD_NAME, EngineerSBlissClient.getModVersion());
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            // Rendering
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Rendering", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Render filter",    RenderingScreen  ::new, 'R', "pause_screen/test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Overlays",         OverlaysScreen   ::new, 'O', "pause_screen/test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Alt textures",     AltTexturesScreen::new, 'T', "pause_screen/test"), Layout.BORDER_HEIGHT);

            // Tools
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Tools", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Action history",   RenderingScreen::new, 'U', "pause_screen/test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Block Properties", RenderingScreen::new, 'P', "pause_screen/test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Block Groups",     RenderingScreen::new, 'G', "pause_screen/test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Container tools",  RenderingScreen::new, 'C', "pause_screen/test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Custom items",     RenderingScreen::new, 'I', "pause_screen/test"), Layout.BORDER_HEIGHT);

            // QoL
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("QoL", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Creative tweaks",  CreativeTweaksScreen::new, 'Y', "pause_screen/test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Gameplay tweaks",  RenderingScreen::new,      'X', "pause_screen/test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("Sound muffler",    RenderingScreen::new,      'M', "pause_screen/test"), Layout.BORDER_HEIGHT);
        }
        addRenderableWidget(leftSidebar);


        //FIXME add a BIG disclaimer to "gameplay tweaks" screen that says it changes game mechanics
        //FIXME anything that changes game mechanics for anything that isn't the creative player is in there (write this too)
        //FIXME move no particles to alternative texture maybe?
        //FIXME move visible block overlays to alternative texture maybe?


        // About section
        final Button aboutButton = eb$createButton("About", RenderingScreen::new, '\0', "pause_screen/test");
        addRenderableWidget(aboutButton);
        aboutButton.setSize((int)(width * leftSidebarWidth), BUTTON_HEIGHT);
        aboutButton.setX(0);
        aboutButton.setY(height - BUTTON_HEIGHT - BUTTON_MARGIN);


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


        // Calculate dimensions and position
        int modelScale = 64;
        int boxSize = Math.max(width, height);
        int heightDiff = boxSize - clusterSizeY;
        int widthDiff = boxSize - clusterSizeX;
        int x0 = clusterRight - widthDiff / 2 + BUTTON_MARGIN;
        int x1 = x0 + boxSize;
        int y0 = clusterTop - heightDiff / 2;
        int y1 = y0 + boxSize;


        // Draw player model
        PlayerMannequin model = PlayerMannequin.getMannequin();
        InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, x0, y0, x1, y1, modelScale, 0.0F, mouseX, mouseY, model);


        // Calculate text dimensions and position
        final Font font = Minecraft.getInstance().font;
        int textCenterX = (x0 + x1) / 2;
        int nameY = clusterTop - 48;
        int titleY = nameY + (int)(font.lineHeight * Layout.HEADER_SCALE) + 2;


        // Calculate play time
        final long ms = MinecraftUtils.getPlaytimeMs();
        final long hours   = TimeUnit.MILLISECONDS.toHours(ms);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;


        // Draw player name an title
        final UiTxt playerName = new UiTxt(String.format("> %s <", player.getGameProfile().name()), Layout.HEADER_SCALE).withBoldFont();
        final UiTxt playTime   = new UiTxt(String.format("Playtime: %dh %dm %ds", hours, minutes, seconds));
        RenderingUtils.extractTxt(graphics, playerName, textCenterX,  nameY, 0xFFFFC200, TextAlignment.CENTER, true);
        RenderingUtils.extractTxt(graphics,   playTime, textCenterX, titleY, 0xFFDDDDDD, TextAlignment.CENTER, true);
    }





    private UiButton eb$createButton(final String label, final Supplier<Screen> screenFactory, char keybind, final @Nullable String spriteName) {
        final Identifier bgSpriteId = spriteName == null ? null : Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, spriteName);
        return new UiButton(new UiTxt(label), b -> {
            minecraft.setScreen(screenFactory.get());
            b.setFocused(false);
        }, keybind).withSpriteBg(bgSpriteId, BUTTON_HEIGHT * 4, BUTTON_HEIGHT);
    }
}