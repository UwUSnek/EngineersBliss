package com.snek.engineersbliss.client.mixin.creative_tweaks.bounce_suppressors;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.SlimeBlock;

//? if <=26.1.2 {
    import org.spongepowered.asm.mixin.injection.Redirect;
    import net.minecraft.world.level.BlockGetter;
//? } else {
//? }




@Mixin(SlimeBlock.class)
public class SlimeBlockBounceSuppressorMixin {

    //! 26.2+ uses the Bounciness parameter. See BouncinessFeaturesMixin
    //? if <=26.1.2 {
        @SuppressWarnings("unused")
        @Redirect(
            method = "updateEntityMovementAfterFallOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSuppressingBounce()Z")
        )
        private boolean eb$isSuppressingBounce(final Entity entity, final BlockGetter level, final Entity entityRef) {
            if(CreativeTweaksClientHandler.creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.DISABLE_SLIME_BOUNCE)) {
            return true;
            }
        return entity.isSuppressingBounce();
        }
    //? } else {
    //? }
}
