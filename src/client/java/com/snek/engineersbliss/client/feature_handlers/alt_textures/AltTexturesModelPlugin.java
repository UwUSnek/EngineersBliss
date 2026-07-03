package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.math.Quadrant;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.GlowLichenPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.HoneyBlockPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.LadderPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.MangroveRootsPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.RedstoneWirePartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.ScaffoldingPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.SlimeBlockPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.VinesPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.CopperChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.ExposedCopperChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.IronChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.OxidizedCopperChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.WaxedCopperChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.WaxedExposedCopperChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.WaxedOxidizedCopperChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.WaxedWeatheredCopperChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.WeatheredCopperChainPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.ActivatorRailPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.DetectorRailPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.PoweredRailPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.RailPartProvider;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;








/**
 * This plugin implements dynamic models for specific blocks.
 * It registers custom models and textures during startup and fetches the right ones based on config settings when resolving the block state's model.
 */
public class AltTexturesModelPlugin implements PreparableModelLoadingPlugin<List<Identifier>> {
    private static final List<String>   PART_SUFFIXES_HORIZONTAL  = List.of("n", "e", "s", "w");
    private static final List<Quadrant> PART_QUADRANTS_HORIZONTAL = List.of(Quadrant.R0, Quadrant.R90, Quadrant.R180, Quadrant.R270);
    private static final List<String>   PART_SUFFIXES_VERTICAL    = List.of("u", "d");
    private static final List<Quadrant> PART_QUADRANTS_VERTICAL   = List.of(Quadrant.R270, Quadrant.R90);
    private static final List<Quadrant> PART_XROT_AXIS            = List.of(Quadrant.R0, Quadrant.R90, Quadrant.R90);
    private static final List<Quadrant> PART_YROT_AXIS            = List.of(Quadrant.R0, Quadrant.R0, Quadrant.R90);
    private static final List<String>   PART_SUFFIXES_AXIS        = List.of("y", "z", "x");
    private static final String TEMPLATE_MARKER_FILE_NAME = ".template";




    // A map containing baked custom models. The runtime resolver fetches models from here.
    private static final Map<Identifier, BlockStateModel> customModels = new HashMap<>();

    // A map containing the model part providers for each block
    private static final Map<Block, __base_PartProvider> partProviders = new HashMap<>();
    static {
        for(final var provider : List.of(
            new SlimeBlockPartProvider(),
            new HoneyBlockPartProvider(),
            new MangroveRootsPartProvider(),
            new ScaffoldingPartProvider(),
            new RedstoneWirePartProvider(),
            new LadderPartProvider(),
            new VinesPartProvider(),
            new GlowLichenPartProvider(),

            new RailPartProvider(),
            new PoweredRailPartProvider(),
            new ActivatorRailPartProvider(),
            new DetectorRailPartProvider(),

            new IronChainPartProvider(),
            new CopperChainPartProvider(),
            new ExposedCopperChainPartProvider(),
            new WeatheredCopperChainPartProvider(),
            new OxidizedCopperChainPartProvider(),
            new WaxedCopperChainPartProvider(),
            new WaxedExposedCopperChainPartProvider(),
            new WaxedWeatheredCopperChainPartProvider(),
            new WaxedOxidizedCopperChainPartProvider()
        )){
            partProviders.put(provider.getBlock(), provider);
        }
    }



