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
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.RedstoneSide;








/**
 * This plugin implements dynamic models for specific blocks.
 * It registers custom models and textures during startup and fetches the right ones based on config settings when resolving the block state's model.
 */
public class AltTexturesModelPlugin implements ModelLoadingPlugin {
    private static final Map<String, BlockStateModel> customModels = new ConcurrentHashMap<>();




    @Override
    public void initialize(final Context ctx) {



        // Register the custom models during startup
        // Minecraft needs to know about all the models and textures beforehand in order to use them for rendering
        ctx.modifyBlockModelOnLoad().register((model, context) -> {
            final BlockState state = context.state();
            final Block block = state.getBlock();
            if(!AltTextureFeature.hasFeature(block)) return model;


            final List<String> stateIds = new ArrayList<>();
            calcStateIds(state, stateIds, true);
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
            if(!AltTextureFeature.hasFeature(block)) return model;


            // For each state ID (model part name) of the current blockstate, bake the model and store it locally
            final List<String> stateIds = new ArrayList<>();
            calcStateIds(state, stateIds, true);
            for(String stateId : stateIds) {
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
            if(!AltTextureFeature.hasFeature(block)) return model;


            final BlockStateModel vanilla = model;
            return new BlockStateModel() {
                @Override
                public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {

                    // If the block has active features
                    List<String> stateIds = new ArrayList<>();
                    final boolean keepVanilla = calcStateIds(state, stateIds, false);
                    if(!stateIds.isEmpty()) {

                        // Loop through the requested parts and merge them together
                        for(String stateId : stateIds) {
                            final BlockStateModel custom = customModels.get(stateId);
                            custom.collectParts(random, output);
                        }
                    }

                    // Add the vanilla parts if needed
                    if(keepVanilla) {
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









    /**
     * Calculates a list of model parts based on the provided blockstate and the currently active texture features
     * @param state The blockstate to check.
     * @param ret A container for the output list of parts. This must be an empty list.
     * @param force True to assume all features are ON.
     * @return true if the Vanilla model needs to be added to the parts, false otherwise.
     */
    private static boolean calcStateIds(BlockState state, final List<String> ret, boolean force) {
        final Block block = state.getBlock();
        boolean keepVanilla = true;


        // Calculate custom state IDs. These include the trailing underscore but not the block's ID
        if(block == Blocks.SLIME_BLOCK) {
            if(force || AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK)) {
                keepVanilla = false;
                ret.add("slime_block/transparent/block");
            }
        }
        else if(block == Blocks.HONEY_BLOCK) {
            if(force || AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK)) {
                keepVanilla = false;
                ret.add("honey_block/transparent/block");
            }
        }
        else if(block == Blocks.MANGROVE_ROOTS) {
            if(force || AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS)) {
                keepVanilla = false;
                ret.add("mangrove_roots/unobstructive/block");
            }
        }
        else if(block == Blocks.SCAFFOLDING) {
            if(force || AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING)) {
                keepVanilla = false;
                final String stateName = state.getValue(ScaffoldingBlock.BOTTOM).booleanValue() ? "unstable" : "stable";
                ret.add("scaffolding/unobstructive/" + stateName);
            }
        }
        else if(block == Blocks.REDSTONE_WIRE) {
            if(force || AltTexturesHandler.getFeature(AltTextureFeature.LINE_REDSTONE_WIRE)) {
                keepVanilla = false;
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
                ) ret.add("redstone_wire/minimal/dot");

                // Side connections
                if(n == RedstoneSide.SIDE) ret.add("redstone_wire/minimal/north_down");
                if(e == RedstoneSide.SIDE) ret.add("redstone_wire/minimal/east_down");
                if(s == RedstoneSide.SIDE) ret.add("redstone_wire/minimal/south_down");
                if(w == RedstoneSide.SIDE) ret.add("redstone_wire/minimal/west_down");
                if(n == RedstoneSide.UP)   ret.add("redstone_wire/minimal/north_up");
                if(e == RedstoneSide.UP)   ret.add("redstone_wire/minimal/east_up");
                if(s == RedstoneSide.UP)   ret.add("redstone_wire/minimal/south_up");
                if(w == RedstoneSide.UP)   ret.add("redstone_wire/minimal/west_up");
            }
            if(force || AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_POWER_LEVELS)) {
                ret.add("redstone_wire/power_levels/" + state.getValue(RedStoneWireBlock.POWER));
            }
        }
        else if(block instanceof BaseRailBlock rail) {
            if(force || AltTexturesHandler.getFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS)) {
                final RailShape shape = state.getValue(rail.getShapeProperty());
                if(shape.isSlope()) {
                    keepVanilla = false;

                    //! Shape names have the format "ascending_<direction>" so i use that directly by removing "ascending_" as that matches the json file names perfectly
                    final String id = BuiltInRegistries.BLOCK.getKey(block).getPath();
                    String railModelName = "raised" + shape.getName().replace("ascending", "");
                    if(block != Blocks.RAIL) railModelName += state.getValue(BlockStateProperties.POWERED).booleanValue() ? "_on" : "_off";
                    ret.add("rails/consistent_sloped/" + id + "/" + railModelName);
                }
            }

            //! Rail power level isn't stored by Minecraft so this needs custom power source lookup logic
            if(force || AltTexturesHandler.getFeature(AltTextureFeature.RAIL_POWER_LEVELS)) {
                //TODO read from mixin map
                // ret.add("rails/power_levels/" + state.getValue(PoweredRailBlock.));
            }
        }
        else {
            return true;
        }


        return keepVanilla;
    }
}