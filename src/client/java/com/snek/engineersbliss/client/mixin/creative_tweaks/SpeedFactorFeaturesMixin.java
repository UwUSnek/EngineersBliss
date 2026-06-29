package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;







@Mixin(Entity.class)
public class SpeedFactorFeaturesMixin {
    @Unique
    private static final float DEFAULT_SPEED_FACTOR = Blocks.STONE.getSpeedFactor();





    @Redirect(
        method = "getBlockSpeedFactor",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getSpeedFactor()F")
    )
    private float getBlockSpeedFactor(Block block) {
        final Entity entity = (Entity)(Object)this;
        Blocks.POWDER_SNOW
        if(!entity.level().isClientSide()) return block.getSpeedFactor();



        if(entity instanceof Player) {
            if(block == Blocks.HONEY_BLOCK) {
                if(CreativeTweaksHandler.hasFeature(CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN)) {
                    return DEFAULT_SPEED_FACTOR;
                }
            }
            if(block == Blocks.SOUL_SAND) {
                if(CreativeTweaksHandler.hasFeature(CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN)) {
                    return DEFAULT_SPEED_FACTOR;
                }
            }
        }
        //! Slime Block uses custom Friction instead of SpeedFactor
        //! Ice Blocks   use custom Friction instead of SpeedFactor
        return block.getSpeedFactor();
    }
}