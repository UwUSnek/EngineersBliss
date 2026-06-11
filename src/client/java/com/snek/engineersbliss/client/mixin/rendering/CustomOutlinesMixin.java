package com.snek.engineersbliss.client.mixin.rendering;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;




@Mixin(LevelRenderer.class)
public abstract class CustomOutlinesMixin {
    private static final List<BlockPos> excludedAlongRay = new ArrayList<>();
    @Shadow public abstract void renderHitOutline(final PoseStack poseStack, final VertexConsumer builder, final double camX, final double camY, final double camZ, final BlockOutlineRenderState state, final int color, final float width);



    @Inject(
        method = "extractBlockOutline",
        at = @At("HEAD"),
        cancellable = true
    )
    public void extractBlockOutline(Camera camera, LevelRenderState levelRenderState, CallbackInfo ci) {
        if(RenderFilterHandler.getTargetHiddenBlocks()) return;
        excludedAlongRay.clear();

        // Return if no level or no hit result
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.level == null || minecraft.player == null) return;
        HitResult hit = minecraft.hitResult;
        if(!(hit instanceof BlockHitResult)) return;


        // Run custom outline logic if the first block is hidden
        //! Don't cancel. Let the vanilla renderer draw the outline of the targeted visible block
        //! Intermediate outlines use a custom renderer
        Vec3 start = camera.position();
        Vec3 look = Vec3.directionFromRotation(camera.xRot(), camera.yRot());
        double reach = minecraft.player.blockInteractionRange();
        Vec3 end = start.add(look.scale(reach));

        BlockHitResult result = BlockGetter.traverseBlocks(start, end, null,
            (context, pos) -> {
                BlockState state = minecraft.level.getBlockState(pos);
                if (state.isAir()) return null;
                if (!RenderFilterHandler.getActiveBlocks().contains(state.getBlock())) {
                    excludedAlongRay.add(pos.immutable());
                    return null;
                }
                BlockHitResult newHit = state.getShape(minecraft.level, pos, CollisionContext.of(minecraft.player)).clip(start, end, pos);
                return newHit != null ? newHit : BlockHitResult.miss(end, Direction.UP, pos);
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




    @Inject(
        method = "renderBlockOutline",
        at = @At("RETURN")
    )
    public void renderExcludedOutlines(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean onlyTranslucentBlocks, LevelRenderState levelRenderState, CallbackInfo ci) {
        if(RenderFilterHandler.getTargetHiddenBlocks()) return;
        if(excludedAlongRay.isEmpty()) return;

        Minecraft minecraft = Minecraft.getInstance();
        Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
        VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lines());

        for(BlockPos pos : excludedAlongRay) {
            BlockState state = minecraft.level.getBlockState(pos);
            VoxelShape shape = state.getShape(minecraft.level, pos, CollisionContext.of(minecraft.player));
            BlockOutlineRenderState outlineState = new BlockOutlineRenderState(pos, false, false, shape);
            this.renderHitOutline(poseStack, buffer, cameraPos.x, cameraPos.y, cameraPos.z, outlineState, ARGB.color(0.3f, 0x888888), 2.5F);
        }

        bufferSource.endLastBatch();
    }
}

//FIXME actually ignore interactions on click