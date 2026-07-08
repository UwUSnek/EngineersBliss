package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.primitives.Chars;
import com.mojang.math.Quadrant;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.BellBlockPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.DecoratedPotPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.GlowLichenPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.HoneyBlockPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.LadderPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.MangroveRootsPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.RedstoneWirePartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.ScaffoldingPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.SlimeBlockPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.VinesPartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.ceiling_hanging.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall.*;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall_hanging.*;

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
public class AltTexturesModelPlugin implements PreparableModelLoadingPlugin<Map<Identifier, String>> {
    private static final List<Character> PART_SUFFIXES_HORIZONTAL  = List.of('n', 'e', 's', 'w');
    private static final List<Quadrant>  PART_QUADRANTS_HORIZONTAL = List.of(Quadrant.R0, Quadrant.R90, Quadrant.R180, Quadrant.R270);
    private static final List<Character> PART_SUFFIXES_VERTICAL    = List.of('u', 'd');
    private static final List<Quadrant>  PART_QUADRANTS_VERTICAL   = List.of(Quadrant.R270, Quadrant.R90);
    private static final List<Quadrant>  PART_XROT_AXIS            = List.of(Quadrant.R0, Quadrant.R90, Quadrant.R90);
    private static final List<Quadrant>  PART_YROT_AXIS            = List.of(Quadrant.R0, Quadrant.R0, Quadrant.R90);
    private static final List<Character> PART_SUFFIXES_AXIS        = List.of('y', 'z', 'x');

    // Templates/Variants info
    private static final String GENERATE_MARKER_PREFIX = ".gen-";
    private static final String TEMPLATE_MARKER_FILE_NAME = ".template";








    // This contains all the discovered models for the current block
    //! The type is called BlockStateModel and not BlockStateModelPart because Minecraft is goofy. It's a part, but it must be a BlockStateModel instance.
    private static final Map<Identifier, BlockStateModel> customModelParts = new ConcurrentHashMap<>();


    // A map containing all the assembled models for each possible BlockState. The runtime resolver fetches models from here
    // This doesn't include the vanilla model.
    private static Map<BlockState, List<BlockStateModel>> customModelsForStates = new ConcurrentHashMap<>();




