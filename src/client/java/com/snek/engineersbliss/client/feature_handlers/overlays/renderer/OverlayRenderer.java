package com.snek.engineersbliss.client.feature_handlers.overlays.renderer;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.RedstoneLevelOverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.__base_OverlayProvider;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;








public final class OverlayRenderer {
    private OverlayRenderer() {}


    private static List<__base_OverlayProvider> providers = new ArrayList<>();


    public static void register() {
        providers.add(new RedstoneLevelOverlayProvider());
        LevelRenderEvents.COLLECT_SUBMITS.register(OverlayRenderer::draw);
    }



    private static void draw(final LevelRenderContext context) {
        final Minecraft client = Minecraft.getInstance();
        final Level level = client.level;
        if(level == null || client.player == null) return;


        // For each chunk in the feature mask
        for(final var chunkFeatureMaskEntry : OverlaysHandler.getFeatureMask().entrySet()) {
            final ChunkPos chunkPos = chunkFeatureMaskEntry.getKey();
            final LevelChunk chunk = level.getChunk(chunkPos.x(), chunkPos.z());

            // If the chunk is currently visible by the player
            if(MinecraftUtils.isChunkVisible(chunk)) {

                // For each block in the chunk
                for(final var blockFeatureMaskEntry : chunkFeatureMaskEntry.getValue().entrySet()) {
                    final BlockPos pos = blockFeatureMaskEntry.getKey();
                    final BlockState state = chunk.getBlockState(pos);

                    // Render each overlay provider that should be rendered, one by one, using the computed values
                    for(final __base_OverlayProvider provider : providers) {
                        if(provider instanceof final TextureOverlayProvider p && p.shouldRender(state, pos)) {

                            // Get texture path
                            final Identifier texturePath = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/" + p.calcTexturePath(state, pos));

                            // Retrieve render info and buffer source
                            final MultiBufferSource.BufferSource bufferSource = context.bufferSource();
                            final RenderType renderType = RenderTypes.entityCutout(texturePath);
                            //TODO use entityTranslucent for semitransparent textures
                            final VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

                            // Create pose stack
                            final PoseStack matrices = new PoseStack();
                            matrices.pushPose();

                            // Calculate vertex positions
                            final Vec3 cameraPos = context.levelState().cameraRenderState.pos;
                            final double _y = pos.getY() - cameraPos.y + p.calcVerticalOffset(state, pos);
                            final double _x = pos.getX() - cameraPos.x + 0.5;
                            final double _z = pos.getZ() - cameraPos.z + 0.5;
                            final double width = p.calcWidth(state, pos);
                            final double x0 = _x - width;
                            final double z0 = _z - width;
                            final double x1 = _x + width;
                            final double z1 = _z + width;

                            // Calculate text light level and color
                            final int light = 0xF000F0;
                            final int color = 0xFFFFFFFF;

                            // Add vertices and set properties
                            vertexConsumer.addVertex(matrices.last().pose(), (float)x0, (float)_y, (float)z0).setUv(0f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                            vertexConsumer.addVertex(matrices.last().pose(), (float)x0, (float)_y, (float)z1).setUv(0f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                            vertexConsumer.addVertex(matrices.last().pose(), (float)x1, (float)_y, (float)z1).setUv(1f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                            vertexConsumer.addVertex(matrices.last().pose(), (float)x1, (float)_y, (float)z0).setUv(1f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);

                            // Reset pose
                            matrices.popPose();

                            //! No endBatch call needed
                            //! Let the game handle that normally
                        }
                    }
                }
            }
            //TODO handle other types
        }
    }
}

