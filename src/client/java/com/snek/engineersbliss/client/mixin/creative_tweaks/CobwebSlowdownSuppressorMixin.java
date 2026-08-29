package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;





@Mixin(WebBlock.class)
public class CobwebSlowdownSuppressorMixin {


    @SuppressWarnings("unused")
    @Redirect(
        method = "entityInside",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;makeStuckInBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/Vec3;)V"
        )
    )
    private void eb$makeStuckInBlock(final Entity entity, final BlockState state, final Vec3 speedMultiplier) {
        if(!CreativeTweaksClientHandler.creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.DISABLE_COBWEB_SLOWDOWN)) {
            entity.makeStuckInBlock(state, speedMultiplier);
        }
    }
}
