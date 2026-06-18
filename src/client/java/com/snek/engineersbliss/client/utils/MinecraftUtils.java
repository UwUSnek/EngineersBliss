package com.snek.engineersbliss.client.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.snek.engineersbliss.client.mixin.accessors.LevelRendererAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;




public class MinecraftUtils {
    private MinecraftUtils() { }








    /**
     Checks if the any part of the specified chunk is currently visible for the player.
     @param chunk The chunk to check.
    */
    public static boolean isChunkVisible(final LevelChunk chunk) {
        final var frustum = ((LevelRendererAccessor)Minecraft.getInstance().levelRenderer).getLevelRenderState().cameraRenderState.cullFrustum;
        final ChunkPos chunkPos = chunk.getPos();
        final double x0 = chunkPos.getMinBlockX();
        final double z0 = chunkPos.getMinBlockZ();
        return frustum.isVisible(new AABB(x0, chunk.getMinY(), z0, x0 + LevelChunkSection.SECTION_WIDTH, chunk.getMaxY(), z0 + LevelChunkSection.SECTION_WIDTH));
    }




    /**
     * Mark all chunk sections that match the provided condition for re-rendering.
     * This doesn't update lighting.
     * This can sometimes create false positives due to how Minecraft handles block pelettes.
     * @param predicate The predicate that checks wheter a block triggers a section refresh. Return true to refresh, false to skip.
     */
    public static void refreshSectionsContaining(final Predicate<BlockState> predicate) {
        final Minecraft minecraft = Minecraft.getInstance();
        final LevelRenderer renderer = minecraft.levelRenderer;
        final int minY = minecraft.level.getMinSectionY();
        for(final LevelChunk chunk : getLoadedChunks()) {
            final LevelChunkSection[] sections = chunk.getSections();
            final ChunkPos pos = chunk.getPos();
            for(int i = 0; i < sections.length; i++) {
                final LevelChunkSection section = sections[i];
                if(section == null || section.hasOnlyAir()) continue;
                if(section.maybeHas(predicate)) {
                    renderer.setSectionDirty(pos.x(), minY + i, pos.z());
                }
            }
        }
    }


    /**
     * Mark all chunk sections containing the specified block for re-rendering.
     * This doesn't update lighting.
     * This can sometimes create false positives due to how Minecraft handles block pelettes.
     * @param block The block to check for.
     */
    public static void refreshSectionsContaining(final Block block) {
        refreshSectionsContaining(state -> state.is(block));
    }


    /**
     * Mark all chunk sections containing the specified blocks for re-rendering.
     * This doesn't update lighting.
     * This can sometimes create false positives due to how Minecraft handles block pelettes.
     * @param blocks The blocks to check for.
     */
    public static void refreshSectionsContaining(final Collection<Block> blocks) {
        refreshSectionsContaining(state -> blocks.contains(state.getBlock()));
    }




    /**
     * Mark all chunk sections for re-rendering.
     * This doesn't update lighting.
     */
    public static void refreshRendering() {
        final Minecraft minecraft = Minecraft.getInstance();
        final LevelRenderer renderer = minecraft.levelRenderer;
        final int minY = minecraft.level.getMinSectionY();
        for(final LevelChunk chunk : getLoadedChunks()) {
            final LevelChunkSection[] sections = chunk.getSections();
            final ChunkPos pos = chunk.getPos();

            for(int i = 0; i < sections.length; i++) {
                final LevelChunkSection section = sections[i];
                if(section == null || section.hasOnlyAir()) continue;
                renderer.setSectionDirty(pos.x(), minY + i, pos.z());
            }
        }
    }




    /**
     * Calculates the amount of currently loaded chunks.
     * ! Ideally, one would access the cache's storage and get the length of the chunk array directly,
     * ! but the class is private and there is no easy way to access the instance and reflection breaks stuff.
     * ! This is a bit slower but it gets the job done. Just don't spam it.
     * @return The number of loaded chunks
     */
    public static int getLoadedChunkNumber() {
        int r = 0;
        final ClientLevel level = Minecraft.getInstance().level;
        final ClientChunkCache cache = level.getChunkSource();

        final int centerX = level.players().get(0).chunkPosition().x();
        final int centerZ = level.players().get(0).chunkPosition().z();
        final int radius = Minecraft.getInstance().options.renderDistance().get();

        for(int x = centerX - radius; x <= centerX + radius; x++) {
            for(int z = centerZ - radius; z <= centerZ + radius; z++) {
                final LevelChunk chunk = cache.getChunk(x, z, ChunkStatus.FULL, false);
                if(chunk != null) ++r;
            }
        }
        return r;
    }




    /**
     * Creates a list containing the currently loaded chunks.
     * @return The list of loaded chunks
     */
    public static List<LevelChunk> getLoadedChunks() {
        final List<LevelChunk> r = new ArrayList<>();
        final ClientLevel level = Minecraft.getInstance().level;
        final ClientChunkCache cache = level.getChunkSource();

        final int centerX = level.players().get(0).chunkPosition().x();
        final int centerZ = level.players().get(0).chunkPosition().z();
        final int radius = Minecraft.getInstance().options.renderDistance().get();

        for(int x = centerX - radius; x <= centerX + radius; x++) {
            for(int z = centerZ - radius; z <= centerZ + radius; z++) {
                final LevelChunk chunk = cache.getChunk(x, z, ChunkStatus.FULL, false);
                if(chunk != null) r.add(chunk);
            }
        }

        return r;
    }




    /**
     * Calculates the list of unique blocks present in the currently loaded chunks.
     * This can sometimes return false positives due to how Minecraft handles block pelettes.
     * @return The list of blocks in loaded chunks
     */
    public static List<Block> calcLoadedBlockList() {
        final Set<Block> r = new HashSet<>();
        for(final LevelChunk chunk : MinecraftUtils.getLoadedChunks()) {
            for(final LevelChunkSection section : chunk.getSections()) {
                section.getStates().getAll(state -> {
                    final Block block = state.getBlock();
                    if(block != Blocks.AIR) r.add(block);
                });
            }
        }
        return new ArrayList<>(r);
    }
}
