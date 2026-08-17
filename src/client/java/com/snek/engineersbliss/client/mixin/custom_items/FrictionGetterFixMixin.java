package com.snek.engineersbliss.client.mixin.custom_items;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.custom.blocks.base.FrictionSurface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;




/**
 * ! By default, Vanilla checks the friction of whatever block is 0.5 blocks below the entity.
 * ! This is what lets entities slide over of slabs on top of ice blocks.
 * This mixin allows the Frictionless Surface and Frictionful Surface blocks to work as intended even when placed in the bottom half of the block.
 */
@Mixin(Entity.class)
public abstract class FrictionGetterFixMixin {
    @Shadow public Optional<BlockPos> mainSupportingBlockPos;
    @Shadow public abstract Level level();


    @SuppressWarnings("unused")
    @Inject(method = "getOnPos(F)Lnet/minecraft/core/BlockPos;", at = @At("HEAD"), cancellable = true)
    private void engineersbliss$trustSupportingSurfaceBlock(float offset, CallbackInfoReturnable<BlockPos> cir) {
        if (this.mainSupportingBlockPos.isPresent()) {
            BlockPos supportingPos = this.mainSupportingBlockPos.get();
            Block supportingBlock = this.level().getBlockState(supportingPos).getBlock();

            if(supportingBlock instanceof FrictionSurface) {
                cir.setReturnValue(supportingPos);
            }
        }
    }
}