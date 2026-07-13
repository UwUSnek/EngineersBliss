package com.snek.engineersbliss.client.feature_handlers.custom_items;

import java.util.List;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.util.TriState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.Material.Baked;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;




/**
 * A BlockStateModel that renders a full unshaded cube.
 */
public class UnshadedBlockStateModel implements BlockStateModel {
    private final Identifier atlas;
    private final Identifier spriteId;

    private Mesh mesh;
    private Material.Baked bakedMaterial;




    private final BlockStateModelPart asPart = new BlockStateModelPart() {
        @Override
        public void emitQuads(final QuadEmitter emitter, final Predicate<Direction> cullTest) {
            ensureBuilt();
            mesh.outputTo(emitter);
        }

        @Override
        public List<BakedQuad> getQuads(final Direction direction) {
            //! Legacy vanilla path
            //! Real Fabric rendering goes through emitQuads
            return List.of();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public Baked particleMaterial() {
            ensureBuilt();
            return bakedMaterial;
        }

        @Override
        public int materialFlags() {
            return 0;
        }
    };




    public UnshadedBlockStateModel(final Identifier spriteId) {
        this(AtlasIds.BLOCKS, spriteId);
    }

    public UnshadedBlockStateModel(final Identifier atlas, final Identifier spriteId) {
        this.atlas = atlas;
        this.spriteId = spriteId;
    }

    private void ensureBuilt() {
        if(mesh != null) return;

        final TextureAtlasSprite atlasSprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(atlas).getSprite(spriteId);
        bakedMaterial = new Material.Baked(atlasSprite, false);

        final MutableMesh mutableMesh = Renderer.get().mutableMesh();
        final QuadEmitter emitter = mutableMesh.emitter();

        for(final Direction dir : Direction.values()) {
            emitter.square(dir, 0, 0, 1, 1, 0);
            emitter.materialBake(bakedMaterial, MutableQuadView.BAKE_LOCK_UV);
            emitter.chunkLayer(ChunkSectionLayer.SOLID);
            emitter.ambientOcclusion(TriState.FALSE);
            emitter.diffuseShade(false);
            emitter.emit();
        }
        mesh = mutableMesh.immutableCopy();
    }

    @Override
    public void emitQuads(final QuadEmitter emitter, final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final RandomSource random, final Predicate<Direction> cullTest) {
        ensureBuilt();
        mesh.outputTo(emitter);
    }

    @Override
    public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {
        output.add(asPart);
    }

    @Override
    public Material.Baked particleMaterial() {
        ensureBuilt();
        return bakedMaterial;
    }

    @Override
    public int materialFlags() {
        return 0;
    }
}