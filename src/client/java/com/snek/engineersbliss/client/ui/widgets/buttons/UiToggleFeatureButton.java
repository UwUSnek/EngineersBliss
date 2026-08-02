package com.snek.engineersbliss.client.ui.widgets.buttons;

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
import com.snek.engineersbliss.client.ui.widgets.base.DualPreviewFeatureInputWidget;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.resources.Identifier;








public class UiToggleFeatureButton extends UiToggleButton implements DualPreviewFeatureInputWidget {
    private final              ClientFeature<?> clientFeature;
    private final @Nullable ServerToggleFeature serverFeature;


    @Override public              ClientFeature<?> getClientFeature() { return clientFeature; }
    @Override public @Nullable ServerToggleFeature getServerFeature() { return serverFeature; }
    @Override public String getLeftPreviewSuffix () { return "on";  }
    @Override public String getRightPreviewSuffix() { return "off"; }
    @Override public String getLeftTitle         () { return "ON";  }
    @Override public String getRightTitle        () { return "OFF"; }


    private final Identifier bgSpriteId;
    private final Consumer<UiButton> afterPressCallback;




    public UiToggleFeatureButton(final Screen screen, final ClientFeature<?> feature) {
        this(screen, feature, null);
    }
    public UiToggleFeatureButton(final Screen screen, final ClientFeature<?> feature, final @Nullable Consumer<UiButton> afterPressCallback) {
        this(screen, 50, 50, 50, 50, feature, afterPressCallback);
    }
    public UiToggleFeatureButton(final Screen screen, final int x, final int y, final int w, final int h, final ClientFeature<?> feature) {
        this(screen, x, y, w, h, feature, null);
    }


    public UiToggleFeatureButton(final Screen screen, final int x, final int y, final int w, final int h, final ClientFeature<?> feature, final @Nullable Consumer<UiButton> afterPressCallback) {

        // Throw exception if not a ServerToggleFeature
        final @NotNull __base_ServerFeature<?> genericServerFeature = feature.getServerFeature();
        if(!(genericServerFeature instanceof ServerToggleFeature)) {
            throw new IllegalArgumentException(
                "UiToggleFeatureButton created with a server feature of incompatible type: " +
                genericServerFeature.getClass().getName()
            );
        }

        // Proceed with normal initialization
        final ServerToggleFeature _serverFeature = (ServerToggleFeature)genericServerFeature;
        final boolean initialValue = ClientFeatureSync.getFeatureB(_serverFeature);
        super(screen, initialValue, x, y, w, h, getToggleText(feature, initialValue), null, '\0', TextAlignment.LEFT);
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;
        this.afterPressCallback = afterPressCallback;

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




    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        setLabel(getToggleText(getClientFeature()));
        ClientFeatureSync.setFeature(getServerFeature(), value);
        setFocused(false);
        if(afterPressCallback != null) afterPressCallback.accept(this);
    }




    public static UiTxt getToggleText(final ClientFeature<?> feature, final boolean value) {
        return (UiTxt)feature.calcName().cat(": " + (value ? "ON" : "OFF"));
    }

    public UiTxt getToggleText(final ClientFeature<?> feature) {
        return getToggleText(feature, value);
    }
}
