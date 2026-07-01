package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BedBlock;



@Mixin(BedBlock.class)
public class BedBounceSuppressorMixin {


    @SuppressWarnings("unused")
    @Redirect(
        method = "updateEntityMovementAfterFallOn",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSuppressingBounce()Z")
    )
    private boolean isSuppressingBounce(Entity entity, BlockGetter level, Entity entityRef) {
        if(CreativeTweaksHandler.clientPlayerHasFeature(entity, CreativeTweakFeature.DISABLE_BED_BOUNCE)) {
            return true;
        }
        return entity.isSuppressingBounce();
    }
}
