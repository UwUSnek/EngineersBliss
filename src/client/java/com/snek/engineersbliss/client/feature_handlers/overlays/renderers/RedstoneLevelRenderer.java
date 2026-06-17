package com.snek.engineersbliss.client.feature_handlers.overlays.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;




public final class RedstoneLevelRenderer {

    private static final int RADIUS = 8;

    private static final Identifier OVERLAY_TEXTURE =
            Identifier.fromNamespaceAndPath("engineers-bliss", "textures/block/power_levels/1.png");

    // Tiny offset above the block's top face so the quad doesn't z-fight with it.
    private static final double Y_OFFSET = 1.001;

    private RedstoneLevelRenderer() {}

    public static void register() {
        LevelRenderEvents.COLLECT_SUBMITS.register(RedstoneLevelRenderer::draw);
    }

    private static void draw(LevelRenderContext context) {
        Minecraft client = Minecraft.getInstance();
        Level level = client.level;
        if (level == null || client.player == null) return;

        MultiBufferSource.BufferSource bufferSource = context.bufferSource();
        Vec3 camera = context.levelState().cameraRenderState.pos;

        RenderType renderType = RenderTypes.entityCutout(OVERLAY_TEXTURE);
        VertexConsumer consumer = bufferSource.getBuffer(renderType);

        PoseStack matrices = new PoseStack();
        matrices.pushPose();

        BlockPos center = client.player.blockPosition();
        int light = 0xF000F0;
        int color = 0xFFFFFFFF;

        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dy = -RADIUS; dy <= RADIUS; dy++) {
                for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                    BlockPos pos = center.offset(dx, dy, dz);

                    double x0 = pos.getX() - camera.x;
                    double y = pos.getY() + Y_OFFSET - camera.y;
                    double z0 = pos.getZ() - camera.z;
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
}

