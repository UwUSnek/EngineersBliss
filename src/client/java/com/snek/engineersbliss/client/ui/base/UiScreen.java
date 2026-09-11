package com.snek.engineersbliss.client.ui.base;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedColor;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedFloat;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
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
public abstract class UiScreen extends Screen {

    // Texture cache
    private final List<TextureCache> textureCaches = new ArrayList<>();
    public void registerTextureCacheForClose(TextureCache cache) {
        textureCaches.add(cache);
    }


    // True Screen size & virtual gui scale
    private int vanillaGuiScale = 1;  // The window's actual current scale, refreshed each vanilla resize. //! Always int.
    protected final AnimatedFloat animatedGuiScale;
    private float lastGuiScale = -1;
    public float getGuiScale() { return animatedGuiScale.compute(); } //FIXME compute once per frame. keep frame number in a global. controlled by the screen
    public float getVanillaGuiScale() { return vanillaGuiScale; }


    // Relayout/rebuild flags
    private boolean needsRelayout;


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


    // Style
    private final AnimatedColor bgColor;




    protected UiScreen() {
        super(new UiTxt().get());
        this.animatedGuiScale = new AnimatedFloat(SettingsFeatureHandler.getCurrentGuiScale(), Layout.guiScaleTransitionDuration);
        this.needsRelayout = false;
        this.bgColor = new AnimatedColor(calcNewBgColor(), 1000);
    }
    private int calcNewBgColor() {
        final int bgOpacity = (int)(255f * SettingsServerFeatureSet.GUI_BACKGROUND_OPACITY.getValues().get(ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.GUI_BACKGROUND_OPACITY)));
        return Layout.bgColor & 0x00FFFFFF | (bgOpacity << 24);
    }
    protected void refreshBgColor() {
        bgColor.startNewTransition(calcNewBgColor());
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
        vanillaGuiScale = mc.getWindow().getGuiScale();

        // Retrieve current true dimensions and virtual scale
        float newScale = animatedGuiScale.compute();
        int newWidth  = mc.getWindow().getScreenWidth();    //! Vanilla updates these when the Vanilla GUI Scale is changed.
        int newHeight = mc.getWindow().getScreenHeight();   //! Vanilla updates these when the Vanilla GUI Scale is changed.

        // Update dimensions and relayout if the scale changed or the window was resized
        if(lastGuiScale != newScale || width != newWidth || height != newHeight) {
            lastGuiScale = newScale;
            width  = newWidth;
            height = newHeight;
            needsRelayout = true;
        }
    }


    private @Nullable GuiEventListener computeHoveredElm(final double mouseX, final double mouseY) {
        if(!isWindowActive()) return null;
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


    /**
     * Calculates the true position of the cursor by calling GLFW's functions directly.
     * ! This bypasses Minecraft Vanilla's mouse handling logic which can return stale values in specific conditions.
     * ! NOTICE: This returns proper cursor coords even while the window is not focused or visible. Use .isWindowActive() to check for that.
     * @return
     */
    public Vector2f calcTrueCursorPos() {
        final @NotNull Window window = Minecraft.getInstance().getWindow();
        double[] px = new double[1];
        double[] py = new double[1];
        GLFW.glfwGetCursorPos(window.handle(), px, py);
        return new Vector2f((float)px[0], (float)py[0]);
    }
    /**
     * Checks if the Minecraft window is currently visible, focused, and not iconified.
     */
    public boolean isWindowActive() {
        final @NotNull Window window = Minecraft.getInstance().getWindow();
        final long handle = window.handle();
        return
            GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_VISIBLE)   == GLFW.GLFW_TRUE  &&
            GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_FALSE &&
            GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FOCUSED)   == GLFW.GLFW_TRUE
        ;
    }


    @Override
    public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
        if(!isWindowActive()) return true;
        final @NotNull Vector2f fixedPos = calcTrueCursorPos();
        final MouseButtonEvent fixedEvent = new MouseButtonEvent(fixedPos.x, fixedPos.y, new MouseButtonInfo(e.button(), e.modifiers()));
        final GuiEventListener hit = computeHoveredElm(fixedPos.x, fixedPos.y);
        if(hit != null) draggedElm = hit;
        return super.mouseClicked(fixedEvent, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent e) {
        if(!isWindowActive()) return true;
        final @NotNull Vector2f fixedPos = calcTrueCursorPos();
        final MouseButtonEvent fixedEvent = new MouseButtonEvent(fixedPos.x, fixedPos.y, new MouseButtonInfo(e.button(), e.modifiers()));
        final boolean r = super.mouseReleased(fixedEvent);
        draggedElm = null;
        return r;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
        if(!isWindowActive()) return true;
        final @NotNull Vector2f fixedPos = calcTrueCursorPos();
        final MouseButtonEvent fixedEvent = new MouseButtonEvent(fixedPos.x, fixedPos.y, new MouseButtonInfo(e.button(), e.modifiers()));
        final int vanillaScale = Minecraft.getInstance().options.guiScale().get();
        return super.mouseDragged(fixedEvent, dx * vanillaScale, dy * vanillaScale);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if(!isWindowActive()) return;
        final @NotNull Vector2f fixedPos = calcTrueCursorPos();
        super.mouseMoved(fixedPos.x, fixedPos.y);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double sx, double sy) {
        if(!isWindowActive()) return true;
        final @NotNull Vector2f fixedPos = calcTrueCursorPos();
        return super.mouseScrolled(fixedPos.x, fixedPos.y, sx, sy);
    }








    protected boolean tabPressed = false;
    @Override
    public boolean keyPressed(final KeyEvent event) {
        if(!isWindowActive()) return true;
        switch(event.key()) {
            case GLFW.GLFW_KEY_ESCAPE: {
                if(shouldCloseOnEsc()) onClose();
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
            case GLFW.GLFW_KEY_PAGE_UP: { //TODO remove
                Minecraft.getInstance().options.guiScale().set(Minecraft.getInstance().options.guiScale().get() + 1);
                resize(0, 0);
                return true;
            }
            case GLFW.GLFW_KEY_PAGE_DOWN: { //TODO remove
                Minecraft.getInstance().options.guiScale().set(Minecraft.getInstance().options.guiScale().get() - 1);
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
        if(!isWindowActive()) return true;
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


    @Override
    public boolean charTyped(final CharacterEvent event) {
        if(!isWindowActive()) return true;
        boolean r = false;
        for(final @NotNull GuiEventListener e : children()) {
            if(e.charTyped(event)) r = true;
        }
        return r;
    }








    /**
     * This function is called after the first init call, during resize animations, and after widget rebuilds (equivalent to destroy+init).
     * Subclasses are expected to include all of the widget positioning logic in this function and call super.layoutContent() at the very beginning.
     */
    protected abstract void relayoutSelf();


    protected void relayoutContent() {
        for(final var e : children()) {
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

        // Check for resizes and relayout widgets if needed
        maybeFlagResize(); //! Check size mismatch every frame to keep the UI synched. This also avoids complex update logic.
        if(needsRelayout) {
            relayout();
            needsRelayout = false;
        }

        // Compensate the visual scale so pixel size stays constant regardless of GUI Scale, then draw everything. Compensate mouse coords too.
        final @NotNull Vector2f fixedPos = calcTrueCursorPos();
        float factor = 1f / vanillaGuiScale;
        graphics.pose().pushMatrix();
        graphics.pose().scale(factor, factor);
        extractRenderState(new UiGraphics(graphics, this), fixedPos.x, fixedPos.y, delta);
        graphics.pose().popMatrix();
    }


    /**
     * Custom render function that uses a UiGraphics instead of Vanill'as graphics extractor.
     */
    public void extractRenderState(final UiGraphics graphics, final float mouseX, final float mouseY, final float delta) {

        // Update hovered element
        hoveredElm = (draggedElm != null) ? draggedElm : computeHoveredElm(mouseX, mouseY);

        // Extract background
        extractBackground(graphics, mouseX, mouseY, delta);

        // Extract widgets
        for(final @NotNull GuiEventListener c : children()) {
            if(c instanceof @NotNull __base_UiWidget r) {
                r.extract(graphics, mouseX, mouseY, delta);
            }
        }
    }


    @Override
    public final void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
        // Empty. Suppress Vanilla background.
    }
    @Override
	public final void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        // Empty. Suppress Vanilla background.
    }
	public void extractBlurredBackground(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        graphics.blurBeforeThisStratum();
    }
	public void extractBackground(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        extractBlurredBackground(graphics, mouseX, mouseY, a);
        final int _bgColor = bgColor.compute();
        if((_bgColor & 0xFF000000) != 0) graphics.fill(0, 0, width, height, _bgColor);
    }








    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
        for(final @NotNull TextureCache textureCache : textureCaches) {
            textureCache.close();
        }
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
