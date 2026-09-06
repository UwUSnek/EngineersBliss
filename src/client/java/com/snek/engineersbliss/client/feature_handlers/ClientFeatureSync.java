package com.snek.engineersbliss.client.feature_handlers;

import com.snek.engineersbliss.feature_handlers.PlayerFeatureData;
import com.snek.engineersbliss.feature_handlers.base.__base_BlockFeatureInterface;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.network.features.payloads.BoolFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.DoubleFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.FloatFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.IntFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.LongFeatureUpdateRequestPayload;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.NetworkUtils;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;








/**
 * This class sends feature update packets to the server.
 * It also keeps track of the current state of the local player.
 */
public class ClientFeatureSync {
    private ClientFeatureSync() {}
    private static final PlayerFeatureData playerData = new PlayerFeatureData();




    public static <T> T getFeature(final __base_ServerFeature<T> feature) {
        feature.getFeatureSet().initializedOrThrow();
        return playerData.getValue(feature);
    }
    public static boolean getFeatureB(final __base_ServerFeature<Boolean> feature) {
        return getFeature(feature);
    }
    public static int getFeatureI(final __base_ServerFeature<Integer> feature) {
        return getFeature(feature);
    }
    public static long getFeatureL(final __base_ServerFeature<Long> feature) {
        return getFeature(feature);
    }
    public static Float getFeatureF(final __base_ServerFeature<Float> feature) {
        return getFeature(feature);
    }
    public static double getFeatureD(final __base_ServerFeature<Double> feature) {
        return getFeature(feature);
    }




    /**
     * Sets a feature to the specified value.
     * This also updates the local feature data cache and sends a feature update packet to the server.
     */
    public static <T> void setFeature(final __base_ServerFeature<T> feature, final T value) {
        feature.getFeatureSet().initializedOrThrow();
        playerData.setValue(feature, value);


        // Refresh chunks if needed
        if(feature instanceof final @NotNull __base_BlockFeatureInterface blockFeature) {
            MinecraftUtils.refreshSectionsContaining(blockFeature.getAffectedBlocks());
        }


        // Send an update packet to the server
        // ! Checking for changes here is pointless as any setFeature call is triggered by a feature change.
        //! (Buttons and sliders can only change to a value they aren't already holding).
        if(NetworkUtils.serverHasMod()) {
            sendFeatureUpdatePacket(feature, value);
        }
    }

    private static <T> void sendFeatureUpdatePacket(final __base_ServerFeature<T> feature, final T value) {
        switch(value) {
            case Boolean n -> ClientPlayNetworking.send(new BoolFeatureUpdateRequestPayload  (feature.getHash(), n));
            case Integer n -> ClientPlayNetworking.send(new IntFeatureUpdateRequestPayload   (feature.getHash(), n));
            case Long    n -> ClientPlayNetworking.send(new LongFeatureUpdateRequestPayload  (feature.getHash(), n));
            case Float   n -> ClientPlayNetworking.send(new FloatFeatureUpdateRequestPayload (feature.getHash(), n));
            case Double  n -> ClientPlayNetworking.send(new DoubleFeatureUpdateRequestPayload(feature.getHash(), n));
            default -> EngineerSBliss.LOGGER.error("Invalid feature type {}", value.getClass().getName(), new Throwable());
        }
    }
}