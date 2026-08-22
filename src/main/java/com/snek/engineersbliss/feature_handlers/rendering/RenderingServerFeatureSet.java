package com.snek.engineersbliss.feature_handlers.rendering;

import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;








public class RenderingServerFeatureSet extends __base_ServerFeatureSet {
    public static int BLOCK_SHADER_LIMIT_INFINITE = 0xC0FFEE & 0xFFF;

    public static RenderingServerFeatureSet INSTANCE = new RenderingServerFeatureSet();
    private RenderingServerFeatureSet() { super("rendering"); }




    public static final ServerToggleFeature RENDER_BLOCK_OUTLINES = INSTANCE.registerFeature(new ServerToggleFeature("render_block_outlines", true));
    public static final ServerToggleFeature RENDER_BLOCKS         = INSTANCE.registerFeature(new ServerToggleFeature("render_blocks",         true));
    public static final ServerToggleFeature RENDER_FLUIDS         = INSTANCE.registerFeature(new ServerToggleFeature("render_fluids",         true));
    public static final ServerToggleFeature RENDER_BLOCK_ENTITIES = INSTANCE.registerFeature(new ServerToggleFeature("render_block_entities", true));
    public static final ServerToggleFeature RENDER_ENTITIES       = INSTANCE.registerFeature(new ServerToggleFeature("render_entities",       true));
    public static final ServerToggleFeature RENDER_PARTICLES      = INSTANCE.registerFeature(new ServerToggleFeature("render_particles",      true));




    public static final ServerToggleFeature TARGET_HIDDEN_BLOCKS  = INSTANCE.registerFeature(new ServerToggleFeature("target_hidden_blocks",  false));
    public static final ServerToggleFeature SMOOTH_SHADING        = INSTANCE.registerFeature(new ServerToggleFeature("smooth_shading",        true));
}
