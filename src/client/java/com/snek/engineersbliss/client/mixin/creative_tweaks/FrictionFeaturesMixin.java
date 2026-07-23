package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;








@Mixin(LivingEntity.class)
public class FrictionFeaturesMixin {
    @Unique
    private static final float DEFAULT_FRICTION = Blocks.STONE.getFriction();




    @SuppressWarnings("unused")
    @Redirect(
        method = "travelInAir",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F")
    )
    private float eb$travelInAir(final Block block) {
        if(block == Blocks.SLIME_BLOCK) {
            if(ClientFeatureSync.creativePlayerHasFeature(this, CreativeTweaksServerFeatureSet.DISABLE_SLIME_SLOWDOWN)) {
                return DEFAULT_FRICTION;
            }
        }
        if(
            block == Blocks.ICE        ||
            block == Blocks.PACKED_ICE ||
            block == Blocks.BLUE_ICE   ||
            block == Blocks.FROSTED_ICE
        ) {
            if(ClientFeatureSync.creativePlayerHasFeature(this, CreativeTweaksServerFeatureSet.DISABLE_ICE_SLIDING)) {
                return DEFAULT_FRICTION;
            }
        }
        //! Honey Block uses custom SpeedFactor instead of Friction
        //! Soul Sand   uses custom SpeedFactor instead of Friction

        return block.getFriction();
    }
}

//TODO idk if methods other than travelInAir use  the blck's friction for the speed calculation
//TODO that doesn't seem to be the case but i need to check properly.
//TODO inject those too if they do