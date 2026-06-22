package com.snek.engineersbliss.client.feature_handlers.overlays.renderer;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.ComparatorLevelOverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.ComparatorLogicOverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.RailLevelOverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.RedstoneLevelOverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.__base_TextureOverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.TextureProviderDisplay;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.__base_OverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.__base_TextOverlayProvider;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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


    // A list constaining all overlay providers.
    //! This needs to be updated manually as new providers are added.
    private static List<__base_OverlayProvider> providers = List.of(
        new RedstoneLevelOverlayProvider(),
        new ComparatorLevelOverlayProvider(),
        new RailLevelOverlayProvider(),

        //FIXME only check the targeted block once per "targeted-block-overlay" provider instead of running this on every block
        new ComparatorLogicOverlayProvider()
    );


    /**
     * Registers the rendering logic
     */
    public static void register() {
        LevelRenderEvents.COLLECT_SUBMITS.register(OverlayRenderer::draw);
    }


    //FIXME only check the targeted block once per "targeted-block-overlay" provider instead of running this on every block
    //FIXME only check the targeted block once per "targeted-block-overlay" provider instead of running this on every block

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
                    final __base_OverlayAttachedData attachedData = blockFeatureMaskEntry.getValue().getSecond();

                    // Render each overlay provider that should be rendered, one by one, using the computed values
                    for(final __base_OverlayProvider provider : providers) {
                        if(provider instanceof final __base_TextureOverlayProvider p && p.shouldRender(state, pos, attachedData)) {
                            final TextureProviderDisplay display = p.getDisplay();

                            // Get texture path
                            final Identifier texturePath = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/" + p.calcTexturePath(state, pos, attachedData));

                            // Retrieve render info and buffer source
                            final MultiBufferSource.BufferSource bufferSource = context.bufferSource();
                            final RenderType renderType = RenderTypes.entityCutout(texturePath);
                            //TODO use entityTranslucent for semitransparent textures
                            final VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

                            // Create pose stack
                            final PoseStack matrices = new PoseStack();
                            matrices.pushPose();

                            // Calculate center position and translate pose to it
                            final Vec3 cameraPos = context.levelState().cameraRenderState.pos;
                            final double _y = pos.getY() - cameraPos.y + p.calcVerticalOffset(state, pos, attachedData);
                            final double _x = pos.getX() - cameraPos.x + 0.5;
                            final double _z = pos.getZ() - cameraPos.z + 0.5;
                            matrices.translate(_x, _y, _z);


                            // Apply custom rotation
                            final @Nullable Vector3f rot = p.calcPostRotation(state, pos, attachedData);
                            if(rot != null) {
                                matrices.mulPose(new Quaternionf().rotateXYZ(rot.x, rot.y, rot.z));
                            }


                            // Align to camera if needed
                            if(display == TextureProviderDisplay.CAMERA_LOCKED || display == TextureProviderDisplay.Y_LOCKED) {
                                final float camYaw = context.levelState().cameraRenderState.yRot;
                                matrices.mulPose(Axis.YP.rotationDegrees(180f - camYaw));
                            }


                            // Calculate text light level and color
                            final int light = 0xF000F0;
                            final int color = 0xFFFFFFFF;


                            // Add vertices and set properties
                            final double width = p.calcWidth(state, pos, attachedData);
                            final int overlay = OverlayTexture.NO_OVERLAY;
                            final Matrix4f pose = matrices.last().pose();
                            if(display == TextureProviderDisplay.CAMERA_LOCKED) {
                                vertexConsumer.addVertex(pose, (float)-width,                  0, 0).setUv(0f, 1f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 0f, 1f);
                                vertexConsumer.addVertex(pose, (float)-width, (float)(width * 2), 0).setUv(0f, 0f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 0f, 1f);
                                vertexConsumer.addVertex(pose, (float)+width, (float)(width * 2), 0).setUv(1f, 0f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 0f, 1f);
                                vertexConsumer.addVertex(pose, (float)+width,                  0, 0).setUv(1f, 1f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 0f, 1f);
                            }
                            else if(display == TextureProviderDisplay.FIXED || display == TextureProviderDisplay.Y_LOCKED) {
                                vertexConsumer.addVertex(pose, (float)-width, 0, (float)-width).setUv(0f, 0f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                                vertexConsumer.addVertex(pose, (float)-width, 0, (float)+width).setUv(0f, 1f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                                vertexConsumer.addVertex(pose, (float)+width, 0, (float)+width).setUv(1f, 1f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                                vertexConsumer.addVertex(pose, (float)+width, 0, (float)-width).setUv(1f, 0f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 1f, 0f);
                            }

                            // Reset pose
                            matrices.popPose();

                            //! No endBatch call needed
                            //! Let the game handle that normally
                        }
                        else if(provider instanceof final __base_TextOverlayProvider p && p.shouldRender(state, pos, attachedData)) {
                            final String text = p.calcText(state, pos, attachedData);
                            final Font font = client.font;
                            final float textWidth = font.width(text);

                            // Create pose stack
                            final PoseStack matrices = new PoseStack();
                            matrices.pushPose();

                            // Calculate center position and translate pose to it
                            final Vec3 cameraPos = context.levelState().cameraRenderState.pos;
                            final double _y = pos.getY() - cameraPos.y + p.calcVerticalOffset(state, pos, attachedData);
                            final double _x = pos.getX() - cameraPos.x + 0.5;
                            final double _z = pos.getZ() - cameraPos.z + 0.5;
                            matrices.translate(_x, _y, _z);

                            // Align to camera if needed
                            final TextureProviderDisplay display = p.getDisplay();
                            if(display == TextureProviderDisplay.CAMERA_LOCKED || display == TextureProviderDisplay.Y_LOCKED) {
                                final float camYaw = context.levelState().cameraRenderState.yRot;
                                matrices.mulPose(Axis.YP.rotationDegrees(180f - camYaw));
                            }

                            // Convert font space to block space
                            final float scale = p.calcScale(state, pos, attachedData) / 18f;
                            matrices.scale(scale, -scale, scale);
                            if(display == TextureProviderDisplay.FIXED || display == TextureProviderDisplay.Y_LOCKED) {
                                matrices.mulPose(Axis.XP.rotationDegrees(90f));
                            }

                            // Submit centered text with no shadow, no background, no outline
                            final MultiBufferSource.BufferSource bufferSource = context.bufferSource();
                            context.submitNodeCollector().submitText(
                                matrices,
                                -textWidth / 2f, -4f,
                                Component.literal(text).getVisualOrderText(),
                                false,
                                Font.DisplayMode.SEE_THROUGH,
                                0xF000F0,
                                p.calcColor(state, pos, attachedData),
                                0,
                                0
                            );

                            matrices.popPose();
                        }
                    }
                }
            }
            //TODO handle other types
        }
    }
}

