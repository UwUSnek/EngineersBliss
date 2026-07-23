package com.snek.engineersbliss.client.ui.widgets;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;

import net.minecraft.resources.Identifier;








public class UiFeatureButton extends UiButton {
    private final              ClientFeature<?> clientFeature;
    private final @Nullable ServerToggleFeature serverFeature;


    public              ClientFeature<?> getClientFeature() { return clientFeature; }
    public @Nullable ServerToggleFeature getServerFeature() { return serverFeature; }


    private final Identifier bgSpriteId;




    public UiFeatureButton(final ClientFeature<?> feature) {
        this(feature, null);
    }
    public UiFeatureButton(final ClientFeature<?> feature, final @Nullable Consumer<UiButton> afterPressCallback) {
        this(0, 0, 0, 0, feature, afterPressCallback);
    }
    public UiFeatureButton(final int x, final int y, final int w, final int h, final ClientFeature<?> feature) {
        this(x, y, w, h, feature, null);
    }


    public UiFeatureButton(final int x, final int y, final int w, final int h, final ClientFeature<?> feature, final @Nullable Consumer<UiButton> afterPressCallback) {

        // Throw exception if not a ServerToggleFeature
        final @NotNull __base_ServerFeature<?> genericServerFeature = feature.getServerFeature();
        if(!(genericServerFeature instanceof ServerToggleFeature)) {
            throw new IllegalArgumentException(
                "UiFeatureButton created with a server feature of incompatible type: " +
                genericServerFeature.getClass().getName()
            );
        }

        // Proceed with normal initialization
        final ServerToggleFeature _serverFeature = (ServerToggleFeature)genericServerFeature;
        super(x, y, w, h, getToggleText(feature, _serverFeature), b -> onClick((UiFeatureButton)b, afterPressCallback), '\0', TextAlignment.LEFT);
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;

        // Calculate sprite id
        final String bgSpritePath = String.format("%s/%s", serverFeature.getFeatureSet().getId(), serverFeature.getId());
        this.bgSpriteId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, bgSpritePath);
    }

    //! Update sprite height and label offset when the height is changed
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        this.withSpriteBg(bgSpriteId, 1f, height + Layout.textMarginPx);
    }





    public static void onClick(final UiFeatureButton b, final @Nullable Consumer<UiButton> afterPressCallback) {
        final boolean newState = !ClientFeatureSync.getFeatureB(b.getServerFeature());
        b.setLabel(getToggleText(b.getClientFeature(), newState));
        ClientFeatureSync.setFeature(b.getServerFeature(), newState);
        if(afterPressCallback != null) afterPressCallback.accept(b);
        b.setFocused(false);
    }

    /**
     * Creates a UiTxt with format "<feature_name>: [ON/OFF]" based on the provided state.
     * @param feature The toggle feature.
     * @param state The state to display. True for ON, false for OFF.
     * @return The created UiTxt.
     */
    public static UiTxt getToggleText(final ClientFeature<?> feature, final boolean state) {
        return (UiTxt)feature.calcName().cat(": " + (state ? "ON" : "OFF"));
    }

    /**
     * Creates a UiTxt with format "<feature_name>: [ON/OFF]" based on the current client-side state of the specified feature.
     * @param feature The toggle feature.
     * @return The created UiTxt.
     */
    public static UiTxt getToggleText(final ClientFeature<?> feature, final ServerToggleFeature stf) {
        return getToggleText(feature, ClientFeatureSync.getFeatureB(stf));
    }
}