    // A map containing the model part providers for each block
    private static final Map<Block, __base_PartProvider> partProviders = new HashMap<>();
    static {
        for(final var provider : List.of(
            new    SlimeBlockPartProvider(),
            new    HoneyBlockPartProvider(),
            new MangroveRootsPartProvider(),
            new   ScaffoldingPartProvider(),
            new  RedstoneWirePartProvider(),
            new        LadderPartProvider(),
            new         VinesPartProvider(),
            new    GlowLichenPartProvider(),
            new  DecoratedPotPartProvider(),
            new     BellBlockPartProvider(),

            new          RailPartProvider(),
            new   PoweredRailPartProvider(),
            new ActivatorRailPartProvider(),
            new  DetectorRailPartProvider(),

            new                 IronChainPartProvider(),
            new               CopperChainPartProvider(),
            new        ExposedCopperChainPartProvider(),
            new      WeatheredCopperChainPartProvider(),
            new       OxidizedCopperChainPartProvider(),
            new          WaxedCopperChainPartProvider(),
            new   WaxedExposedCopperChainPartProvider(),
            new WaxedWeatheredCopperChainPartProvider(),
            new  WaxedOxidizedCopperChainPartProvider(),

            new               NormalLanternPartProvider(),
            new                 SoulLanternPartProvider(),
            new               CopperLanternPartProvider(),
            new        ExposedCopperLanternPartProvider(),
            new      WeatheredCopperLanternPartProvider(),
            new       OxidizedCopperLanternPartProvider(),
            new          WaxedCopperLanternPartProvider(),
            new   WaxedExposedCopperLanternPartProvider(),
            new WaxedWeatheredCopperLanternPartProvider(),
            new  WaxedOxidizedCopperLanternPartProvider(),

            new   AcaciaStandingSignPartProvider(),
            new   BambooStandingSignPartProvider(),
            new    BirchStandingSignPartProvider(),
            new   CherryStandingSignPartProvider(),
            new  CrimsonStandingSignPartProvider(),
            new  DarkOakStandingSignPartProvider(),
            new   JungleStandingSignPartProvider(),
            new MangroveStandingSignPartProvider(),
            new      OakStandingSignPartProvider(),
            new  PaleOakStandingSignPartProvider(),
            new   SpruceStandingSignPartProvider(),
            new   WarpedStandingSignPartProvider(),

            new   AcaciaWallSignPartProvider(),
            new   BambooWallSignPartProvider(),
            new    BirchWallSignPartProvider(),
            new   CherryWallSignPartProvider(),
            new  CrimsonWallSignPartProvider(),
            new  DarkOakWallSignPartProvider(),
            new   JungleWallSignPartProvider(),
            new MangroveWallSignPartProvider(),
            new      OakWallSignPartProvider(),
            new  PaleOakWallSignPartProvider(),
            new   SpruceWallSignPartProvider(),
            new   WarpedWallSignPartProvider(),

            new   AcaciaCeilingHangingSignPartProvider(),
            new   BambooCeilingHangingSignPartProvider(),
            new    BirchCeilingHangingSignPartProvider(),
            new   CherryCeilingHangingSignPartProvider(),
            new  CrimsonCeilingHangingSignPartProvider(),
            new  DarkOakCeilingHangingSignPartProvider(),
            new   JungleCeilingHangingSignPartProvider(),
            new MangroveCeilingHangingSignPartProvider(),
            new      OakCeilingHangingSignPartProvider(),
            new  PaleOakCeilingHangingSignPartProvider(),
            new   SpruceCeilingHangingSignPartProvider(),
            new   WarpedCeilingHangingSignPartProvider(),

            new   AcaciaWallHangingSignPartProvider(),
            new   BambooWallHangingSignPartProvider(),
            new    BirchWallHangingSignPartProvider(),
            new   CherryWallHangingSignPartProvider(),
            new  CrimsonWallHangingSignPartProvider(),
            new  DarkOakWallHangingSignPartProvider(),
            new   JungleWallHangingSignPartProvider(),
            new MangroveWallHangingSignPartProvider(),
            new      OakWallHangingSignPartProvider(),
            new  PaleOakWallHangingSignPartProvider(),
            new   SpruceWallHangingSignPartProvider(),
            new   WarpedWallHangingSignPartProvider(),

            new               NormalChestPartProvider(),
            new                EnderChestPartProvider(),
            new              TrappedChestPartProvider(),
            new               CopperChestPartProvider(),
            new        ExposedCopperChestPartProvider(),
            new      WeatheredCopperChestPartProvider(),
            new       OxidizedCopperChestPartProvider(),
            new          WaxedCopperChestPartProvider(),
            new   WaxedExposedCopperChestPartProvider(),
            new WaxedWeatheredCopperChestPartProvider(),
            new  WaxedOxidizedCopperChestPartProvider(),

            new     WhiteBannerPartProvider(),
            new    OrangeBannerPartProvider(),
            new   MagentaBannerPartProvider(),
            new LightBlueBannerPartProvider(),
            new    YellowBannerPartProvider(),
            new      LimeBannerPartProvider(),
            new      PinkBannerPartProvider(),
            new      GrayBannerPartProvider(),
            new LightGrayBannerPartProvider(),
            new      CyanBannerPartProvider(),
            new    PurpleBannerPartProvider(),
            new      BlueBannerPartProvider(),
            new     BrownBannerPartProvider(),
            new     GreenBannerPartProvider(),
            new       RedBannerPartProvider(),
            new     BlackBannerPartProvider(),

            new     WhiteWallBannerPartProvider(),
            new    OrangeWallBannerPartProvider(),
            new   MagentaWallBannerPartProvider(),
            new LightBlueWallBannerPartProvider(),
            new    YellowWallBannerPartProvider(),
            new      LimeWallBannerPartProvider(),
            new      PinkWallBannerPartProvider(),
            new      GrayWallBannerPartProvider(),
            new LightGrayWallBannerPartProvider(),
            new      CyanWallBannerPartProvider(),
            new    PurpleWallBannerPartProvider(),
            new      BlueWallBannerPartProvider(),
            new     BrownWallBannerPartProvider(),
            new     GreenWallBannerPartProvider(),
            new       RedWallBannerPartProvider(),
            new     BlackWallBannerPartProvider(),

            new               CopperGolemStatuePartProvider(),
            new        ExposedCopperGolemStatuePartProvider(),
            new      WeatheredCopperGolemStatuePartProvider(),
            new       OxidizedCopperGolemStatuePartProvider(),
            new          WaxedCopperGolemStatuePartProvider(),
            new   WaxedExposedCopperGolemStatuePartProvider(),
            new WaxedWeatheredCopperGolemStatuePartProvider(),
            new  WaxedOxidizedCopperGolemStatuePartProvider()
        )){
            partProviders.put(provider.getBlock(), provider);
        }
    }








