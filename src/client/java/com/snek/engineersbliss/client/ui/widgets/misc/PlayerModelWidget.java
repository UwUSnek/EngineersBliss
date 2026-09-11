package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;








public class PlayerModelWidget extends __base_UiContainer {
    public static final float DEFAULT_MODEL_SCALE = 128f;

    private final UiTextWidget nameWidget;
    private final UiTextWidget playTimeWidget;
    private float modelScale;
    public void setModelScale(final float scale) { modelScale = scale; }


    public PlayerModelWidget(final UiScreen screen) {
        super(screen);
        this.modelScale = DEFAULT_MODEL_SCALE;
        final LocalPlayer player = Minecraft.getInstance().player;
        final UiTxt nameText = new UiTxt(player == null ? "" : player.getGameProfile().name(), Fonts.ui.regular, 2f);
        addChild(    nameWidget = new UiTextWidget(screen, nameText,    TextAlignment.CENTER, 0xFFFFC200));
        addChild(playTimeWidget = new UiTextWidget(screen, new UiTxt(), TextAlignment.CENTER, 0xFFDDDDDD));
    }


    @Override
    public void relayoutSelf() {
        final LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        // Position labels
        final int nameLineHeight =     nameWidget.getLabel().getScaledFont().getLineHeight();
        final int timeLineHeight = playTimeWidget.getLabel().getScaledFont().getLineHeight();
        final float nameY  = getYF();
        final float titleY = nameY + nameLineHeight;
        nameWidget    .setPos(getXF(), nameY);
        playTimeWidget.setPos(getXF(), titleY);
        nameWidget    .setSize(getWidthF(), nameLineHeight);
        playTimeWidget.setSize(getWidthF(), timeLineHeight);
    }




    @Override
    public void extractSelf(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractSelf(graphics, mouseX, mouseY, a);
        if(!ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.PLAYER_MODEL_IN_PAUSE_SCREEN)) return;


        // Refresh playtime label
        final long ms = MinecraftUtils.getPlaytimeMs();
        final long hours   = TimeUnit.MILLISECONDS.toHours(ms);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;
        playTimeWidget.setLabel(new UiTxt(String.format("Playtime: %dh %dm %ds", hours, minutes, seconds), Fonts.ui.light));


        // Draw player model
        final @Nullable PlayerMannequin model = PlayerMannequin.getMannequin();
        if(model == null) return;
        graphics.entity(getXF(), getYF(), getRight(), getBottom(), modelScale, 0f, mouseX, mouseY, model);
    }


    @Override
    public void extractBackground(UiGraphics graphics, float mouseX, float mouseY, float a) {
        // Empty. No background.
    }
}
