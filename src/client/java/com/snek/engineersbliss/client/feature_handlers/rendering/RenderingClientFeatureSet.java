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
        () -> new UiTxt("Render block outlines"),
        () -> new UiTxt("Whether to render block outlines.")
    );
    public static final ClientFeature<?> RENDER_BLOCKS = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_BLOCKS,
        () -> new UiTxt("Render blocks"),
        () -> new UiTxt("Whether to render blocks. This doesn't affect fluids and block entities.")
    );
    public static final ClientFeature<?> RENDER_FLUIDS = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_FLUIDS,
        () -> new UiTxt("Render fluids"),
        () -> new UiTxt("Whether to render fluids. This doesn't affect blocks and block entities.")
    );
    public static final ClientFeature<?> RENDER_BLOCK_ENTITIES = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_BLOCK_ENTITIES,
        () -> new UiTxt("Render block entities"),
        () -> new UiTxt("Whether to render block entities. This doesn't affect fluids and non-entity blocks.")
    );
    public static final ClientFeature<?> RENDER_ENTITIES = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_ENTITIES,
        () -> new UiTxt("Render entities"),
        () -> new UiTxt("Whether to render entities.")
    );
    public static final ClientFeature<?> RENDER_PARTICLES = new ClientFeature<>(
        RenderingServerFeatureSet.RENDER_PARTICLES,
        () -> new UiTxt("Render particles"),
        () -> new UiTxt("Whether to render particles.")
    );




    public static final ClientFeature<?> TARGET_HIDDEN_BLOCKS = new ClientFeature<>(
        RenderingServerFeatureSet.TARGET_HIDDEN_BLOCKS,
        () -> new UiTxt("Target hidden blocks"),
        () -> new UiTxt("Whether to target hidden blocks. When ON, this lets you break and interact with blocks that are not currently visible.")
    );
    public static final ClientFeature<?> SMOOTH_SHADING = new ClientFeature<>(
        RenderingServerFeatureSet.SMOOTH_SHADING,
        () -> new UiTxt("Smooth shading"),
        () -> new UiTxt("Fixes the weird shading Vanilla applies to certain blocks. This is most visible on Dirt Path and Farmland blocks.")
    );








    private static class Notices {
        // private static Supplier<UiTxt> SINGLE_PLAYER_ONLY = () -> (UiTxt)new UiTxt( //TODO remove if not used
        //     "This only works in Single Player."
        // ).red();
    }
}
