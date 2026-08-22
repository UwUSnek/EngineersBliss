package com.snek.engineersbliss.client.ui.base;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedFloat;
import com.snek.engineersbliss.client.ui.widgets.base.UiWidgetBase;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiButton;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;

















//BUG n.00x and n.50x scales work perfectly
//BUG n.25x and n.75x scales duplicate pixel rows (and probably columns)
//BUG The factor isn't the issue, as it's always calculated perfectly and float precision just isnt so bad to explain multiple duplicated pixel rows per text line


/**
 * The base class for any screen of the Engineer's Bliss mod.
 * Screens come with +/- GUI Scale keybinds, Tab keybind to hide the screen, and a blurred background.
 * By default, all screens pause the game.
 */
public abstract class __base_UiScreen extends Screen {
    private int realGuiScale = 1;  // The window's actual current scale, refreshed each resize
    protected final AnimatedFloat animatedGuiScale;
    public boolean isGuiScaleTransitioning() { return !animatedGuiScale.isIdle(); }

    public static final int BORDER_WIDTH  = Layout.BORDER_WIDTH;
    public static final int BORDER_HEIGHT = Layout.BORDER_HEIGHT;
    public static final int LIST_TOP      = Layout.LIST_TOP;
    public static final int BUTTON_HEIGHT = Layout.BUTTON_HEIGHT;

    private boolean needsRelayout;
    private boolean needsRebuild;




