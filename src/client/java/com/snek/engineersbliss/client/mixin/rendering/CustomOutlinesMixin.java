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
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderFilterHandler;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;




@Mixin(LevelRenderer.class)
public abstract class CustomOutlinesMixin {
    private static final List<BlockPos> customOutlineBlocks = new ArrayList<>();
    @Shadow public abstract void renderHitOutline(final PoseStack poseStack, final VertexConsumer builder, final double camX, final double camY, final double camZ, final BlockOutlineRenderState state, final int color, final float width);



    @Inject(
        method = "extractBlockOutline",
        at = @At("HEAD"),
        cancellable = true
    )
    public void extractBlockOutline(final Camera camera, final LevelRenderState levelRenderState, final CallbackInfo ci) {
        if(!RenderFilterHandler.getRenderBlockOutlines()) { ci.cancel(); return; } //! Block vanilla and return if outlines are disabled
        if(RenderFilterHandler.getTargetHiddenBlocks()) return;
        customOutlineBlocks.clear();

        // Return if no level or no hit result
        final Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.level == null || minecraft.player == null) return;
        final HitResult hit = minecraft.hitResult;
        if(!(hit instanceof BlockHitResult)) return;


        // Run custom outline logic if the first block is hidden
        ci.cancel();
        final Vec3 start = camera.position();
        final Vec3 look = Vec3.directionFromRotation(camera.xRot(), camera.yRot());
        final double reach = minecraft.player.blockInteractionRange();
        final Vec3 end = start.add(look.scale(reach));

        final BlockHitResult result = BlockGetter.traverseBlocks(start, end, null,
            (context, pos) -> {
                final BlockState state = minecraft.level.getBlockState(pos);
                final Block block = state.getBlock();
                if(block == Blocks.AIR) return null;
                customOutlineBlocks.add(pos.immutable());
                if(!RenderFilterHandler.getActiveBlocks().contains(block)) {
                    return null;
                }
                final BlockHitResult newHit = state.getShape(minecraft.level, pos, CollisionContext.of(minecraft.player)).clip(start, end, pos);
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
        at = @At("HEAD"),
        cancellable = true
    )
    public void renderBlockOutlines(final MultiBufferSource.BufferSource bufferSource, final PoseStack poseStack, final boolean onlyTranslucentBlocks, final LevelRenderState levelRenderState, final CallbackInfo ci) {
        if(!RenderFilterHandler.getRenderBlockOutlines()) { ci.cancel(); return; } //! Block vanilla and return if outlines are disabled
        if(RenderFilterHandler.getTargetHiddenBlocks()) return;
        if(customOutlineBlocks.isEmpty()) return;


        ci.cancel();
        if(onlyTranslucentBlocks) return;
        //! Vanilla's checks don't actually draw any outline when ran from this mixin so I use !onlyTranslucentBlocks.
        //! I have no idea why. But this produces a consistent outline

        final Minecraft minecraft = Minecraft.getInstance();
        final Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
        final VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lines());
        final float lineWidth = minecraft.gameRenderer.getGameRenderState().windowRenderState.appropriateLineWidth;


        // For each block position in the player's view ray
        for(int i = 0; i < customOutlineBlocks.size(); ++i) {
            final BlockPos pos = customOutlineBlocks.get(i);
            final BlockState state = minecraft.level.getBlockState(pos);
            final VoxelShape shape = state.getShape(minecraft.level, pos, CollisionContext.of(minecraft.player));
            final BlockOutlineRenderState outlineState = new BlockOutlineRenderState(pos, false, false, shape);

            // Draw outline: Default black for visible block, thicker gray for hidden ones
            if(i == customOutlineBlocks.size() - 1 && RenderFilterHandler.getActiveBlocks().contains(minecraft.level.getBlockState(pos).getBlock())) {
                this.renderHitOutline(poseStack, buffer, cameraPos.x, cameraPos.y, cameraPos.z, outlineState, ARGB.black(102), lineWidth);
            }
            else {
                this.renderHitOutline(poseStack, buffer, cameraPos.x, cameraPos.y, cameraPos.z, outlineState, ARGB.color(0.3f, 0x888888), lineWidth * 1.25f);
            }
        }

        bufferSource.endLastBatch();
    }
}