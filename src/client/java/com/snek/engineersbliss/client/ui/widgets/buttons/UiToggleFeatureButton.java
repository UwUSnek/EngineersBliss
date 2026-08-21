package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.client.ui.widgets.base.DualPreviewFeatureInputWidget;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.resources.Identifier;








public class UiToggleFeatureButton extends UiToggleButton implements DualPreviewFeatureInputWidget {
    private final              ClientFeature<?> clientFeature;
    private final @Nullable ServerToggleFeature serverFeature;


    @Override public              ClientFeature<?> getClientFeature() { return clientFeature; }
    @Override public @Nullable ServerToggleFeature getServerFeature() { return serverFeature; }
    @Override public String getLeftPreviewSuffix () { return "off";  }
    @Override public String getRightPreviewSuffix() { return "on";  }
    @Override public String getLeftTitle         () { return formatValue(false, true); }
    @Override public String getRightTitle        () { return formatValue(true,  true); }


    private final Consumer<UiButton> afterPressCallback;




    public UiToggleFeatureButton(final Screen screen, final ClientFeature<?> feature, final @Nullable ValueFormatter<Boolean> valueFormatter) {
        this(screen, feature, null, valueFormatter);
    }
    public UiToggleFeatureButton(final Screen screen, final ClientFeature<?> feature, final @Nullable Consumer<UiButton> afterPressCallback, final @Nullable ValueFormatter<Boolean> valueFormatter) {

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
        final ValueFormatter<Boolean> nonNullValueFormatter = valueFormatter != null ? valueFormatter : (n, u) -> n.booleanValue() ? "ON" : "OFF";
        super(screen, initialValue, getToggleText(feature, initialValue, nonNullValueFormatter), null, nonNullValueFormatter, '\0', TextAlignment.LEFT);
        this.clientFeature = feature;
        this.serverFeature = _serverFeature;
        this.afterPressCallback = afterPressCallback;

        // Calculate sprite id
        final String bgSpritePath = String.format("%s/%s", serverFeature.getFeatureSet().getId(), serverFeature.getId());
        this.withSpriteBg(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, bgSpritePath), 1f, 1f);
    }




    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        setLabel(getToggleText(getClientFeature()));
        ClientFeatureSync.setFeature(getServerFeature(), value);
        setFocused(false);
        if(afterPressCallback != null) afterPressCallback.accept(this);
    }




    public static UiTxt getToggleText(final ClientFeature<?> feature, final boolean value, final ValueFormatter<Boolean> valueFormatter) {
        return (UiTxt)feature.calcName().cat(": " + valueFormatter.format(value, false));
    }

    public UiTxt getToggleText(final ClientFeature<?> feature) {
        return getToggleText(feature, value, getValueFormatter());
    }
}