    //! Called by the prepatable model plugin system once the plugin is registered.
    //! Registed from the client initializer.
    public static CompletableFuture<List<Identifier>> discoverModels(final PreparableReloadListener.SharedState sharedState, final Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            final List<Identifier> r = new ArrayList<>();
            final String root = "models/block";

            // Find all json models using the resource manager. Filter out stuff not from this mod and non-json files
            final @NotNull ResourceManager resourceManager = sharedState.resourceManager();
            resourceManager.listResources(
                root,
                id -> {
                    return id.getNamespace().equals(EngineerSBliss.MOD_ID) && id.getPath().endsWith(".json");
                }).keySet().forEach(id -> {

                    // Skip models in the same directory as files named ".template"
                    final @NotNull String path = id.getPath();
                    final String dir  = path.substring(0, path.lastIndexOf('/') + 1);
                    final Identifier templateMarker = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, dir + TEMPLATE_MARKER_FILE_NAME);
                    if(resourceManager.getResource(templateMarker).isPresent()) return;

                    // If the model is not a template, load it in the runtime map
                    final Identifier finalId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, path.substring("models/".length(), path.length() - ".json".length()));
                    r.add(finalId);
                    EngineerSBliss.LOGGER.info("Loaded dynamic custom model {}", finalId);
                }
            );
            return r;
        }, executor);
    }




    @Override //! Called automatically. No need to manually call from the client initializer
    public void initialize(final List<Identifier> modelIds, final Context initContext) {


        // Register the custom models during startup
        // Minecraft needs to know about all the models and textures beforehand in order to use them for rendering
        initContext.modifyBlockModelOnLoad().register((model, onLoadContext) -> {
            final @NotNull BlockState state = onLoadContext.state();
            final Block block = state.getBlock();
            if(!AltTextureFeature.hasFeature(block)) return model;


            return new BlockStateModel.UnbakedRoot() {
                @Override
                public void resolveDependencies(final ResolvableModel.Resolver resolver) {
                    model.resolveDependencies(resolver);
                    for(final Identifier modelId : modelIds) {
                        resolver.markDependency(modelId);
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

        initContext.modifyBlockModelBeforeBake().register((model, beforeBakeContext) -> {
            final @NotNull BlockState state = beforeBakeContext.state();
            final Block block = state.getBlock();
            if(!AltTextureFeature.hasFeature(block)) return model;


            // For each model ID
            for(@NotNull final Identifier modelId : modelIds) {

                // Bake one model per horizontal direction
                for (int i = 0; i < 4; ++i) {
                    final BlockStateModelPart part = new Variant(modelId)
                        .withYRot(PART_QUADRANTS_HORIZONTAL.get(i))
                        .bake(beforeBakeContext.baker())
                    ;
                    final Identifier rotatedModelId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, modelId.getPath() + "_" + PART_SUFFIXES_HORIZONTAL.get(i));
                    customModels.put(rotatedModelId, new SingleVariant(part));
                }

                // Bake up and down variants
                for (int i = 0; i < 2; ++i) {
                    final BlockStateModelPart part = new Variant(modelId)
                        .withYRot(Quadrant.R180)
                        .withXRot(PART_QUADRANTS_VERTICAL.get(i))
                        .bake(beforeBakeContext.baker())
                    ;
                    final Identifier rotatedModelId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, modelId.getPath() + "_" + PART_SUFFIXES_VERTICAL.get(i));
                    customModels.put(rotatedModelId, new SingleVariant(part));
                }

                // Bake axis-aligned variants
                for (int i = 0; i < 3; ++i) {
                    final BlockStateModelPart part = new Variant(modelId)
                        .withXRot(PART_XROT_AXIS.get(i))
                        .withYRot(PART_YROT_AXIS.get(i))
                        .bake(beforeBakeContext.baker())
                    ;
                    final Identifier axisModelId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, modelId.getPath() + "_" + PART_SUFFIXES_AXIS.get(i));
                    customModels.put(axisModelId, new SingleVariant(part));
                }
            }
            return model;
        });




        // This is what's called in runtime when the blocks need to be rendered.
        // The custom BlockStateModel applies a different model based on AltTexturesHandler's values
        // Particles and material flags are always vanilla, while the model parts are replaced by the custom model when needed

        initContext.modifyBlockModelAfterBake().register((model, afterBakeContext) -> {
            final @NotNull BlockState state = afterBakeContext.state();
            final Block block = state.getBlock();
            if(!AltTextureFeature.hasFeature(block)) return model;


            final @NotNull BlockStateModel vanilla = model;
            return new BlockStateModel() {
                @Override
                public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {

                    // Add custom parts
                    final __base_PartProvider partProvider = partProviders.get(state.getBlock());
                    if(partProvider != null) {
                        final @Nullable List<String> partNames = partProvider.calcPartNames(state);
                        if(partNames != null) for(final String partName : partNames) {
                            final Identifier partId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block/" + partName);
                            final BlockStateModel custom = customModels.get(partId);
                            if(custom != null) {
                                custom.collectParts(random, output);
                            }
                            else {
                                EngineerSBliss.LOGGER.error("Baked dynamic model part {} is unavailable", partId);
                                vanilla.collectParts(random, output);
                            }
                        }

                        // Add the vanilla parts if needed
                        if(partProvider.shouldKeepVanilla(state)) {
                            vanilla.collectParts(random, output);
                        }
                    }
                    else {
                        EngineerSBliss.LOGGER.error("Part provider for block {} is unavailable", BuiltInRegistries.BLOCK.getKey(block));
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
}



//TODO reuse this plugin to hide filtered blocks?
//TODO only if it can filter all the blocks and is called when rebuilding chunk sections/slices
//TODO and if culling follows geometry outputted by the plugin