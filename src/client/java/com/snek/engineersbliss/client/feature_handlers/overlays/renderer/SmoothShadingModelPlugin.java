//FIXME make this a toggle in the rendering screen
//FIXME make this a toggle in the rendering screen
//FIXME make this a toggle in the rendering screen
//FIXME make this a toggle in the rendering screen
//FIXME make this a toggle in the rendering screen



package com.snek.engineersbliss.client.feature_handlers.overlays.renderer;

import java.util.List;
import java.util.function.Predicate;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.ShadeMode;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;








/**
 * Static model faces, including Vanilla's, are shaded based on a single direction instead of interpolating them (MC-223314).
 * This plugin Fixes this by forcing {@link ShadeMode#ENHANCED} on all models.
 */
public class SmoothShadingModelPlugin implements ModelLoadingPlugin {


    @Override
    public void initialize(final Context context) {
        context.modifyBlockModelAfterBake().register((model, afterBakeContext) -> new ShadeCorrectingModel(model));
    }



    /**
     * Wraps a baked BlockStateModel so every quad it emits is forced to ShadeMode.ENHANCED.
     */
    private static final class ShadeCorrectingModel implements BlockStateModel {
        private final BlockStateModel delegate;

        ShadeCorrectingModel(final BlockStateModel delegate) {
            this.delegate = delegate;
        }


        @Override
        public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {
            delegate.collectParts(random, output);
        }

        @Override
        public Material.Baked particleMaterial() {
            return delegate.particleMaterial();
        }

        @Override
        public int materialFlags() {
            return delegate.materialFlags();
        }

        @Override
        public Material.Baked particleMaterial(final BlockAndTintGetter level, final BlockPos pos, final BlockState state) {
            return delegate.particleMaterial(level, pos, state);
        }

        @Override
        public int materialFlags(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final RandomSource random) {
            return delegate.materialFlags(level, pos, state, random);
        }


        /**
         * The actual fix
         */
        @Override
        public void emitQuads(
            final QuadEmitter emitter,
            final BlockAndTintGetter level,
            final BlockPos pos,
            final BlockState state,
            final RandomSource random,
            final Predicate<@Nullable Direction> cullTest
        ) {
            emitter.pushTransform(quad -> {
                quad.shadeMode(ShadeMode.ENHANCED);
                return true;
            });
            try {
                delegate.emitQuads(emitter, level, pos, state, random, cullTest);
            }
            finally {
                emitter.popTransform();
            }
        }
    }
}