package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;




@Mixin(SlimeBlock.class)
public class SlimeFeaturesMixin {


    // Disable slime slowdown by removing the stepOn function.
    // Superclass stepOn is no-op so cancelling the entire call is enough.
    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    public void stepOn(final Level level, final BlockPos pos, final BlockState onState, final Entity entity, final CallbackInfo ci) {
        if(CreativeTweaksHandler.getFeature(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN)) {
            System.out.println("HERE");
            ci.cancel();
        }
    }
}
