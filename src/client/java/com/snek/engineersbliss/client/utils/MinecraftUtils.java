package com.snek.engineersbliss.client.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;




public class MinecraftUtils {
    private MinecraftUtils() { }




    /**
     * Mark all chunk sections containing the specified block for re-rendering.
     * This doesn't update lighting.
     * This can sometimes create false positives due to how Minecraft handles block pelettes.
     * @param block The block to check for.
     */
    public static void refreshSectionsContaining(Block block) {
        Minecraft minecraft = Minecraft.getInstance();
        LevelRenderer renderer = minecraft.levelRenderer;
        int minY = minecraft.level.getMinSectionY();
        for(LevelChunk chunk : getLoadedChunks()) {
            LevelChunkSection[] sections = chunk.getSections();
            ChunkPos pos = chunk.getPos();

            for(int i = 0; i < sections.length; i++) {
                LevelChunkSection section = sections[i];
                if(section == null || section.hasOnlyAir()) continue;
                if(section.getStates().maybeHas(state -> state.is(block))) {
                    renderer.setSectionDirty(pos.x(), minY + i, pos.z());
                }
            }
        }
    }




    /**
     * Mark all chunk sections for re-rendering.
     * This doesn't update lighting.
     */
    public static void refreshRendering() {
        Minecraft minecraft = Minecraft.getInstance();
        LevelRenderer renderer = minecraft.levelRenderer;
        int minY = minecraft.level.getMinSectionY();
        for(LevelChunk chunk : getLoadedChunks()) {
            LevelChunkSection[] sections = chunk.getSections();
            ChunkPos pos = chunk.getPos();

            for(int i = 0; i < sections.length; i++) {
                LevelChunkSection section = sections[i];
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
     * @return The list of blocks in loaded chunks
     */
    public static List<Block> calcLoadedBlockList() {
        final ClientLevel level = Minecraft.getInstance().level;
        final Set<Block> r = new HashSet<>();

        for(final LevelChunk chunk : MinecraftUtils.getLoadedChunks()) {
            final ChunkPos chunkPos = chunk.getPos();

            for(int x = chunkPos.getMinBlockX(); x < chunkPos.getMaxBlockX(); x++) {
                for(int z = chunkPos.getMinBlockZ(); z < chunkPos.getMaxBlockZ(); z++) {
                    for(int y = level.getMinY(); y < level.getMaxY(); y++) {
                        final Block block = chunk.getBlockState(new BlockPos(x, y, z)).getBlock();
                        if(block != Blocks.AIR) r.add(block);
                    }
                }
            }
        }
        return new ArrayList<>(r);
    }
}
