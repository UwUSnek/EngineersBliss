package com.snek.engineersbliss.client.mixin.misc;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.mixin.accessors.SpriteSourcesAccessor;
import com.snek.engineersbliss.client.utils.textures.svg.SvgSpriteSource;

import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;




@Mixin(SpriteSources.class)
public class SpriteSourcesMixin {

    @SuppressWarnings("unused")
    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void eb$registerSvgSource(final CallbackInfo ci) {
        SpriteSourcesAccessor.eb$idMapper().put(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "svg"), SvgSpriteSource.MAP_CODEC);
    }
}