package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;








/**
 * This plugin implements dynamic models for specific blocks.
 * It registers custom models and textures during startup and fetches the right ones based on config settings when resolving the block state's model.
 */
public class AltTexturesModelPlugin implements ModelLoadingPlugin {
    public static final AtomicReference<BlockStateModel> customSlimeModel = new AtomicReference<>(null);

    @Override
    public void initialize(Context ctx) {




        // Register the custom models during startup
        // Minecraft needs to know about all the models and textures beforehand in order to use them for rendering

        ctx.modifyBlockModelOnLoad().register((model, context) -> {
            if(!context.state().is(Blocks.SLIME_BLOCK)) return model;

            return new BlockStateModel.UnbakedRoot() {
                @Override
                public void resolveDependencies(final ResolvableModel.Resolver resolver) {
                    model.resolveDependencies(resolver);
                    resolver.markDependency(Identifier.fromNamespaceAndPath("engineers-bliss", "block/slime_block"));
                }

                @Override
                public BlockStateModel bake(final BlockState blockState, final ModelBaker baker) {
                    return model.bake(blockState, baker);
                }

                @Override
                public Object visualEqualityGroup(final BlockState blockState) {
                    return model.visualEqualityGroup(blockState);
                }
            };
        });




        // This step yoinks the loaded custom model and stores a local reference to it so it can be used when needed

        ctx.modifyBlockModelBeforeBake().register((model, context) -> {
            if(!context.state().is(Blocks.SLIME_BLOCK)) return model;

            final Identifier customId = Identifier.fromNamespaceAndPath("engineers-bliss", "block/slime_block");
            final BlockStateModelPart part = new Variant(customId).bake(context.baker());
            customSlimeModel.set(new SingleVariant(part));

            return model;
        });




        // This is what's called in runtime when the blocks need to be rendered.
        // The custom BlockStateModel applies a different model based on AltTexturesHandler's values
        // Particles and material flags are always vanilla, while the model parts are replaced by the custom model when needed

        ctx.modifyBlockModelAfterBake().register((model, context) -> {
            if(!context.state().is(Blocks.SLIME_BLOCK)) return model;

            final BlockStateModel vanilla = model;
            return new BlockStateModel() {
                @Override
                public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {
                    final BlockStateModel custom = customSlimeModel.get();
                    if(AltTexturesHandler.getTransparentSlimeBlock() && custom != null) {
                        custom.collectParts(random, output);
                        return;
                    }
                    vanilla.collectParts(random, output);
                }

                @Override
                public Material.Baked particleMaterial() {
                    return vanilla.particleMaterial();
                }

                @Override
                public int materialFlags() {
                    return vanilla.materialFlags();
                }
            };
        });
    }
}