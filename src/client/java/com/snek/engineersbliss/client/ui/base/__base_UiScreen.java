package com.snek.engineersbliss.client.ui.base;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiButton;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;









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



    protected __base_UiScreen() {
        super(new UiTxt().get());
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
                Minecraft client = Minecraft.getInstance();
                final OptionInstance<Integer> option = client.options.guiScale();
                option.set(option.get() + 1);
                client.resizeGui();
                return true;
            }
            case GLFW.GLFW_KEY_KP_SUBTRACT: {
                Minecraft client = Minecraft.getInstance();
                final OptionInstance<Integer> option = client.options.guiScale();
                option.set(option.get() - 1);
                client.resizeGui();
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







    //TODO remove. this is the old version, still used by RenderingScreen
    //TODO remove. this is the old version, still used by RenderingScreen
    protected UiButton addButton(final UiTxt label, final UiTxt details, final Consumer<UiButton> action, final int x, final int y, final int width) {
        final UiButton r = new UiButton(this, x, y, width, BUTTON_HEIGHT, label, b -> {
            action.accept(b);
            b.setFocused(false);
        }, TextAlignment.CENTER);
        r.setTooltip(Tooltip.create(details.get()));
        this.addRenderableWidget(r);
        return r;
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;

        //! Stop other widgets from updating hover state while dragging.
        //! This is done by calling the superclass's extractRenderState with a fake invalid mouse position that no widget can cover.
        //! This stops the cursor from highlighting other stuff while dragging, making controls feel tidier.
        if(isDragging()) super.extractRenderState(graphics, -1,     -1,     delta);
        else             super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
        graphics.blurBeforeThisStratum();
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
