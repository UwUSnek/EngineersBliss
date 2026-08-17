package com.snek.engineersbliss.client.mixin.screens;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.ui.base.ScreenMixinAccessor;

import net.minecraft.client.gui.screens.Screen;




/**
 * A mixin that adds texture cache handling logic to all Screens
 */
@Mixin(Screen.class)
public class ScreenMixin implements ScreenMixinAccessor {


    @Unique
    private final List<TextureCache> eb$textureCaches = new ArrayList<>();


    @Unique
    public void eb$registerTextureCacheForClose(TextureCache cache) {
        eb$textureCaches.add(cache);
    }


    @SuppressWarnings("unused")
    @Inject(method = "onClose", at = @At("HEAD"), cancellable = false, require = 1)
    private void eb$onClose(CallbackInfo ci) {
        for(final @NotNull TextureCache textureCache : eb$textureCaches) {
            textureCache.close();
        }
    }

}
