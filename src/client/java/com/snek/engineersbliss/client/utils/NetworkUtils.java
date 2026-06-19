package com.snek.engineersbliss.client.utils;

import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;








public class NetworkUtils {
    private NetworkUtils() {}
    private static boolean _serverHasMod = false;



    /**
     * Initializes this utility class and registers relevant listeners.
     * This must be called in the mod's client init function.
     */
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            _serverHasMod = ClientPlayNetworking.getReceived().contains(ComparatorUpdatePayload.TYPE.id());
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            _serverHasMod = false;
        });
    }


    /**
     * Checks if the server the client is currently connected to has this mod installed.
     * @return True if the server has the mod installed, false if it doesn't or the client isn't connected to any server.
     */
    public static boolean serverHasMod() {
        return _serverHasMod;
    }
}
