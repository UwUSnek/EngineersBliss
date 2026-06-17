package com.snek.engineersbliss.client.feature_handlers.overlays.renderer;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.overlays.renderers.RedstoneLevelOverlayProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;








public final class OverlayRenderer {
    private OverlayRenderer() {}


    private static List<__base_OverlayProvider> providers = new ArrayList<>();


    public static void register() {
        providers.add(new RedstoneLevelOverlayProvider());
        LevelRenderEvents.COLLECT_SUBMITS.register(OverlayRenderer::draw);
        // LevelRenderEvents.AFTER_OPAQUE_TERRAIN.register(OverlayRenderer::draw); //BUG use this to get the blocks?
    }



    private static void draw(LevelRenderContext context) {
        for(__base_OverlayProvider provider : providers) {
            if(provider instanceof TextureOverlayProvider p) {
                Minecraft client = Minecraft.getInstance();
                Level level = client.level;
                if(level == null || client.player == null) return;

                //FIXME actually calculate position and state
                final BlockPos pos = new BlockPos(0,0,0);
                final BlockState state = Blocks.AIR.defaultBlockState();
                MultiBufferSource.BufferSource bufferSource = context.bufferSource();
                Vec3 camera = context.levelState().cameraRenderState.pos;

                final Identifier texturePath = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/" + p.calcTexturePath(state, pos));
                RenderType renderType = RenderTypes.entityCutout(texturePath);
                VertexConsumer consumer = bufferSource.getBuffer(renderType);

                PoseStack matrices = new PoseStack();
                matrices.pushPose();

                BlockPos center = client.player.blockPosition();
                int light = 0xF000F0;
                int color = 0xFFFFFFFF;

                final int RADIUS = 5; //FIXME remove, check all chunks
                for(int dx = -RADIUS; dx <= RADIUS; dx++) {
                    for(int dy = -RADIUS; dy <= RADIUS; dy++) {
                        for(int dz = -RADIUS; dz <= RADIUS; dz++) {
                            BlockPos _pos = center.offset(dx, dy, dz);

                            double x0 = _pos.getX() - camera.x;
                            double z0 = _pos.getZ() - camera.z;
                            double y  = _pos.getY() - camera.y + p.calcVerticalOffset(state, pos);
                            double x1 = x0 + 1.0;
                            double z1 = z0 + 1.0;

                            consumer.addVertex(matrices.last().pose(), (float) x0, (float) y, (float) z0).setUv(0f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                            consumer.addVertex(matrices.last().pose(), (float) x0, (float) y, (float) z1).setUv(0f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                            consumer.addVertex(matrices.last().pose(), (float) x1, (float) y, (float) z1).setUv(1f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                            consumer.addVertex(matrices.last().pose(), (float) x1, (float) y, (float) z0).setUv(1f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                        }
                    }
                }

                matrices.popPose();
                bufferSource.endBatch(renderType);
            }
            //TODO handle other types
        }
    }
}

