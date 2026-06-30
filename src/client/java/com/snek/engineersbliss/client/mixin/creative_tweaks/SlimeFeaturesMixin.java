package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;



@Mixin(SlimeBlock.class)
public class SlimeFeaturesMixin {


    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    public void stepOn(final Level level, final BlockPos pos, final BlockState onState, final Entity entity, final CallbackInfo ci) {
        if(CreativeTweaksServerHandler.serverPlayerHasFeature(entity, CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN)) {
            ci.cancel();
        }
    }


    @Redirect(
        method = "updateEntityMovementAfterFallOn",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSuppressingBounce()Z")
    )
    public boolean isSuppressingBounce(Entity entity, BlockGetter level, Entity entityRef) {
        if(CreativeTweaksServerHandler.serverPlayerHasFeature(entity, CreativeTweakFeature.DISABLE_SLIME_BOUNCE)) {
            return true;
        }
        return entity.isSuppressingBounce();
    }
}
