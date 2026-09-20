package com.snek.engineersbliss.client.feature_handlers.rendering;

import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.rendering.RenderingServerFeatureSet;








@SuppressWarnings("java:S1905")
public class RenderingClientFeatureSet extends __base_ClientFeatureSet<RenderingServerFeatureSet> {
    public static final RenderingClientFeatureSet INSTANCE = new RenderingClientFeatureSet();
    private RenderingClientFeatureSet() {
        super(RenderingServerFeatureSet.INSTANCE, () -> new UiTxt("Rendering"));
    }





    public static final ClientFeature<?> RENDER_BLOCK_OUTLINES = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_BLOCK_OUTLINES,
        s(() -> new UiTxt("Render block outlines")),
        s(() -> new UiTxt("Whether to render block outlines."))
    );
    public static final ClientFeature<?> RENDER_BLOCKS = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_BLOCKS,
        s(() -> new UiTxt("Render blocks")),
        s(() -> new UiTxt("Whether to render blocks. This doesn't affect fluids and block entities."))
    );
    public static final ClientFeature<?> RENDER_FLUIDS = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_FLUIDS,
        s(() -> new UiTxt("Render fluids")),
        s(() -> new UiTxt("Whether to render fluids. This doesn't affect blocks and block entities."))
    );
    public static final ClientFeature<?> RENDER_BLOCK_ENTITIES = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_BLOCK_ENTITIES,
        s(() -> new UiTxt("Render block entities")),
        s(() -> new UiTxt("Whether to render block entities. This doesn't affect fluids and non-entity blocks."))
    );
    public static final ClientFeature<?> RENDER_ENTITIES = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_ENTITIES,
        s(() -> new UiTxt("Render entities")),
        s(() -> new UiTxt("Whether to render entities."))
    );
    public static final ClientFeature<?> RENDER_PARTICLES = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_PARTICLES,
        s(() -> new UiTxt("Render particles")),
        s(() -> new UiTxt("Whether to render particles."))
    );




    public static final ClientFeature<?> TARGET_HIDDEN_BLOCKS = new ClientFeature<>(
        RenderingServerFeatureSet.TARGET_HIDDEN_BLOCKS,
        s(() -> new UiTxt("Target hidden blocks")),
        s(() -> new UiTxt("Whether to target hidden blocks. When ON, this lets you break and interact with blocks that are not currently visible."))
    );
    public static final ClientFeature<?> SMOOTH_SHADING = new ClientFeature<>(
        RenderingServerFeatureSet.SMOOTH_SHADING,
        s(() -> new UiTxt("Smooth shading")),
        s(() -> new UiTxt("Fixes the weird shading Vanilla applies to certain blocks. This is most visible on Dirt Path and Farmland blocks."))
    );
}
