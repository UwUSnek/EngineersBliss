package com.snek.engineersbliss.client.mixin.misc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.utils.textures.SvgTextureTracker;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;




@Mixin(TextureAtlas.class)
public abstract class SpriteAtlasSvgSupportMixin {

    @Inject(method = "getSprite", at = @At("HEAD"), cancellable = true)
    private void eb$redirectToScaledSprite(Identifier location, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        if(SvgTextureTracker.isRegistered(location)) {
            final Identifier scaledId = SvgTextureTracker.getOptimalSprite(location);
            cir.setReturnValue(((TextureAtlas)(Object)this).getSprite(scaledId)); //! Does 1 recursion then stops
        }
    }
}