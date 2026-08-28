package com.snek.engineersbliss.client.ui.widgets.base;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.utils.Txt;
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;








public abstract class __base_UiLayoutElm extends AbstractWidget {

    // Input handling
    private boolean dragged;


    // Screen reference
    private final Screen screen;
    public Screen getScreen() { return screen; }
    public boolean isGuiScaleTransitioning() {
        return (screen instanceof @NotNull __base_UiScreen uiScreen) && uiScreen.isGuiScaleTransitioning();
    }


    // Relayout handling
    private static boolean relayoutDisabled = false;
    public static void    disableRelayout() { relayoutDisabled = true;  }
    public static void     enableRelayout() { relayoutDisabled = false; }
    public static boolean isRelayoutDisabled() { return relayoutDisabled; }




    protected __base_UiLayoutElm(final Screen screen) {
        super(50, 50, 50, 50, new Txt().get());
        this.dragged = false;
        this.screen = screen;
    }
    public abstract @NotNull List<?> children();








    public abstract void relayoutSelf();

    public void relayoutContent() {
        if(!isRelayoutDisabled()) for(final var e : children()) {
            if(e instanceof final @NotNull __base_UiLayoutElm w) {
                w.relayout();
            }
        }
    }

    public void relayout() {
        if(!isRelayoutDisabled()) {
            relayoutSelf();
            relayoutContent();
        }
    }








    // Forbid vanilla setMessage() in favor of setLabel()
    //! Though, setLabel is only available in __base_UiWidget. __base_UiLayoutElm still needs to suppress vanilla's message stuff.
    //! A layout element isn't even supposed to have a message in the first place,
    //! but extending AbstractWidget is way easier than implementing Vanilla's LayoutElement directly, and it also helps with compatibility.
    @Override
    public void setMessage(final Component message) {
        throw new UnsupportedOperationException("Use .setLabel(label) instead.");
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        boolean result = super.mouseClicked(event, doubled);
        dragged = true;
        return result;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        dragged = false;
        return super.mouseReleased(event);
    }

    public boolean isBeingDragged() {
        return dragged;
    }

    public boolean isHoveredOrBeingDragged() {
        return isBeingDragged() || isHovered();
    }


    //! Vanilla's hovering system checks for scissors. Unlike clicks, which don't do that, for whatever reason.
    //! Scissors always use screen coordinates because Minecraft only ever manages 1 coordinate space.
    //! They end up reporting an incorrect boundary when the custom GUI Scale doesn't match Vanilla's, making hover detection very unrealiable.
    //! This override fixes that by changing isHovered's behaviour for widgets that are children of __base_UiScreen
    //! (the only screen that can use custom scale), making it convert from screen to virtual coordinates before checking boundaries.

    @Override
    public boolean isHovered() {
        if(!isActive()) return false;
        if(screen instanceof @NotNull __base_UiScreen s) {
            return !(
                s.getMirrorHoverMouseX() <  getX()      ||
                s.getMirrorHoverMouseX() >= getRight()  ||
                s.getMirrorHoverMouseY() <  getY()      ||
                s.getMirrorHoverMouseY() >= getBottom() ||
                s.getMirrorHoverGraphics() == null      ||
                !s.getMirrorHoverGraphics().containsPointInScissor(
                    s.getMirrorHoverScreenMouseX(),
                    s.getMirrorHoverScreenMouseY()
                )
            );
        }
        else {
            return super.isHovered();
        }
    }








    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // Empty by default
    }

    @Override
    protected void updateWidgetNarration(final NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(final FocusNavigationEvent navigationEvent) {
        return null; // Empty. Tab focusing is disabled.
    }
}
