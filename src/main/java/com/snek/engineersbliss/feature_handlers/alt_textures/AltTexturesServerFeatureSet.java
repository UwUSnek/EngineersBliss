package com.snek.engineersbliss.feature_handlers.alt_textures;

import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








public class AltTexturesServerFeatureSet extends __base_ServerFeatureSet {
    public static final AltTexturesServerFeatureSet INSTANCE = new AltTexturesServerFeatureSet();
    private AltTexturesServerFeatureSet() { super("alt_textures"); }


    //! Vanilla Beds are no longer block entities as of 26.3.
    //! No point in adding it as a feature just to remove it after one version.


    //! For whatever reason, Vanilla's Shelves and Campfires are rendered properly: The block model is static, while held items are rendered dynamically.
    //! However, their rendering logic does a lot of extra preparation steps even when there are no items to render.
    //! Campfires are also extremely laggy because of their particles.
    //! These all get simple logic optimizations, while Campfires also get particle suppression.


    //! Vanilla Bells are rendered dynamically but the supports are static.
    //! STATIC_BELLS simply replaces the bell part and keeps the Vanilla support.




    public static final ServerToggleFeature MINIMAL_REDSTONE_WIRE        = INSTANCE.registerFeature(new ServerToggleFeature("minimal_redstone_wire",        true));
    public static final ServerToggleFeature NO_REDSTONE_DUST_PARTICLES   = INSTANCE.registerFeature(new ServerToggleFeature("no_redstone_dust_particles",   true));
    public static final ServerToggleFeature NO_CAMPFIRE_PARTICLES        = INSTANCE.registerFeature(new ServerToggleFeature("no_campfire_particles",        true));
    public static final ServerToggleFeature NO_FIRE_PARTICLES            = INSTANCE.registerFeature(new ServerToggleFeature("no_fire_particles",            true));
    public static final ServerToggleFeature NO_LAVA_PARTICLES            = INSTANCE.registerFeature(new ServerToggleFeature("no_lava_particles",            true));
    public static final ServerToggleFeature NO_WATER_STREAM_PARTICLES    = INSTANCE.registerFeature(new ServerToggleFeature("no_water_stream_particles",    true));
    public static final ServerToggleFeature NO_DRIP_PARTICLES            = INSTANCE.registerFeature(new ServerToggleFeature("no_drip_particles",            true));
    public static final ServerToggleFeature TRANSPARENT_SLIME_BLOCK      = INSTANCE.registerFeature(new ServerToggleFeature("transparent_slime_block",      true));
    public static final ServerToggleFeature TRANSPARENT_HONEY_BLOCK      = INSTANCE.registerFeature(new ServerToggleFeature("transparent_honey_block",      true));
    public static final ServerToggleFeature UNOBSTRUCTIVE_MANGROVE_ROOTS = INSTANCE.registerFeature(new ServerToggleFeature("unobstructive_mangrove_roots", true));
    public static final ServerToggleFeature UNOBSTRUCTIVE_SCAFFOLDING    = INSTANCE.registerFeature(new ServerToggleFeature("unobstructive_scaffolding",    true));

    public static final ServerToggleFeature CONSISTENT_SLOPED_RAILS      = INSTANCE.registerFeature(new ServerToggleFeature("consistent_sloped_rails",      true));
    public static final ServerToggleFeature STATIC_CHESTS                = INSTANCE.registerFeature(new ServerToggleFeature("static_chests",                true));
    public static final ServerToggleFeature STATIC_SIGNS                 = INSTANCE.registerFeature(new ServerToggleFeature("static_signs",                 true));
    public static final ServerToggleFeature STATIC_BANNERS               = INSTANCE.registerFeature(new ServerToggleFeature("static_banners",               true));
    public static final ServerToggleFeature STATIC_DECORATED_POTS        = INSTANCE.registerFeature(new ServerToggleFeature("static_decorated_pots",        true));
    public static final ServerToggleFeature STATIC_BELLS                 = INSTANCE.registerFeature(new ServerToggleFeature("static_bells",                 true));
    public static final ServerToggleFeature STATIC_COPPER_GOLEM_STATUES  = INSTANCE.registerFeature(new ServerToggleFeature("static_copper_golem_statues",  true));
    public static final ServerToggleFeature STATIC_LECTERNS              = INSTANCE.registerFeature(new ServerToggleFeature("static_lecterns",              true));
    public static final ServerToggleFeature OPTIMIZED_SHELVES            = INSTANCE.registerFeature(new ServerToggleFeature("optimized_shelves",            true));
    public static final ServerToggleFeature OPTIMIZED_CAMPFIRES          = INSTANCE.registerFeature(new ServerToggleFeature("optimized_campfires",          true));

    public static final ServerToggleFeature REDSTONE_WIRE_3D             = INSTANCE.registerFeature(new ServerToggleFeature("redstone_wire_3d",             true));
    public static final ServerToggleFeature RAILS_3D                     = INSTANCE.registerFeature(new ServerToggleFeature("rails_3d",                     true));
    public static final ServerToggleFeature LADDERS_3D                   = INSTANCE.registerFeature(new ServerToggleFeature("ladders_3d",                   true));
    public static final ServerToggleFeature CHAINS_3D                    = INSTANCE.registerFeature(new ServerToggleFeature("chains_3d",                    true));
    public static final ServerToggleFeature BARS_3D                      = INSTANCE.registerFeature(new ServerToggleFeature("bars_3d",                      true));
    public static final ServerToggleFeature VINES_3D                     = INSTANCE.registerFeature(new ServerToggleFeature("vines_3d",                     true));
    public static final ServerToggleFeature GLOW_LICHEN_3D               = INSTANCE.registerFeature(new ServerToggleFeature("glow_lichen_3d",               true));
}



