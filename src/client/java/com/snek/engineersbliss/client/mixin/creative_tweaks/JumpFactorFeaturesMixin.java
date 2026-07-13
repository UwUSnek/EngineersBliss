package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweakFeature;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;







@Mixin(Entity.class)
public class JumpFactorFeaturesMixin {
    @Unique
    private static final float DEFAULT_JUMP_FACTOR = Blocks.STONE.getJumpFactor();




    @SuppressWarnings("unused")
    @Redirect(
        method = "getBlockJumpFactor",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getJumpFactor()F")
    )
    private float eb$getBlockSpeedFactor(final Block block) {
        if(block == Blocks.HONEY_BLOCK) {
            if(CreativeTweaksHandler.clientPlayerHasFeature(this, CreativeTweakFeature.DISABLE_HONEY_JUMP)) {
                return DEFAULT_JUMP_FACTOR;
            }
        }
        return block.getSpeedFactor();
    }
}