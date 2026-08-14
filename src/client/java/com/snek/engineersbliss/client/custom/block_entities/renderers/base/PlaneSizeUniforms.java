package com.snek.engineersbliss.client.custom.block_entities.renderers.base;

import java.nio.ByteBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import net.minecraft.client.renderer.DynamicUniformStorage;




public class PlaneSizeUniforms implements AutoCloseable {
    public static final int UBO_SIZE = new Std140SizeCalculator().putFloat().get();
    public static PlaneSizeUniforms INSTANCE = new PlaneSizeUniforms();




    private final DynamicUniformStorage<PlaneSize> storage = new DynamicUniformStorage<>("PlaneSize UBO", UBO_SIZE, 2);

    public GpuBufferSlice write(float planeSize) {
        return storage.writeUniform(new PlaneSize(planeSize));
    }
    public void reset() { storage.endFrame(); }
    public void close() { storage.close(); }

    public record PlaneSize(float value) implements DynamicUniformStorage.DynamicUniform {
        @Override public void write(final ByteBuffer buffer) {
            Std140Builder.intoBuffer(buffer).putFloat(this.value);
        }
    }
}