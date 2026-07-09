package com.snek.engineersbliss.mixin.creative_tweaks;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;




@Mixin(RangedAttribute.class)
public class ReachDistanceCapMixin {

    @Inject(method = "sanitizeValue", at = @At("HEAD"), cancellable = true, require = 1)
    public void eb$sanitizeValue(final double value, final CallbackInfoReturnable<Double> cir) {
        final RangedAttribute _this = (RangedAttribute)(Object)this;
        final @NotNull String id = _this.getDescriptionId();
        if(
            id.equals("attribute.name.block_interaction_range") ||
            id.equals("attribute.name.entity_interaction_range")
        ) {
            final double min = _this.getMinValue();
            cir.setReturnValue(Double.isNaN(value) ? min : Mth.clamp(value, min, 16384.0));
        }
    }
}
