package com.snek.engineersbliss.client.mixin.interactions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;




@Mixin(Minecraft.class)
public class InteractionFilterMixin {
    @Inject(
        method = "pick",
        at = @At("RETURN")
    )
    public void onPick(final float partialTicks, final CallbackInfo ci) {
        if(RenderFilterHandler.getTargetHiddenBlocks()) return;
        final Minecraft minecraft = Minecraft.getInstance();
        final LocalPlayer player = minecraft.player;
        if(minecraft.level == null || player == null) return;


        // If the first block is not hidden, continue with vanilla logic
        if(!(minecraft.hitResult instanceof final BlockHitResult bhr)) return;
        final BlockState targeted = minecraft.level.getBlockState(bhr.getBlockPos());
        if(RenderFilterHandler.getActiveBlocks().contains(targeted.getBlock())) return;


        // Otherwise run custom ray casting logic
        final Vec3 start = minecraft.gameRenderer.getMainCamera().position();
        final Vec3 look = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
        final double reach = player.blockInteractionRange();
        final Vec3 end = start.add(look.scale(reach));

        final BlockHitResult result = BlockGetter.traverseBlocks(start, end, null,
            (context, pos) -> {
                final BlockState state = minecraft.level.getBlockState(pos);
                if(state.isAir()) return null;
                if(!RenderFilterHandler.getActiveBlocks().contains(state.getBlock())) return null;
                final BlockHitResult hit = state.getShape(minecraft.level, pos, CollisionContext.of(player))
                    .clip(start, end, pos);
                return hit != null ? hit : BlockHitResult.miss(end, Direction.UP, pos);
            },
            context -> BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end))
        );

        if(result != null && result.getType() != HitResult.Type.MISS) {
            minecraft.hitResult = result;
        }
        else {
            minecraft.hitResult = BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));
        }
    }
}