    protected __base_UiScreen() {
        super(new UiTxt().get());
        final float guiScale = SettingsServerFeatureSet.GUI_SCALE.getValues().get(ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.GUI_SCALE));
        this.animatedGuiScale = new AnimatedFloat(guiScale, Layout.guiScaleTransitionDuration);
        this.needsRebuild = true;
        this.needsRelayout = false;
    }








    @Override
    public void resize(final int width, final int height) {
        final float newGuiScale = SettingsServerFeatureSet.GUI_SCALE.getValues().get(ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.GUI_SCALE)); //TODO replace all instances with a utility method in ClientFeatureSync
        animatedGuiScale.startNewTransition(newGuiScale);
        maybeFlagResize();
    }


    /**
     * Sets the resize flags, but only if needed.
     * Flags the cheaper layoutWidgets() during transitions to improve performance.
     * NOTICE: The actual resize operation is done by the rendering loop when needed.
     */
    protected void maybeFlagResize() {
        final @NotNull Minecraft mc = Minecraft.getInstance();
        realGuiScale = mc.getWindow().getGuiScale();

        int fbWidth  = mc.getWindow().getScreenWidth();
        int fbHeight = mc.getWindow().getScreenHeight();
        int fixedWidth  = (int)Math.floor(fbWidth  / animatedGuiScale.compute());
        int fixedHeight = (int)Math.floor(fbHeight / animatedGuiScale.compute());

        boolean transitioning = isGuiScaleTransitioning();
        if(this.width != fixedWidth || this.height != fixedHeight) {
            this.width  = fixedWidth;
            this.height = fixedHeight;

            if(transitioning) {
                needsRelayout = true;
            }
            else {
                needsRebuild = true;
            }
        }
    }



    // Converts an mouse coord from GuiScale-dependant coords to the fake screen size.
    private double fx(double v) {
        return v * (realGuiScale / animatedGuiScale.compute());
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
        return super.mouseClicked(new MouseButtonEvent(fx(e.x()), fx(e.y()), new MouseButtonInfo(e.button(), e.modifiers())), doubleClick);
    }
    @Override
    public boolean mouseReleased(MouseButtonEvent e) {
        return super.mouseReleased(new MouseButtonEvent(fx(e.x()), fx(e.y()), new MouseButtonInfo(e.button(), e.modifiers())));
    }
    @Override
    public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
        return super.mouseDragged(new MouseButtonEvent(fx(e.x()), fx(e.y()), new MouseButtonInfo(e.button(), e.modifiers())), fx(dx), fx(dy));
    }
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(fx(mouseX), fx(mouseY));
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double sx, double sy) {
        return super.mouseScrolled(fx(mouseX), fx(mouseY), sx, sy);
    }








    protected boolean tabPressed = false;
    @Override
    public boolean keyPressed(final KeyEvent event) {
        switch(event.key()) {
            case InputConstants.KEY_ESCAPE: {
                onClose();
                return true;
            }
            case InputConstants.KEY_TAB: {
                tabPressed = true;
                return true;
            }
            case InputConstants.KEY_ADD: {
                final int newScaleIndex = ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.GUI_SCALE) + 1;
                final int clampedNewScaleIndex = Math.clamp(newScaleIndex, 0, SettingsServerFeatureSet.GUI_SCALE.getValues().size() - 1); //TODO replace with utility method in ClientFeatureSync
                ClientFeatureSync.setFeature(SettingsServerFeatureSet.GUI_SCALE, clampedNewScaleIndex);
                resize(0, 0);
                return true;
            }
            case GLFW.GLFW_KEY_KP_SUBTRACT: {
                final int newScaleIndex = ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.GUI_SCALE) - 1;
                final int clampedNewScaleIndex = Math.clamp(newScaleIndex, 0, SettingsServerFeatureSet.GUI_SCALE.getValues().size() - 1);
                ClientFeatureSync.setFeature(SettingsServerFeatureSet.GUI_SCALE, clampedNewScaleIndex);
                resize(0, 0);
                return true;
            }
            default: {
                //! Don't call super.keyPressed.
                //! Vanilla has custom handling for arrow keys which breaks all sorts of stuff.
                boolean r = false;
                for(final @NotNull GuiEventListener e : children()) {
                    if(e.keyPressed(event)) r = true;
                }
                return r;
            }
        }
    }


    @Override
    public boolean keyReleased(final KeyEvent event) {
        switch(event.key()) {
            case InputConstants.KEY_TAB: {
                tabPressed = false;
                return true;
            }
            default: {
                //! No super.keyReleased call.
                //! super.keyPressed is never called in the first place. This simply mirrors that behaviour.
                boolean r = false;
                for(final @NotNull GuiEventListener e : children()) {
                    if(e.keyReleased(event)) r = true;
                }
                return r;
            }
        }
    }




    /**
     * Custom lightweight layout update function.
     * ! For some dumb reason, Vanilla's repositionElements() is just an alias for rebuildWidgets() like- WHY.
     *
     * This function is called after the first init call, during resize animations, and after widget rebuilds (equivalent to destroy+init).
     * Subclasses are expected to include all of the widget positioning logic in this function and call super.layoutWidgets() at the end.
     */
    protected void layoutWidgets() {
        for(final var e : children()) {
            if(e instanceof final @NotNull UiWidgetBase w) {
                w.layoutWidgets();
            }
        }
    }


    @Override
    public final void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;
        maybeFlagResize(); //! Check size mismatch every frame to keep the UI synched. This also avoids complex update logic.
        if(needsRebuild) {
            rebuildWidgets();
            layoutWidgets();
            needsRebuild = false;
            needsRelayout = false;
        }
        else if(needsRelayout) {
            layoutWidgets();
            needsRelayout = false;
        }

        // Compensate the visual scale so pixel size stays constant regardless of GUI Scale.
        float factor = animatedGuiScale.compute() / realGuiScale;
        graphics.pose().pushMatrix();
        graphics.pose().scale(factor, factor);
        _extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.pose().popMatrix();
    }


    public void _extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        int adjMouseX = (int)fx(mouseX);
        int adjMouseY = (int)fx(mouseY);

        //! Stop other widgets from updating hover state while dragging.
        //! This is done by calling the superclass's extractRenderState with a fake invalid mouse position that no widget can cover.
        //! This stops the cursor from highlighting other stuff while dragging, making controls feel tidier.
        if(isDragging()) super.extractRenderState(graphics, -1,        -1,        delta);
        else             super.extractRenderState(graphics, adjMouseX, adjMouseY, delta);
    }


    @Override
    public void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
        if(!tabPressed) {
            graphics.blurBeforeThisStratum();
        }
    }


    @Override
	public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        this.extractBlurredBackground(graphics);
    }








    @Override
    public void onClose() {

        // Close screen and go back to game
        this.minecraft.setScreen(null);
    }


    @Override
    public boolean isPauseScreen() {
        return ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.PAUSE_GAME_IN_MOD_SCREENS);
    }


    @Override
    public boolean isAllowedInPortal() {
        return true;
    }
}
