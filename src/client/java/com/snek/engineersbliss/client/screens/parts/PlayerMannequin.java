package com.snek.engineersbliss.client.screens.parts;

import org.jetbrains.annotations.Nullable;

import com.mojang.authlib.GameProfile;
import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;




public class PlayerMannequin extends AbstractClientPlayer {
    public PlayerMannequin(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Override
    public boolean isSpectator() { return false; }

    @Override
    public boolean isCreative() { return false; }

    @Override
    public boolean isModelPartShown(PlayerModelPart part) { return true; }

    @Override
    public boolean shouldShowName() { return false; }




    public static PlayerMannequin mannequin;
    public static @Nullable PlayerMannequin getMannequin() {
        if(mannequin == null) {
            final Player player = Minecraft.getInstance().player;
            if(player != null) {
                mannequin = new PlayerMannequin(Minecraft.getInstance().level, player.getGameProfile());
            }
            else {
                EngineerSBliss.LOGGER.error("Player instance not found", new Exception(""));
            }
        }
        return mannequin;
    }
}