package com.snek.engineersbliss.client.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;




@Mixin(BlockEntityRenderer.class)
public interface RenderFilterBlockEntityMixin {
    @Inject(
        method = "shouldRender",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private<T extends BlockEntity> void shouldRender(final T blockEntity, final Vec3 cameraPosition, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("BLOCK ENTITY");
        if(!RenderFilterHandler.getActiveBlocks().contains(blockEntity.getBlockState().getBlock())) {
            cir.setReturnValue(false);
        }
    }
}