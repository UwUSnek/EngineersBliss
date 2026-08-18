package com.snek.engineersbliss.client.network.login;

import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;

import java.util.concurrent.CompletableFuture;

import com.snek.engineersbliss.network.login.ServerModVersionCheck;

import io.netty.buffer.Unpooled;




public class ClientModVersionCheck {
    private ClientModVersionCheck() {}


    public static void register() {
        ClientLoginNetworking.registerGlobalReceiver(
            ServerModVersionCheck.CHANNEL,
            (client, handler, buf, listenerAdder) -> {
                FriendlyByteBuf response = new FriendlyByteBuf(Unpooled.buffer());
                response.writeUtf(getClientVersion());
                return CompletableFuture.completedFuture(response);
            }
        );
    }


    private static String getClientVersion() {
        return FabricLoader.getInstance()
            .getModContainer(com.snek.engineersbliss.EngineerSBliss.MOD_ID)
            .map(c -> c.getMetadata().getVersion().getFriendlyString())
            .orElse("0.0.0")
        ;
    }
}