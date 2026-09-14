package com.snek.engineersbliss.client.mixin.creative_tweaks.bounce_suppressors;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.Entity;

//! <=26.1.2 doesn't use the Bounciness parameter. Custom mixins modify each block individually.
//? if <=26.1.2 {
//? } else {
     import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
     import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
     import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
     import org.spongepowered.asm.mixin.injection.At;
     import org.spongepowered.asm.mixin.injection.Inject;
     import net.minecraft.tags.BlockTags;
     import net.minecraft.world.level.block.Block;
     import net.minecraft.world.level.block.Blocks;
//? }






@Mixin(Entity.class)
public class BouncinessFeaturesMixin {




    //! <=26.1.2 doesn't use the Bounciness parameter. Custom mixins modify each block individually.
    //? if <=26.1.2 {
    //? } else {
        @SuppressWarnings("unused")
        @Inject(method = "getBlockBounciness", at = @At("HEAD"), cancellable = true, require = 1)
        private void eb$getBlockBounciness(final Block onBlock, CallbackInfoReturnable<Double> cir) {
            if(
                ClientFeatureSync.getFeatureB(CreativeTweaksServerFeatureSet.DISABLE_BED_BOUNCE)   && onBlock.builtInRegistryHolder().is(BlockTags.BEDS) ||
                ClientFeatureSync.getFeatureB(CreativeTweaksServerFeatureSet.DISABLE_SLIME_BOUNCE) && onBlock == Blocks.SLIME_BLOCK
            ) {
                cir.setReturnValue(0.0);
            }
        }
    //? }
}