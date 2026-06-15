package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;








/**
 * This plugin implements dynamic models for specific blocks.
 * It registers custom models and textures during startup and fetches the right ones based on config settings when resolving the block state's model.
 */
public class AltTexturesModelPlugin implements ModelLoadingPlugin {
    private static final List<Block> blocks = List.of(
        Blocks.SLIME_BLOCK,
        Blocks.HONEY_BLOCK,
        Blocks.MANGROVE_ROOTS,
        Blocks.SCAFFOLDING

        //TODO redstone wire

        //TODO unify arrays with AltTexturesHandler
    );
    private static final Map<String, BlockStateModel> customModels = new ConcurrentHashMap<>();




    @Override
    public void initialize(final Context ctx) {



        // Register the custom models during startup
        // Minecraft needs to know about all the models and textures beforehand in order to use them for rendering
        ctx.modifyBlockModelOnLoad().register((model, context) -> {
            final BlockState state = context.state();
            final Block block = state.getBlock();
            if(!blocks.contains(block)) return model;


            final String stateId = calcStateId(state);
            return new BlockStateModel.UnbakedRoot() {
                @Override
                public void resolveDependencies(final ResolvableModel.Resolver resolver) {
                    model.resolveDependencies(resolver);
                    resolver.markDependency(Identifier.fromNamespaceAndPath("engineers-bliss", "block/" + stateId));
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
            final BlockState state = context.state();
            final Block block = state.getBlock();
            if(!blocks.contains(block)) return model;


            final String stateId = calcStateId(state);
            final Identifier customId = Identifier.fromNamespaceAndPath("engineers-bliss", "block/" + stateId);
            final BlockStateModelPart part = new Variant(customId).bake(context.baker());
            customModels.put(stateId, new SingleVariant(part));

            return model;
        });




        // This is what's called in runtime when the blocks need to be rendered.
        // The custom BlockStateModel applies a different model based on AltTexturesHandler's values
        // Particles and material flags are always vanilla, while the model parts are replaced by the custom model when needed

        ctx.modifyBlockModelAfterBake().register((model, context) -> {
            final BlockState state = context.state();
            final Block block = state.getBlock();
            if(!blocks.contains(block)) return model;


            final BlockStateModel vanilla = model;
            return new BlockStateModel() {
                @Override
                public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {
                    if(AltTexturesHandler.getFeature(block)) {
                        final BlockStateModel custom = customModels.get(calcStateId(state));
                        custom.collectParts(random, output);
                    }
                    else {
                        vanilla.collectParts(random, output);
                    }
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









    private static String calcStateId(BlockState state) {
        final Block block = state.getBlock();


        // Calculate custom state ID. This includes the trailing underscore
        String stateOnlyId = "";
        if(block == Blocks.SCAFFOLDING) {
            stateOnlyId = "_" + (state.getValue(ScaffoldingBlock.BOTTOM).booleanValue() ? "unstable" : "stable");
        }
        //TODO redstone


        // Merge with block ID and return
        final String id = BuiltInRegistries.BLOCK.getKey(block).getPath();
        return id + stateOnlyId;
    }
}