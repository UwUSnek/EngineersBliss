package com.snek.engineersbliss.client.ui.widgets;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.resources.Identifier;








public class UiFeatureButton extends UiButton {
    private final              ClientFeature<?> clientFeature;
    private final @Nullable ServerToggleFeature serverFeature;


    public              ClientFeature<?> getClientFeature() { return clientFeature; }
    public @Nullable ServerToggleFeature getServerFeature() { return serverFeature; }




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

        // Call superconstructor in a safe way
        final boolean isToggleFeature = (feature.getServerFeature() instanceof ServerToggleFeature);
        final ServerToggleFeature _serverFeature = isToggleFeature ? (ServerToggleFeature)feature.getServerFeature() : null;
        final Txt                 _text          = isToggleFeature ? getToggleText(feature, _serverFeature) : new Txt();
        final Consumer<UiButton>  _pressCallback = isToggleFeature ? b -> onClick((UiFeatureButton)b, afterPressCallback) : null;
        super(x, y, w, h, _text, _pressCallback, '\0', TextAlignment.LEFT);
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;


        // Log error if needed
        if(!isToggleFeature) {
            EngineerSBliss.LOGGER.error(
                "{} created with a server feature of incompatible type: {}",
                getClass().getName(),
                feature.getServerFeature().getId(),
                new Throwable()
            );
        }


        // Set up sprite
        if(serverFeature != null) {
            final String bgSpritePath = String.format("%s/%s", serverFeature.getFeatureSet().getId(), serverFeature.getId());
            final Identifier bgSpriteId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, bgSpritePath);
            this.withSpriteBg(bgSpriteId, 1f, 1f);
        }
    }




    public static void onClick(final UiFeatureButton b, final @Nullable Consumer<UiButton> afterPressCallback) {
        final boolean newState = !ClientFeatureSync.getFeatureB(b.getServerFeature());
        b.setMessage(getToggleText(b.getClientFeature(), newState).get());
        ClientFeatureSync.setFeature(b.getServerFeature(), newState);
        if(afterPressCallback != null) afterPressCallback.accept(b);
        b.setFocused(false);
    }

    /**
     * Creates a Txt with format "<feature_name>: [ON/OFF]" based on the provided state.
     * @param feature The toggle feature.
     * @param state The state to display. True for ON, false for OFF.
     * @return The created Txt.
     */
    public static Txt getToggleText(final ClientFeature<?> feature, final boolean state) {
        return feature.calcName().cat(": " + (state ? "ON" : "OFF"));
    }

    /**
     * Creates a Txt with format "<feature_name>: [ON/OFF]" based on the current client-side state of the specified feature.
     * @param feature The toggle feature.
     * @return The created Txt.
     */
    public static Txt getToggleText(final ClientFeature<?> feature, final ServerToggleFeature stf) {
        return getToggleText(feature, ClientFeatureSync.getFeatureB(stf));
    }
}
