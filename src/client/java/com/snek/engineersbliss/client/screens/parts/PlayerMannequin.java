package com.snek.engineersbliss.client.screens.parts;

import com.mojang.authlib.GameProfile;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
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




    public static PlayerMannequin mannequin;
    public static PlayerMannequin getMannequin() {
        if(mannequin == null) {
            mannequin = new PlayerMannequin(Minecraft.getInstance().level, Minecraft.getInstance().player.getGameProfile());
        }
        return mannequin;
    }
}