package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;





public class CreativeTweaksClientHandler {
    private CreativeTweaksClientHandler() { }
    private static final float DEFAULT_FLYING_SPEED = new Abilities().getFlyingSpeed();


    public static void onFlyingSpeedChange(final Integer index, final Float value) {
        final @NotNull Player player = Minecraft.getInstance().player;
        if(player != null) {
            if(!player.isCreative()) return;
            player.getAbilities().setFlyingSpeed(value * DEFAULT_FLYING_SPEED);
        }
    }
}
