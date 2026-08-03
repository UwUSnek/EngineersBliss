package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;




/**
 * A mixin that shrinks the reported GUI height to leave space for the status bar.
 * This doesn't catch everything, mods can still read the actual height of the window and draw on top of the status bar, but it's the best option.
 */
@Mixin(GuiGraphicsExtractor.class)
public class VanillaGuiHeightChangerMixin {


    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "guiHeight", at = @At("RETURN"), require = 1)
    private int eb$guiHeight(int original) {


        // Do nothing if the player is not in a world.
        // Also do nothing if the player is not in creative mode.
        // Also do nothing if the player has the chat open and the "Chat hides Status Bar" setting ON.
        if(
            Minecraft.getInstance().level == null ||
            !MinecraftUtils.isCreativeMode() ||
            (ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.CHAT_HIDES_STATUS_BAR) && MinecraftUtils.isChatOpen())
        ) {
            return original;
        }

        // Modify GUI height otherwise
        //! Position=TOP doesn't require any resizing.
        //! Minecraft doesn't provide any getter for the Y base coord, so the status bar is simply drawn on top of the existing elements.
        else {
            if(!ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.STATUS_BAR_POSITION)) {
                final int barHeight = ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.STATUS_BAR_HEIGHT);
                return original - barHeight;
            }
            return original;
        }
    }
}
