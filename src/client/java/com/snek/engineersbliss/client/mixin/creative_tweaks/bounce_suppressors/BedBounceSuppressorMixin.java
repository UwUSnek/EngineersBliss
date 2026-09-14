package com.snek.engineersbliss.client.mixin.creative_tweaks.bounce_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.BedBlock;

//? if <=26.1.2 {
    // import org.spongepowered.asm.mixin.injection.At;
    // import org.spongepowered.asm.mixin.injection.Redirect;
    // import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
    // import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
    // import net.minecraft.world.entity.Entity;
    // import net.minecraft.world.level.BlockGetter;
//? } else {
//? }




@Mixin(BedBlock.class)
public class BedBounceSuppressorMixin {


    //! 26.2+ uses the Bounciness parameter. See BouncinessFeaturesMixin
    //? if <=26.1.2 {
        // @SuppressWarnings("unused")
        // @Redirect(
        //     method = "updateEntityMovementAfterFallOn",
        //     at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSuppressingBounce()Z")
        // )
        // private boolean eb$isSuppressingBounce(final Entity entity, final BlockGetter level, final Entity entityRef) {
        //     if(CreativeTweaksClientHandler.creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.DISABLE_BED_BOUNCE)) {
        //         return true;
        //     }
        //     return entity.isSuppressingBounce();
        // }
    //? } else {
    //? }
}
