package com.snek.engineersbliss.client.ui.base;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedFloat;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
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
    public static final int BORDER_WIDTH  = Layout.BORDER_WIDTH;
    public static final int BORDER_HEIGHT = Layout.BORDER_HEIGHT;
    public static final int LIST_TOP      = Layout.LIST_TOP;
    public static final int BUTTON_HEIGHT = Layout.BUTTON_HEIGHT;


    // Virtual gui scale & sclae animation
    private int realGuiScale = 1;  // The window's actual current scale, refreshed each resize
    protected final AnimatedFloat animatedGuiScale;
    public AnimatedFloat getAnimatedGuiScale() { return animatedGuiScale; }
    public boolean isGuiScaleTransitioning() { return !animatedGuiScale.isIdle(); }


    // Relayout/rebuild flags
    private boolean needsRelayout;
    private boolean needsRebuild;


    // Element tracking
    private @Nullable GuiEventListener hoveredElm;
    private @Nullable GuiEventListener selectedElm;
    private @Nullable GuiEventListener focusedElm;
    private @Nullable GuiEventListener draggedElm;
    public  @Nullable GuiEventListener getHoveredElm()  { return hoveredElm; }
    public  @Nullable GuiEventListener getSelectedElm() { return selectedElm; }
    public  @Nullable GuiEventListener getFocusedElm()  { return focusedElm; }
    public  @Nullable GuiEventListener getDraggedElm()  { return draggedElm; }
    public  @Nullable GuiEventListener getHoveredOrDraggedElm()  {
        return hoveredElm == null ? draggedElm : hoveredElm;
    }


    //! Mirror hover tracking, needed for the vanilla scissor fix.
    //! See __base_UiWidget's isHovered().
    private int mirrorHoverMouseX = Integer.MIN_VALUE;
    private int mirrorHoverMouseY = Integer.MIN_VALUE;
    private int mirrorHoverScreenMouseX = Integer.MIN_VALUE;
    private int mirrorHoverScreenMouseY = Integer.MIN_VALUE;
    private @Nullable GuiGraphicsExtractor mirrorHoverGraphics;
    public int getMirrorHoverMouseX() { return mirrorHoverMouseX; }
    public int getMirrorHoverMouseY() { return mirrorHoverMouseY; }
    public int getMirrorHoverScreenMouseX() { return mirrorHoverScreenMouseX; }
    public int getMirrorHoverScreenMouseY() { return mirrorHoverScreenMouseY; }
    public @Nullable GuiGraphicsExtractor getMirrorHoverGraphics() { return mirrorHoverGraphics; }




    protected __base_UiScreen() {
        super(new UiTxt().get());
        this.animatedGuiScale = new AnimatedFloat(SettingsFeatureHandler.getCurrentGuiScale(), Layout.guiScaleTransitionDuration);
        this.needsRebuild = true;
        this.needsRelayout = false;
    }
    private void updateMirrorHoverState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int screenMouseX, int screenMouseY) {
        mirrorHoverGraphics = graphics;
        mirrorHoverMouseX = mouseX;
        mirrorHoverMouseY = mouseY;
        mirrorHoverScreenMouseX = screenMouseX;
        mirrorHoverScreenMouseY = screenMouseY;
    }






    @Override
    public void resize(final int width, final int height) {
        animatedGuiScale.startNewTransition(SettingsFeatureHandler.getCurrentGuiScale());
        maybeFlagResize();
    }


    /**
     * Sets the resize flags, but only if needed.
     * Flags the cheaper relayoutWidgets() during transitions to improve performance.
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


    private @Nullable GuiEventListener computeHoveredElm(final double mouseX, final double mouseY) {
        GuiEventListener current = this;
        GuiEventListener result  = null;
        while(current instanceof ContainerEventHandler containerCurrent) {
            GuiEventListener next = null;
            for(final GuiEventListener child : containerCurrent.children()) {
                if(child.isMouseOver(mouseX, mouseY)) { next = child; break; }
            }
            if(next == null) break;
            result  = next;
            current = next;
        }
        return result;
    }

    // Converts a mouse coord from GuiScale-dependant coords to virtual screen coords
    private double fx(double v) {
        return v * (realGuiScale / animatedGuiScale.compute());
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
        final MouseButtonEvent fixed = new MouseButtonEvent(fx(e.x()), fx(e.y()), new MouseButtonInfo(e.button(), e.modifiers()));
        final GuiEventListener hit = computeHoveredElm(fixed.x(), fixed.y());
        if(hit != null) draggedElm = hit;
        return super.mouseClicked(fixed, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent e) {
        final boolean r = super.mouseReleased(new MouseButtonEvent(fx(e.x()), fx(e.y()), new MouseButtonInfo(e.button(), e.modifiers())));
        draggedElm = null;
        return r;
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
            case GLFW.GLFW_KEY_ESCAPE: {
                onClose();
                return true;
            }
            case GLFW.GLFW_KEY_TAB: {
                tabPressed = true;
                return true;
            }
            case GLFW.GLFW_KEY_KP_ADD: {
                final int newScaleIndex = SettingsFeatureHandler.getCurrentGuiScaleIndex() + 1;
                final int clampedNewScaleIndex = Math.clamp(newScaleIndex, 0, SettingsFeatureHandler.getGuiScalesNumber() - 1);
                ClientFeatureSync.setFeature(SettingsServerFeatureSet.GUI_SCALE, clampedNewScaleIndex);
                resize(0, 0);
                return true;
            }
            case GLFW.GLFW_KEY_KP_SUBTRACT: {
                final int newScaleIndex = SettingsFeatureHandler.getCurrentGuiScaleIndex() - 1;
                final int clampedNewScaleIndex = Math.clamp(newScaleIndex, 0, SettingsFeatureHandler.getGuiScalesNumber() - 1);
                ClientFeatureSync.setFeature(SettingsServerFeatureSet.GUI_SCALE, clampedNewScaleIndex);
                resize(0, 0);
                return true;
            }
            case GLFW.GLFW_KEY_KP_MULTIPLY: {
                final boolean newDebugOverlays = !ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.DEBUG_OVERLAYS);
                ClientFeatureSync.setFeature(SettingsServerFeatureSet.DEBUG_OVERLAYS, newDebugOverlays);
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
     * This function is called after the first init call, during resize animations, and after widget rebuilds (equivalent to destroy+init).
     * Subclasses are expected to include all of the widget positioning logic in this function and call super.layoutContent() at the very beginning.
     */
    protected abstract void relayoutSelf();


    protected void relayoutContent() {
        for(final var e : List.copyOf(children())) { //! Iterate snapshot to avoid concurrent modification issues
            if(e instanceof final @NotNull __base_UiLayoutElm w) {
                w.relayout();
            }
        }
    }

    /**
     * Custom lightweight layout update function.
     * ! For some dumb reason, Vanilla's repositionElements() is just an alias for rebuildWidgets() like- WHY.
     */
    protected void relayout() {
        relayoutSelf();
        relayoutContent();
    }




    @Override
    public final void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;

        // Check for resizes and rebuild/layout widgets if needed
        maybeFlagResize(); //! Check size mismatch every frame to keep the UI synched. This also avoids complex update logic.
        if(needsRebuild) {
            rebuildWidgets();
            relayout();
            needsRebuild = false;
            needsRelayout = false;
        }
        else if(needsRelayout) {
            relayout();
            needsRelayout = false;
        }

        // Compensate the visual scale so pixel size stays constant regardless of GUI Scale, then draw everything.
        float factor = animatedGuiScale.compute() / realGuiScale;
        graphics.pose().pushMatrix();
        graphics.pose().scale(factor, factor);
        _extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.pose().popMatrix();
    }


    public void _extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        int adjMouseX = (int)fx(mouseX);
        int adjMouseY = (int)fx(mouseY);


        // Update hovered element
        hoveredElm = (draggedElm != null) ? draggedElm : computeHoveredElm(adjMouseX, adjMouseY);


        //! Mirror hover state must be global and identical for all widgets.
        //! Setting a global it once for each widget makes widgets reading it from outside the render loop go out of sync,
        //! while keeping a separate cached value for each individual widget is a maintainability nightmare.
        //! One global state for all widgets has the drawback of it reporting [not hovered] for the widget thats currently being dragged,
        //! but the widget itself can simply use isBeingHoveredOrDragged(), which checks for both.
        if(!isDragging()) {
            updateMirrorHoverState(graphics, adjMouseX, adjMouseY, mouseX, mouseY);
        }
        else {
            updateMirrorHoverState(graphics, -0xDEAD_BEEF, -0xDEAD_BEEF, -0xDEAD_BEEF, -0xDEAD_BEEF);
        }


        // Extract widgets
        for(final @NotNull GuiEventListener c : List.copyOf(children())) { //! Iterate snapshot to avoid concurrent modification issues
            if(c instanceof @NotNull Renderable r) {
                r.extractRenderState(graphics, adjMouseX, adjMouseY, delta);
            }
        }
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
