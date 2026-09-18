package com.snek.engineersbliss.client.mixin.screens;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.screens.pause_screen.PauseScreenContent;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.utils.data_types.Pair;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;








@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    @Unique private PauseScreenContent embedded;


    //! Not actually called. Java cries about constructors because the mixin extends Screen.
    //! True initialization is done in the init inject.
    protected PauseScreenMixin(final Component title) {
        super(title);
        embedded = null;
    }








    /**
     * Calculates the right edge and Y center of the Vanilla buttons cluster.
     * @return
     */
    @Unique
    private Pair<Float, Float> eb$getButtonClusterRightAndCenterY() {
        final List<Button> buttons = new ArrayList<>();
        for(final var e : this.children()) {
            if(e instanceof Button button) {
                buttons.add(button);
            }
        }
        final int maxX = buttons.stream().mapToInt(b -> b.getRight ()).max().orElseThrow();
        final int minY = buttons.stream().mapToInt(Button::getY)      .min().orElseThrow();
        final int maxY = buttons.stream().mapToInt(b -> b.getBottom()).max().orElseThrow();
        return Pair.from((float)maxX, (minY + maxY) / 2f);
    }

    @Inject(method = "init", at = @At("TAIL"), cancellable = false, require = 1)
    public void eb$init(final CallbackInfo ci) {
        final @NotNull Pair<Float, Float> clusterData = eb$getButtonClusterRightAndCenterY();
        final int vanillaGuiScale = MinecraftUtils.getVanillaGuiScale();
        embedded = new PauseScreenContent(clusterData.getFirst() * vanillaGuiScale, clusterData.getSecond() * vanillaGuiScale);
        embedded.init(width, height);
    }


    @Inject(method = "extractRenderState", at = @At("TAIL"), cancellable = false, require = 1)
    public void eb$extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a, CallbackInfo ci) {
        //! Draw the custom PauseScreenContents instance's contents. Without background.
        embedded.setForegroundOnly();
        embedded.extractRenderState(graphics, mouseX, mouseY, a);
    }


    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true, require = 1)
	public void eb$extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a, CallbackInfo ci) {
        //! Suppress Vanilla pause screen background.
        ci.cancel();
        //! Draw the custom PauseScreenContents instance's inherited background.
        embedded.setBackgroundOnly();
        embedded.extractRenderState(graphics, mouseX, mouseY, a);
    }







    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        final boolean    superRes =    super.mouseClicked(event, doubleClick);
        final boolean embeddedRes = embedded.mouseClicked(event, doubleClick);
        return superRes && embeddedRes;
    }
    @Override
    public boolean mouseReleased(final MouseButtonEvent event) {
        final boolean    superRes =    super.mouseReleased(event);
        final boolean embeddedRes = embedded.mouseReleased(event);
        return superRes && embeddedRes;
    }
    @Override
    public boolean mouseDragged(final MouseButtonEvent event, final double dx, final double dy) {
        final boolean    superRes =    super.mouseDragged(event, dx, dy);
        final boolean embeddedRes = embedded.mouseDragged(event, dx, dy);
        return superRes && embeddedRes;
    }
    @Override
    public boolean mouseScrolled(final double x, final double y, final double scrollX, final double scrollY) {
        final boolean    superRes =    super.mouseScrolled(x, y, scrollX, scrollY);
        final boolean embeddedRes = embedded.mouseScrolled(x, y, scrollX, scrollY);
        return superRes && embeddedRes;
    }
    @Override
    public boolean keyPressed(final KeyEvent event) {
        final boolean    superRes =    super.keyPressed(event);
        final boolean embeddedRes = embedded.keyPressed(event);
        return superRes && embeddedRes;
    }
    @Override
    public boolean keyReleased(final KeyEvent event) {
        final boolean    superRes =    super.keyReleased(event);
        final boolean embeddedRes = embedded.keyReleased(event);
        return superRes && embeddedRes;
    }
    @Override
    public boolean charTyped(final CharacterEvent event) {
        final boolean    superRes =    super.charTyped(event);
        final boolean embeddedRes = embedded.charTyped(event);
        return superRes && embeddedRes;
    }
}


//TODO add a placeholder "coming soon" overlay to more complex features
//TODO also add this to the reamde file
//TODO release a beta version without these features
//TODO full release will contain most of the main features and all the fixes