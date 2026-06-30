package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;




@Mixin(Item.class)
public class PlayerTargetedBlockOverrideMixin {
    private PlayerTargetedBlockOverrideMixin() {}


    @SuppressWarnings("unused")
    @Inject(method = "getPlayerPOVHitResult", at = @At("HEAD"), cancellable = true, require = 1)
    private static void overrideGetPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluid, CallbackInfoReturnable<BlockHitResult> cir) {
        final BlockPos override = CreativeTweaksServerHandler.getPickOverride();
        if(override != null) {
            Vec3 center = Vec3.atCenterOf(override);
            cir.setReturnValue(new BlockHitResult(center, Direction.UP, override, false));
        }
    }
}