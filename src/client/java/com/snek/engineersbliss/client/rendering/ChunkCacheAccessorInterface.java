package com.snek.engineersbliss.client.rendering;

import java.util.concurrent.atomic.AtomicReferenceArray;

import net.minecraft.world.level.chunk.LevelChunk;




public interface ChunkCacheAccessorInterface {
    public AtomicReferenceArray<LevelChunk> getChunks();
}