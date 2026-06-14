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








public class AltTexturesModelPlugin implements ModelLoadingPlugin {
    public static final AtomicReference<BlockStateModel> customSlimeModel = new AtomicReference<>(null);

    @Override
    public void initialize(Context ctx) {




        //
        ctx.modifyBlockModelOnLoad().register((model, context) -> {
            if (!context.state().is(Blocks.SLIME_BLOCK)) return model;

            return new BlockStateModel.UnbakedRoot() {
                @Override
                public void resolveDependencies(ResolvableModel.Resolver resolver) {
                    model.resolveDependencies(resolver);
                    resolver.markDependency(
                        Identifier.fromNamespaceAndPath("engineers-bliss", "block/slime_block")
                    );
                    System.out.println("[EB] marked dependency in UnbakedRoot");
                }

                @Override
                public BlockStateModel bake(BlockState blockState, ModelBaker baker) {
                    return model.bake(blockState, baker);
                }

                @Override
                public Object visualEqualityGroup(BlockState blockState) {
                    return model.visualEqualityGroup(blockState);
                }
            };
        });




        //
        ctx.modifyBlockModelBeforeBake().register((model, context) -> {
            if (!context.state().is(Blocks.SLIME_BLOCK)) return model;

            Identifier customId = Identifier.fromNamespaceAndPath("engineers-bliss", "block/slime_block");
            BlockStateModelPart part = new Variant(customId).bake(context.baker());
            customSlimeModel.set(new SingleVariant(part));

            return model;
        });




        //
        ctx.modifyBlockModelAfterBake().register((model, context) -> {
            if (!context.state().is(Blocks.SLIME_BLOCK)) return model;

            BlockStateModel vanilla = model;
            return new BlockStateModel() {
                @Override
                public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
                    BlockStateModel custom = customSlimeModel.get();
                    if (AltTexturesHandler.getTransparentSlimeBlock() && custom != null) {
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