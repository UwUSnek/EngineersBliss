package com.snek.engineersbliss.feature_handlers.alt_textures;

import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








public class AltTextureServerFeatureSet extends __base_ServerFeatureSet {
    private AltTextureServerFeatureSet(final String id, final String displayName) { super(id, displayName); }
    public static final AltTextureServerFeatureSet INSTANCE = new AltTextureServerFeatureSet("alt_textures", "Alternative Textures");


    //! Vanilla Beds are no longer block entities as of 26.3.
    //! No point in adding it as a feature just to remove it after one version.


    //! For whatever reason, Vanilla's Shelves and Campfires are rendered properly: The block model is static, while held items are rendered dynamically.
    //! However, their rendering logic does a lot of extra preparation steps even when there are no items to render.
    //! Campfires are also extremely laggy because of their particles.
    //! These all get simple logic optimizations, while Campfires also get particle suppression.


    //! Vanilla Bells are rendered dynamically but the supports are static.
    //! STATIC_BELLS simply replaces the bell part and keeps the Vanilla support.




    public static final ServerToggleFeature MINIMAL_REDSTONE_WIRE        = INSTANCE.registerFeature(new ServerToggleFeature("minimal_redstone_wire",        "Minimal Redstone Wire",             true));
    public static final ServerToggleFeature NO_REDSTONE_DUST_PARTICLES   = INSTANCE.registerFeature(new ServerToggleFeature("no_redstone_dust_particles",   "Disable Redstone dust particles",   true));
    public static final ServerToggleFeature NO_CAMPFIRE_PARTICLES        = INSTANCE.registerFeature(new ServerToggleFeature("no_campfire_particles",        "Disable Campfire particles",        true));
    public static final ServerToggleFeature NO_FIRE_PARTICLES            = INSTANCE.registerFeature(new ServerToggleFeature("no_fire_particles",            "Disable Fire particles",            true));
    public static final ServerToggleFeature NO_LAVA_PARTICLES            = INSTANCE.registerFeature(new ServerToggleFeature("no_lava_particles",            "Disable Lava particles",            true));
    public static final ServerToggleFeature NO_WATER_STREAM_PARTICLES    = INSTANCE.registerFeature(new ServerToggleFeature("no_water_stream_particles",    "Disable Water Stream particles",    true));
    public static final ServerToggleFeature NO_DRIP_PARTICLES            = INSTANCE.registerFeature(new ServerToggleFeature("no_drip_particles",            "Disable drip particles",            true));
    public static final ServerToggleFeature TRANSPARENT_SLIME_BLOCK      = INSTANCE.registerFeature(new ServerToggleFeature("transparent_slime_block",      "Transparent Slime Blocks",          true));
    public static final ServerToggleFeature TRANSPARENT_HONEY_BLOCK      = INSTANCE.registerFeature(new ServerToggleFeature("transparent_honey_block",      "Transparent Honey Blocks",          true));
    public static final ServerToggleFeature UNOBSTRUCTIVE_MANGROVE_ROOTS = INSTANCE.registerFeature(new ServerToggleFeature("unobstructive_mangrove_roots", "Unobstructive Mangrove Roots",      true));
    public static final ServerToggleFeature UNOBSTRUCTIVE_SCAFFOLDING    = INSTANCE.registerFeature(new ServerToggleFeature("unobstructive_scaffolding",    "Unobstructive Scaffolding",         true));


    public static final ServerToggleFeature CONSISTENT_SLOPED_RAILS      = INSTANCE.registerFeature(new ServerToggleFeature("consistent_sloped_rails",      "Consistent sloped Rails",           true));
    public static final ServerToggleFeature STATIC_CHESTS                = INSTANCE.registerFeature(new ServerToggleFeature("static_chests",                "Static Chest models",               true));
    public static final ServerToggleFeature STATIC_SIGNS                 = INSTANCE.registerFeature(new ServerToggleFeature("static_signs",                 "Static Sign models",                true));
    public static final ServerToggleFeature STATIC_BANNERS               = INSTANCE.registerFeature(new ServerToggleFeature("static_banners",               "Static Banner models",              true));
    public static final ServerToggleFeature STATIC_DECORATED_POTS        = INSTANCE.registerFeature(new ServerToggleFeature("static_decorated_pots",        "Static Decorated Pot models",       true));
    public static final ServerToggleFeature STATIC_BELLS                 = INSTANCE.registerFeature(new ServerToggleFeature("static_bells",                 "Static Bell models",                true));
    public static final ServerToggleFeature STATIC_COPPER_GOLEM_STATUES  = INSTANCE.registerFeature(new ServerToggleFeature("static_copper_golem_statues",  "Static Copper Golem Statue models", true));
    public static final ServerToggleFeature STATIC_LECTERNS              = INSTANCE.registerFeature(new ServerToggleFeature("static_lecterns",              "Static Lectern models",             true));
    public static final ServerToggleFeature OPTIMIZED_SHELVES            = INSTANCE.registerFeature(new ServerToggleFeature("optimized_shelves",            "Optimized Shelf models",            true));
    public static final ServerToggleFeature OPTIMIZED_CAMPFIRES          = INSTANCE.registerFeature(new ServerToggleFeature("optimized_campfires",          "Optimized Campfire models",         true));


    public static final ServerToggleFeature REDSTONE_WIRE_3D             = INSTANCE.registerFeature(new ServerToggleFeature("redstone_wire_3d",             "3D Redstone Wire",                  true));
    public static final ServerToggleFeature RAILS_3D                     = INSTANCE.registerFeature(new ServerToggleFeature("rails_3d",                     "3D Rails",                          true));
    public static final ServerToggleFeature LADDERS_3D                   = INSTANCE.registerFeature(new ServerToggleFeature("ladders_3d",                   "3D Ladders",                        true));
    public static final ServerToggleFeature CHAINS_3D                    = INSTANCE.registerFeature(new ServerToggleFeature("chains_3d",                    "3D Chains",                         true));
    public static final ServerToggleFeature BARS_3D                      = INSTANCE.registerFeature(new ServerToggleFeature("bars_3d",                      "3D Bars",                           true));
    public static final ServerToggleFeature VINES_3D                     = INSTANCE.registerFeature(new ServerToggleFeature("vines_3d",                     "3D Vines",                          true));
    public static final ServerToggleFeature GLOW_LICHEN_3D               = INSTANCE.registerFeature(new ServerToggleFeature("glow_lichen_3d",               "3D Glow Lichen",                    true));
}



