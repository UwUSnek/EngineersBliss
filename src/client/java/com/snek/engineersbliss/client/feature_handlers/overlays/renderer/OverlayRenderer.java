package com.snek.engineersbliss.client.feature_handlers.overlays.renderer;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
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



    private static void draw(LevelRenderContext context) {
        Minecraft client = Minecraft.getInstance();
        Level level = client.level;
        if(level == null || client.player == null) return;

        //FIXME actually calculate position and state
        // For each chunk
        for(final LevelChunk chunk : MinecraftUtils.getLoadedChunks()) {
            final ChunkPos chunkPos = chunk.getPos();
            final int minX = chunkPos.getMinBlockX();
            final int minZ = chunkPos.getMinBlockZ();

            // For each chunk section
            final var sections = chunk.getSections();
            for(int i = 0; i < sections.length; ++i) {
                final LevelChunkSection section = sections[i];
                final int minY = chunk.getMinY() + (i * LevelChunkSection.SECTION_HEIGHT);

                // If the section contains blocks that have features
                if(!section.hasOnlyAir() && section.maybeHas(state -> OverlayFeature.hasFeature(state.getBlock()))) {

                    // For each block in the section
                    for(int x = 0; x < LevelChunkSection.SECTION_WIDTH; x++) {
                        for(int y = 0; y < LevelChunkSection.SECTION_HEIGHT; y++) {
                            for(int z = 0; z < LevelChunkSection.SECTION_WIDTH; z++) {
                                final BlockPos pos = new BlockPos(minX + x, minY + y, minZ + z);
                                final BlockState state = section.getBlockState(x, y, z);

                                // For each overlay provider whose block matches, render the overlay
                                for(__base_OverlayProvider provider : providers) {
                                    if(provider instanceof TextureOverlayProvider p && p.shouldRender(state, pos)) {

                                        // Get buffer source and texture
                                        MultiBufferSource.BufferSource bufferSource = context.bufferSource();
                                        final Identifier texturePath = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/" + p.calcTexturePath(state, pos));

                                        // Create render data
                                        RenderType renderType = RenderTypes.entityCutout(texturePath);
                                        //TODO use entityTranslucent for semitransparent textures
                                        VertexConsumer consumer = bufferSource.getBuffer(renderType);
                                        PoseStack matrices = new PoseStack();
                                        matrices.pushPose();

                                        // Calculate vertex positions
                                        Vec3 cameraPos = context.levelState().cameraRenderState.pos;
                                        double _y = pos.getY() - cameraPos.y + p.calcVerticalOffset(state, pos);
                                        double x0 = pos.getX() - cameraPos.x;
                                        double z0 = pos.getZ() - cameraPos.z;
                                        double x1 = x0 + 1.0;
                                        double z1 = z0 + 1.0;

                                        // Calculate text light level and color
                                        int light = 0xF000F0;
                                        int color = 0xFFFFFFFF;

                                        // Add vertices and set properties
                                        consumer.addVertex(matrices.last().pose(), (float)x0, (float)_y, (float)z0).setUv(0f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                                        consumer.addVertex(matrices.last().pose(), (float)x0, (float)_y, (float)z1).setUv(0f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                                        consumer.addVertex(matrices.last().pose(), (float)x1, (float)_y, (float)z1).setUv(1f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                                        consumer.addVertex(matrices.last().pose(), (float)x1, (float)_y, (float)z0).setUv(1f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);

                                        // Send render batch
                                        matrices.popPose();
                                        bufferSource.endBatch(renderType);
                                        //TODO this might need to be ran at the very end of each rener type batch, instead of multiple times for single block
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //TODO handle other types
        }
    }
}

