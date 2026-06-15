package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.ArrayList;
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
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RedstoneSide;








/**
 * This plugin implements dynamic models for specific blocks.
 * It registers custom models and textures during startup and fetches the right ones based on config settings when resolving the block state's model.
 */
public class AltTexturesModelPlugin implements ModelLoadingPlugin {
    private static final List<Block> blocks = List.of(
        Blocks.SLIME_BLOCK,
        Blocks.HONEY_BLOCK,
        Blocks.MANGROVE_ROOTS,
        Blocks.SCAFFOLDING,
        Blocks.REDSTONE_WIRE

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


            final List<String> stateIds = calcStateIds(state);
            return new BlockStateModel.UnbakedRoot() {
                @Override
                public void resolveDependencies(final ResolvableModel.Resolver resolver) {
                    model.resolveDependencies(resolver);
                    for(String stateId : stateIds) {
                        resolver.markDependency(Identifier.fromNamespaceAndPath("engineers-bliss", "block/" + stateId));
                    }
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


            // For each state ID (model part name) of the current blockstate, bake the model and store it locally
            for(String stateId : calcStateIds(state)) {
                final Identifier customId = Identifier.fromNamespaceAndPath("engineers-bliss", "block/" + stateId);
                final BlockStateModelPart part = new Variant(customId).bake(context.baker());
                customModels.put(stateId, new SingleVariant(part));
            }
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
                        for(String stateId : calcStateIds(state)) {
                            final BlockStateModel custom = customModels.get(stateId);
                            custom.collectParts(random, output);
                        }
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









    private static List<String> calcStateIds(BlockState state) {
        final Block block = state.getBlock();


        // Calculate custom state IDs. These include the trailing underscore but not the block's ID
        List<String> stateOnlyIds = new ArrayList<>();
        if(block == Blocks.SCAFFOLDING) {
            stateOnlyIds.add("_" + (state.getValue(ScaffoldingBlock.BOTTOM).booleanValue() ? "unstable" : "stable"));
        }
        else if(block == Blocks.REDSTONE_WIRE) {
            final RedstoneSide n = state.getValue(RedStoneWireBlock.NORTH);
            final RedstoneSide e = state.getValue(RedStoneWireBlock.EAST);
            final RedstoneSide s = state.getValue(RedStoneWireBlock.SOUTH);
            final RedstoneSide w = state.getValue(RedStoneWireBlock.WEST);

            // Central dot and power level
            if(
                n == RedstoneSide.NONE && e == RedstoneSide.NONE && s == RedstoneSide.NONE && w == RedstoneSide.NONE ||
                n != RedstoneSide.NONE && e != RedstoneSide.NONE ||
                e != RedstoneSide.NONE && s != RedstoneSide.NONE ||
                s != RedstoneSide.NONE && w != RedstoneSide.NONE ||
                w != RedstoneSide.NONE && n != RedstoneSide.NONE
            ) stateOnlyIds.add("/dot");
            stateOnlyIds.add("/" + state.getValue(RedStoneWireBlock.POWER));

            // Side connections
            if(n == RedstoneSide.SIDE) stateOnlyIds.add("/north_down");
            if(e == RedstoneSide.SIDE) stateOnlyIds.add("/east_down");
            if(s == RedstoneSide.SIDE) stateOnlyIds.add("/south_down");
            if(w == RedstoneSide.SIDE) stateOnlyIds.add("/west_down");
            if(n == RedstoneSide.UP) stateOnlyIds.add("/north_up");
            if(e == RedstoneSide.UP) stateOnlyIds.add("/east_up");
            if(s == RedstoneSide.UP) stateOnlyIds.add("/south_up");
            if(w == RedstoneSide.UP) stateOnlyIds.add("/west_up");
        }
        else {
            stateOnlyIds.add("");
        }




        // Merge with block ID and return
        final String id = BuiltInRegistries.BLOCK.getKey(block).getPath();
        List<String> r = new ArrayList<>();
        for(String stateOnlyId : stateOnlyIds) {
            r.add(id + stateOnlyId);
        }
        return r;
    }
}