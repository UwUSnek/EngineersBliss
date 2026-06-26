package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




@Mixin(LivingEntity.class)
public class FrictionFeaturesMixin {
    @Unique
    private static final float DEFAULT_FRICTION = Blocks.GRASS_BLOCK.getFriction();


    @Redirect(
        method = "travelInAir",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F")
    )
    private float travelInAir(Block block) {
        final LivingEntity entity = (LivingEntity)(Object)this;
        if(entity instanceof Player player) {
            if(
                block == Blocks.SLIME_BLOCK
            ) {
                final long featureMask = CreativeTweaksServerHandler.getToggleFeatures(player);
                if(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN.hasFlagBit(featureMask)) {
                    return DEFAULT_FRICTION;
                }
            }
        }
        return block.getFriction();
    }
}

//TODO idk if methods other than travelInAir use  the blck's friction for the speed calculation
//TODO that doesn't seem to be the case but i need to check properly.
//TODO inject those too if they do