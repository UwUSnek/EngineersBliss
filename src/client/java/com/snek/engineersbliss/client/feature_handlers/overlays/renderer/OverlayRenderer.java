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
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.alternative_invisible_blocks.BarrierOverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.alternative_invisible_blocks.LightBlockOverlayProvider;
import com.snek.engineersbliss.client.feature_handlers.overlays.providers.alternative_invisible_blocks.StructureVoidOverlayProvider;
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

        // Power level
        new RedstoneLevelOverlayProvider(),
        new ComparatorLevelOverlayProvider(),
        new RailLevelOverlayProvider(),

        // Logic
        //FIXME only check the targeted block once per "targeted-block-overlay" provider instead of running this on every block
        new ComparatorLogicOverlayProvider(),

        // Invisible blocks //! Feature toggled from the Alt Textures screen
        new StructureVoidOverlayProvider(),
        new BarrierOverlayProvider(),
        new LightBlockOverlayProvider()
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
        for(final var chunkFeatureMaskEntry : OverlaysHandler.getFeatureWorldMap().entrySet()) {
            final ChunkPos chunkPos = chunkFeatureMaskEntry.getKey();
            final LevelChunk chunk = level.getChunk(chunkPos.x(), chunkPos.z());

            // If the chunk is currently visible by the player
            if(MinecraftUtils.isChunkVisible(chunk)) {

                // For each block in the chunk
                for(final var blockFeatureMaskEntry : chunkFeatureMaskEntry.getValue().entrySet()) {
                    final BlockPos pos = blockFeatureMaskEntry.getKey();
                    final BlockState state = chunk.getBlockState(pos);
                    final __base_OverlayAttachedData attachedData = blockFeatureMaskEntry.getValue().getSecond();
                    final Minecraft minecraft = Minecraft.getInstance();

                    // If the overlay should be rendered and is in view range, render it
                    for(final __base_OverlayProvider provider : providers) {
                        if(
                            provider.shouldRender(state, pos, attachedData) && (
                                provider.getMaxRenderDistance() == __base_OverlayProvider.RENDER_DISTANCE_UNLIMITED ||
                                provider.getMaxRenderDistance() > minecraft.player.position().distanceTo(new Vec3(pos))
                            )
                        ) {
                            if(provider instanceof final __base_TextureOverlayProvider p) {
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
                                else if(display == TextureProviderDisplay.BILLBOARD) {
                                    final float camYaw   = context.levelState().cameraRenderState.yRot;
                                    final float camPitch = context.levelState().cameraRenderState.xRot;
                                    matrices.mulPose(Axis.YP.rotationDegrees(180f - camYaw));
                                    matrices.mulPose(Axis.XP.rotationDegrees(-camPitch));
                                }


                                // Calculate text light level and color
                                final int light = 0xF000F0;
                                final int color = 0xFFFFFFFF;


                                // Add vertices and set properties
                                final double width = p.calcWidth(state, pos, attachedData);
                                final int overlay = OverlayTexture.NO_OVERLAY;
                                final Matrix4f pose = matrices.last().pose();
                                if(display == TextureProviderDisplay.CAMERA_LOCKED || display == TextureProviderDisplay.BILLBOARD) {
                                    final float anchor = p.calcAnchor(state, pos, attachedData);
                                    final float bottom = (float)(-width * 2 * anchor);
                                    final float top    = bottom + (float)(width * 2);
                                    vertexConsumer.addVertex(pose, (float)-width, bottom, 0).setUv(0f, 1f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 0f, 1f);
                                    vertexConsumer.addVertex(pose, (float)-width, top,    0).setUv(0f, 0f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 0f, 1f);
                                    vertexConsumer.addVertex(pose, (float)+width, top,    0).setUv(1f, 0f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 0f, 1f);
                                    vertexConsumer.addVertex(pose, (float)+width, bottom, 0).setUv(1f, 1f).setOverlay(overlay).setColor(color).setLight(light).setNormal(0f, 0f, 1f);
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
                            else if(provider instanceof final __base_TextOverlayProvider p) {
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
                                else if(display == TextureProviderDisplay.BILLBOARD) {
                                    final float camYaw   = context.levelState().cameraRenderState.yRot;
                                    final float camPitch = context.levelState().cameraRenderState.xRot;
                                    matrices.mulPose(Axis.YP.rotationDegrees(180f - camYaw));
                                    matrices.mulPose(Axis.XP.rotationDegrees(-camPitch));
                                }

                                // Convert font space to block space
                                final float scale = p.calcScale(state, pos, attachedData) / 18f;
                                matrices.scale(scale, -scale, scale);
                                if(display == TextureProviderDisplay.FIXED || display == TextureProviderDisplay.Y_LOCKED) {
                                    matrices.mulPose(Axis.XP.rotationDegrees(90f));
                                }

                                // Submit centered text with no shadow, default background, no outline
                                context.submitNodeCollector().submitText(
                                    matrices,
                                    -textWidth / 2f, -4f,
                                    Component.literal(text).getVisualOrderText(),
                                    false,
                                    Font.DisplayMode.SEE_THROUGH,
                                    0xF000F0,
                                    p.calcColor(state, pos, attachedData),
                                    0x99151515, // Background //TODO maybe make this a parameter?
                                    0x00000000  // Outline //TODO maybe make this a parameter?
                                );

                                matrices.popPose();
                            }
                            //TODO handle other types
                        }
                    }
                }
            }
        }
    }
}

