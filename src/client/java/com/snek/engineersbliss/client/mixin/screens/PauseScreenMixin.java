package com.snek.engineersbliss.client.mixin.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.InputConstants;
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
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.screens.rendering.RenderingScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;




@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    private static final int BUTTON_HEIGHT = Layout.BUTTON_HEIGHT;
    private static final int BUTTON_MARGIN = Layout.BORDER_HEIGHT;
    private static final int BUTTON_SPACING = BUTTON_HEIGHT + BUTTON_MARGIN;

    private static Button blockPropertiesButton;
    private static Button groupsButton;
    private static Button containerToolsButton;
    private static Button gameplayTweaksButton;
    private static Button creativeTweaksButton;

    private static Button renderingButton;
    private static Button overlaysButton;
    private static Button altTexturesButton;
    private static Button mufflerButton;
    private static Button actionHistory;


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
    private static UiWidgetList rightSidebar;
    private static final float leftSidebarWidth = 0.25f;
    private static final float rightSidebarWidth = 0.25f;




    protected PauseScreenMixin(final Component title) {
        super(title);
    }





    @Override
    public boolean keyPressed(final KeyEvent event) {
        if(event.key() == InputConstants.KEY_R) {
            renderingButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_O) {
            overlaysButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_G) {
            groupsButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_T) {
            altTexturesButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_P) {
            blockPropertiesButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_M) {
            mufflerButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_C) {
            containerToolsButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_X) {
            gameplayTweaksButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_Y) {
            creativeTweaksButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_U) {
            actionHistory.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        return super.keyPressed(event);
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
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString).getBold(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            // Rendering
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Rendering"), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[R] Render filter",    RenderingScreen  ::new), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[O] Overlays",         OverlaysScreen   ::new), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[T] Alt textures",     AltTexturesScreen::new), Layout.BORDER_HEIGHT);

            // Tools
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Tools"), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[U] Action history",   RenderingScreen::new), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[P] Block Properties", RenderingScreen::new), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[G] Block Groups",     RenderingScreen::new), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[C] Container tools",  RenderingScreen::new), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[I] Custom items",     RenderingScreen::new), Layout.BORDER_HEIGHT);

            // QoL
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("QoL"), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[X] Gameplay tweaks",  RenderingScreen::new), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(eb$createButton("[M] Sound muffler",    RenderingScreen::new), Layout.BORDER_HEIGHT);
        }
        addRenderableWidget(leftSidebar);


        //FIXME add a BIG disclaimer to "gameplay tweaks" screen that says it changes game mechanics
        //FIXME anything that changes game mechanics for anything that isn't the creative player is in there (write this too)
        //FIXME move no particles to alternative texture maybe?
        //FIXME move visible block overlays to alternative texture maybe?


        eb$addButton("??", JuliaSetScreen::new, width - BUTTON_HEIGHT - BUTTON_MARGIN, height - BUTTON_HEIGHT - BUTTON_MARGIN, BUTTON_HEIGHT);
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
        int nameY = clusterTop - 32;
        int titleY = nameY + font.lineHeight + 2;


        // Calculate play time
        final long ms = MinecraftUtils.getPlaytimeMs();
        final long hours   = TimeUnit.MILLISECONDS.toHours(ms);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;
        String playtime = String.format("%dh %dm %ds", hours, minutes, seconds);


        // Draw player name an title
        final Component playerName = new UiTxt(String.format(" %s ", player.getGameProfile().name())).getBold();
        final Component playTime   = new UiTxt(String.format("Playtime: %s", playtime)).get();
        graphics.centeredText(font, playerName, textCenterX,  nameY, 0xFFFFC200);
        graphics.centeredText(font, playTime,   textCenterX, titleY, 0xFFDDDDDD);
    }








    private Button eb$addButton(final String label, final Supplier<Screen> screenFactory, final int x, final int y, final int width) {
        final UiButton r = new UiButton(x, y, width, BUTTON_HEIGHT, new UiTxt(label), b -> {
            minecraft.setScreen(screenFactory.get());
            b.setFocused(false);
        });
        return this.addRenderableWidget(r);
    } //TODO remove if not used



    private Button eb$createButton(final String label, final Supplier<Screen> screenFactory) {
        final UiButton r = new UiButton(50, 50, 50, BUTTON_HEIGHT, new UiTxt(label), b -> {
            minecraft.setScreen(screenFactory.get());
            b.setFocused(false);
        });
        return r;
        // return this.addRenderableWidget(r);
    }
}