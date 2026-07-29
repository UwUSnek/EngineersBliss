package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;



@Mixin(SlimeBlock.class)
public class SlimeFeaturesMixin {


    @SuppressWarnings("unused")
    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    private void eb$stepOn(final Level level, final BlockPos pos, final BlockState onState, final Entity entity, final CallbackInfo ci) {
        if(ClientFeatureSync.creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.DISABLE_SLIME_SLOWDOWN)) {
            ci.cancel();
        }
    }


    @SuppressWarnings("unused")
    @Redirect(
        method = "updateEntityMovementAfterFallOn",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSuppressingBounce()Z")
    )
    private boolean eb$isSuppressingBounce(final Entity entity, final BlockGetter level, final Entity entityRef) {
        if(ClientFeatureSync.creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.DISABLE_SLIME_BOUNCE)) {
            return true;
        }
        return entity.isSuppressingBounce();
    }
}