    //! Called by the prepatable model plugin system once the plugin is registered.
    //! Registed from the client initializer.
    public static CompletableFuture<Map<Identifier, String>> discoverModels(final PreparableReloadListener.SharedState sharedState, final Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            final Map<Identifier, String> r = new HashMap<>();
            final String root = "models/block";
            final @NotNull ResourceManager resourceManager = sharedState.resourceManager();

            resourceManager.listResources(root, id ->
                id.getNamespace().equals(EngineerSBliss.MOD_ID) && id.getPath().endsWith(".json")
            ).keySet().forEach(id -> {
                final @NotNull String path = id.getPath();
                final String dir = path.substring(0, path.lastIndexOf('/') + 1);

                final Identifier templateMarker = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, dir + TEMPLATE_MARKER_FILE_NAME);
                if(resourceManager.getResource(templateMarker).isPresent()) return;

                final String suffixes = findGenerateSuffixes(resourceManager, dir);
                if(suffixes == null) {
                    EngineerSBliss.LOGGER.error("Model directory {} doesn't define any variant.", dir);
                }
                else {
                    final Identifier finalId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, path.substring("models/".length(), path.length() - ".json".length()));
                    r.put(finalId, suffixes);
                    EngineerSBliss.LOGGER.debug("Loaded dynamic custom model {} (variants: {})", finalId, suffixes);
                }
            });
            return r;
        }, executor);
    }

    @Nullable
    private static String findGenerateSuffixes(final ResourceManager resourceManager, final String dir) {
        final String dirRoot = dir.substring(0, dir.length() - 1); // strip trailing '/'
        return resourceManager.listResources(dirRoot, i ->
            i.getNamespace().equals(EngineerSBliss.MOD_ID) &&
            i.getPath().startsWith(dir) &&
            i.getPath().startsWith(GENERATE_MARKER_PREFIX, dir.length())
        )
            .keySet().stream()
            .findFirst()
            .map(id -> id.getPath().substring(dir.length() + GENERATE_MARKER_PREFIX.length()))
            .orElse(null)
        ;
    }







    @Override //! Called automatically. No need to manually call from the client initializer
    public void initialize(final Map<Identifier, String> requestedModelVariants, final Context initContext) {


        // Register the custom models during startup
        // Minecraft needs to know about all the models and textures beforehand in order to use them for rendering
        initContext.modifyBlockModelOnLoad().register((model, onLoadContext) -> {
            final @NotNull BlockState state = onLoadContext.state();
            final Block block = state.getBlock();
            if(!AltTextureFeature.hasFeature(block)) return model;


            return new BlockStateModel.UnbakedRoot() {
                @Override
                public void resolveDependencies(final ResolvableModel.Resolver resolver) {
                    final __base_PartProvider partProvider = partProviders.get(block);
                    model.resolveDependencies(resolver);
                    if(partProvider != null) {
                        for(final Identifier id : partProvider.calcPartIds(state, false)) {
                            resolver.markDependency(id);
                        }
                    }
                    else {
                        EngineerSBliss.LOGGER.error("Part provider for block {} is unavailable", BuiltInRegistries.BLOCK.getKey(block));
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


            // For each model ID of this blockstate
            final __base_PartProvider partProvider = partProviders.get(block);
            if(partProvider != null) {
                for(final Identifier partId : partProvider.calcPartIds(state, false)) {
                    final String variantSuffixes = requestedModelVariants.get(partId);

                    // Bake one model per horizontal direction
                    for(int i = 0; i < 4; ++i) {
                        final String suffix = String.valueOf(PART_SUFFIXES_HORIZONTAL.get(i));
                        if(variantSuffixes == null || !variantSuffixes.contains(suffix)) continue;
                        final BlockStateModelPart part = new Variant(partId)
                            .withYRot(PART_QUADRANTS_HORIZONTAL.get(i))
                            .bake(beforeBakeContext.baker());
                        final Identifier rotatedModelId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, partId.getPath() + "_" + suffix);
                        customModelParts.put(rotatedModelId, new SingleVariant(part));
                    }

                    // Bake up and down variants
                    for(int i = 0; i < 2; ++i) {
                        final String suffix = String.valueOf(PART_SUFFIXES_VERTICAL.get(i));
                        if(variantSuffixes == null || !variantSuffixes.contains(suffix)) continue;
                        final BlockStateModelPart part = new Variant(partId)
                            .withYRot(Quadrant.R180)
                            .withXRot(PART_QUADRANTS_VERTICAL.get(i))
                            .bake(beforeBakeContext.baker());
                        final Identifier rotatedModelId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, partId.getPath() + "_" + suffix);
                        customModelParts.put(rotatedModelId, new SingleVariant(part));
                    }

                    // Bake axis-aligned variants
                    for(int i = 0; i < 3; ++i) {
                        final String suffix = String.valueOf(PART_SUFFIXES_AXIS.get(i));
                        if(variantSuffixes == null || !variantSuffixes.contains(suffix)) continue;
                        final BlockStateModelPart part = new Variant(partId)
                            .withXRot(PART_XROT_AXIS.get(i))
                            .withYRot(PART_YROT_AXIS.get(i))
                            .bake(beforeBakeContext.baker());
                        final Identifier axisModelId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, partId.getPath() + "_" + suffix);
                        customModelParts.put(axisModelId, new SingleVariant(part));
                    }
                }
            }


            // Return the model
            return model;
        });








        // This is what's called in runtime when the blocks need to be rendered.
        // The custom BlockStateModel applies a different model based on AltTexturesHandler's values
        // Particles and material flags are always vanilla, while the model parts are replaced by the custom model when needed

        // This also builds the BlockState -> List<ModelPart> map for O(1) lookup on first call

        initContext.modifyBlockModelAfterBake().register((vanilla, afterBakeContext) -> {
            final @NotNull BlockState state = afterBakeContext.state();
            final Block block = state.getBlock();
            if(!AltTextureFeature.hasFeature(block)) return vanilla;


            return new BlockStateModel() {
                @Override
                public void collectParts(final RandomSource random, final List<BlockStateModelPart> output) {
                    boolean keepVanilla = true;


                    // Compute and cache parts for this BlockState
                    //! This must be done during the first instance of actual rendering workload
                    //! because Minecraft itself loads models lazily. Trying to load all the parts before any rendering occurs would result in missing cache entries
                    final __base_PartProvider partProvider = partProviders.get(block);
                    if(partProvider != null) {
                        final List<BlockStateModel> cachedParts = customModelsForStates.computeIfAbsent(state, s -> {
                            final List<BlockStateModel> collected = new ArrayList<>();

                            for(final Identifier partId : partProvider.calcPartIds(s, true)) {
                                final BlockStateModel custom = customModelParts.get(partId);
                                if(custom != null) {
                                    collected.add(custom);
                                }
                                else {
                                    EngineerSBliss.LOGGER.error("Baked dynamic model part {} is unavailable", partId);
                                }
                            }
                            return collected;
                        });



                        // Add custom model parts if needed (list can be empty but never null)
                        if(partProvider.shouldUseCustom(state)) {
                            for(final BlockStateModel cachedPart : cachedParts) {
                                cachedPart.collectParts(random, output);
                            }
                        }
                        keepVanilla = partProvider.shouldKeepVanilla(state);
                    }
                    //! No need to print the missing part provider error here. Previous steps already did that


                    // Add vanilla part if needed
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
}