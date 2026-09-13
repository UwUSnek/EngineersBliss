package com.snek.engineersbliss.client.mixin.rendering;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.feature_handlers.rendering.RenderingServerFeatureSet;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;

//? if <=26.1.2 {
    // import com.mojang.blaze3d.vertex.VertexConsumer;
    // import net.minecraft.client.renderer.MultiBufferSource;
//? } else {
    import net.minecraft.client.renderer.SubmitNodeCollector;
    import net.minecraft.client.renderer.rendertype.RenderType;
//? }
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.BlockGetter;
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
    //? if <=26.1.2 {
        // @Shadow public abstract void renderHitOutline(final PoseStack poseStack, final VertexConsumer builder, final double camX, final double camY, final double camZ, final BlockOutlineRenderState state, final int color, final float width);
    //? } else {
        @Shadow public abstract void submitHitOutline(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final RenderType renderType, final BlockOutlineRenderState state, final int color, final float width, final boolean afterTerrain);
    //? }



    @SuppressWarnings("unused")
    @Inject(method = "extractBlockOutline", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$extractBlockOutline(final Camera camera, final LevelRenderState levelRenderState, final CallbackInfo ci) {

        //! Block vanilla and return if outlines are disabled
        if(!ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.RENDER_BLOCK_OUTLINES)) {
            ci.cancel();
            return;
        }
        if(ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.TARGET_HIDDEN_BLOCKS)) {
            return;
        }
        customOutlineBlocks.clear();


        // Return if no level or no hit result
        final @NotNull Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.level == null || minecraft.player == null) return;
        final HitResult hit = minecraft.hitResult;
        if(!(hit instanceof BlockHitResult)) return;


        // Run custom outline logic if the first block is hidden
        ci.cancel();
        final Vec3 start = minecraft.player.getEyePosition();
        final Vec3 look = minecraft.player.getViewVector(1.0f);
        final double reach = minecraft.player.blockInteractionRange();
        final Vec3 end = start.add(look.scale(reach));


        // Traverse blocks and save hidden blocks the ray passes through
        //! Hit result is not stored anywhere as all relevant information is stored in the output block list
        //! Both outline rendering and interaction handling use the block list and ignore the vanilla hit result
        BlockGetter.traverseBlocks(start, end, null,
            (context, pos) -> {
                final BlockState state = minecraft.level.getBlockState(pos);
                if(state.is(Blocks.AIR)) return null;

                // Text ray hit, add to the list of outlines if it intersects the shape
                final BlockHitResult newHit = state.getShape(minecraft.level, pos, CollisionContext.of(minecraft.player)).clip(start, end, pos);
                if(newHit != null) {
                    customOutlineBlocks.add(pos.immutable());
                }

                // If the block is hidden or the ray didn't intersect, return null (keep traversing). Otherwise return the hit result
                if(!RenderingFilterHandler.shouldStateRender(state) || newHit == null) return null;
                else return newHit;
            },
            context -> BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end))
        );
    }








    //? if <=26.1.2 {
        // @SuppressWarnings("unused")
        // @Inject(method = "renderBlockOutline", at = @At("HEAD"), cancellable = true, require = 1)
        // private void eb$renderBlockOutlines(final MultiBufferSource.BufferSource bufferSource, final PoseStack poseStack, final boolean onlyTranslucentBlocks, final LevelRenderState levelRenderState, final CallbackInfo ci) {
    // } else {
        @SuppressWarnings("unused")
        @Inject(method = "submitBlockOutline", at = @At("HEAD"), cancellable = true, require = 1)
        private void eb$submitBlockOutline(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final LevelRenderState levelRenderState, final CallbackInfo ci) {
    // }

        //! Block vanilla and return if outlines are disabled
        if(!ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.RENDER_BLOCK_OUTLINES)) {
            ci.cancel();
            return;
        }
        if(ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.TARGET_HIDDEN_BLOCKS)) {
            return;
        }
        if(customOutlineBlocks.isEmpty()) return;


        ci.cancel();
        //? if <=26.1.2 {
            // if(onlyTranslucentBlocks) return;
        //? } else {
            //BUG idk whats supposed to replace that. the parameter is just gone
        //? }
        //! Vanilla's checks don't actually draw any outline when ran from this mixin so I use !onlyTranslucentBlocks.
        //! I have no idea why. But this produces a consistent outline

        final @NotNull Minecraft minecraft = Minecraft.getInstance();
        //? if <=26.1.2 {
            // final Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
            // final VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lines());
            // final float lineWidth = minecraft.gameRenderer.getGameRenderState().windowRenderState.appropriateLineWidth;
        //? } else {
            //BUG idk whats supposed to replace the buffer. there is no "getBuffer" in SubmitNodeCollector
            final float lineWidth = minecraft.gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth;
        //? }


        // For each block position in the player's view ray
        for(int i = 0; i < customOutlineBlocks.size(); ++i) {
            final BlockPos pos = customOutlineBlocks.get(i);
            final BlockState state = minecraft.level.getBlockState(pos);
            final VoxelShape shape = state.getShape(minecraft.level, pos, CollisionContext.of(minecraft.player));
            final BlockOutlineRenderState outlineState = new BlockOutlineRenderState(pos, false, false, shape);

            // Draw outline: Default black for visible block, thicker gray for hidden ones
            if(i == customOutlineBlocks.size() - 1 && RenderingFilterHandler.shouldStateRender(state)) {
                //? if <=26.1.2 {
                    // this.renderHitOutline(poseStack, buffer, cameraPos.x, cameraPos.y, cameraPos.z, outlineState, ARGB.black(102), lineWidth);
                //? } else {
                    this.submitHitOutline(poseStack, submitNodeCollector, RenderTypes.secondaryBlockOutline(), outlineState, ARGB.black(102), lineWidth, false);
                //? }
            }
            else {
                //? if <=26.1.2 {
                    // this.renderHitOutline(poseStack, buffer, cameraPos.x, cameraPos.y, cameraPos.z, outlineState, ARGB.color(0.3f, 0x666666), lineWidth * 1.25f);
                //? } else {
                    this.submitHitOutline(poseStack, submitNodeCollector, RenderTypes.secondaryBlockOutline(), outlineState, ARGB.color(0.3f, 0x666666), lineWidth * 1.25f, false);
                //? }
            }
        }

        //? if <=26.1.2 {
            // bufferSource.endLastBatch();
        //? } else {
            //BUG idk whats supposed to replace endLastBatch. there is no "getBuffer" in SubmitNodeCollector
        //? }
    }
}