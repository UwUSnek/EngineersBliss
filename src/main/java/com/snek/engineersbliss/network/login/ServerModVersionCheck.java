package com.snek.engineersbliss.network.login;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.utils.Txt;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;




public class ServerModVersionCheck {
    public static final Identifier CHANNEL = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "version_handshake");
    private ServerModVersionCheck() {}


    public static void register() {
        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf(getVersion());
            sender.sendPacket(CHANNEL, buf);
        });

        ServerLoginNetworking.registerGlobalReceiver(CHANNEL, (server, handler, understood, buf, synchronizer, responseSender) -> {
            if(understood) {
                String clientVersion = buf.readUtf();
                if(!compatible(clientVersion, getVersion())) {
                    handler.disconnect(new Txt(EngineerSBliss.MOD_NAME + " version mismatch. Server: " + getVersion() + ", Client: " + clientVersion).get());
                }
            }
        });
    }


    private static boolean compatible(String a, String b) {
        String[] pa = a.split("\\.", 3);
        String[] pb = b.split("\\.", 3);
        return
            pa.length >= 2 &&
            pb.length >= 2 &&
            pa[0].equals(pb[0]) &&
            pa[1].equals(pb[1])
        ;
    }


    private static String getVersion() {
        return FabricLoader.getInstance().getModContainer(EngineerSBliss.MOD_ID)
            .map(c -> c.getMetadata().getVersion().getFriendlyString())
            .orElse("0.0.0")
        ;
    }
}