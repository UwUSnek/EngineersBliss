package com.snek.engineersbliss.feature_handlers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.feature_handlers.base.__base_BlockFeatureInterface;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;
import com.snek.engineersbliss.network.features.payloads.BoolFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.FloatFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.IntFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.DoubleFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.LongFeatureUpdateRequestPayload;
import com.snek.engineersbliss.utils.scheduler.ServerScheduler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;








/**
 * This class accepts feature update packets from clients.
 * It also keeps track of the current state of all features of any connected player.
 */
public class ServerFeatureSync {
    private ServerFeatureSync() {}
    private static final Map<UUID,       PlayerFeatureData> playerData = new ConcurrentHashMap<>();
    private static final Map<BlockState,  StateFeatureData>  stateData = new ConcurrentHashMap<>();




    private static StateFeatureData getStateData(final @NotNull BlockState state) {
        return stateData.computeIfAbsent(state, k -> {
            final StateFeatureData data = new StateFeatureData();
            for(final __base_ServerFeature<?> feature : __base_ServerFeature.getAllFeatures().values()) {
                if(feature instanceof final @NotNull __base_BlockFeatureInterface blockFeature) {
                    if(blockFeature.affects(k.getBlock())) {
                        data.addFeature(feature);
                    }
                }
            }
            return data;
        });
    }
    public static boolean stateHasFeaturesFromSet(final @NotNull BlockState state, __base_ServerFeatureSet featureSet) {
        return getStateData(state).hasFeaturesFromSet(featureSet);
    }
    public static boolean stateHasFeatures(final @NotNull BlockState state) {
        return getStateData(state).hasFeatures();
    }
    public static <T> boolean stateHasFeature(final @NotNull BlockState state, final @NotNull __base_ServerFeature<T> feature) {
        return getStateData(state).hasFeature(feature);
    }
    //! No setter for these since they are all initialized when the server starts




    private static PlayerFeatureData getPlayerData(final Player player) {
        return playerData.compute(player.getUUID(), (k, v) -> v == null ? new PlayerFeatureData() : v);
    }
    public static <T> T getFeature(final @NotNull Player player, final @NotNull __base_ServerFeature<T> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static boolean getFeatureB(final @NotNull Player player, final @NotNull __base_ServerFeature<Boolean> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static int getFeatureI(final @NotNull Player player, final @NotNull __base_ServerFeature<Integer> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static long getFeatureL(final @NotNull Player player, final @NotNull __base_ServerFeature<Long> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static Float getFeatureF(final @NotNull Player player, final @NotNull __base_ServerFeature<Float> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static double getFeatureD(final @NotNull Player player, final @NotNull __base_ServerFeature<Double> feature) {
        return getPlayerData(player).getValue(feature);
    }


    //! Private because other stuff shouldn't change the server's feature states. Only the packet receiver can (this class).
    private static <T> void setFeature(final @NotNull Player player, final @NotNull __base_ServerFeature<T> feature, final T value) {
        getPlayerData(player).setValue(feature, value);
    }







    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(  BoolFeatureUpdateRequestPayload.TYPE, (payload, context) -> handleFeatureUpdateRequest(payload.id(), payload.value(), context));
        ServerPlayNetworking.registerGlobalReceiver(   IntFeatureUpdateRequestPayload.TYPE, (payload, context) -> handleFeatureUpdateRequest(payload.id(), payload.value(), context));
        ServerPlayNetworking.registerGlobalReceiver(  LongFeatureUpdateRequestPayload.TYPE, (payload, context) -> handleFeatureUpdateRequest(payload.id(), payload.value(), context));
        ServerPlayNetworking.registerGlobalReceiver( FloatFeatureUpdateRequestPayload.TYPE, (payload, context) -> handleFeatureUpdateRequest(payload.id(), payload.value(), context));
        ServerPlayNetworking.registerGlobalReceiver(DoubleFeatureUpdateRequestPayload.TYPE, (payload, context) -> handleFeatureUpdateRequest(payload.id(), payload.value(), context));
    }


    //! Packet receivers need extra checks to ensure the server doesn't crash for modified packets or a version mismatch
    @SuppressWarnings("unchecked")
    private static <T> void handleFeatureUpdateRequest(int id, T newValue, ServerPlayNetworking.Context context) {
        ServerScheduler.run(() -> {
            final Player player = context.player();
            try {
                final __base_ServerFeature<T> feature = (__base_ServerFeature<T>)__base_ServerFeature.getAllFeatures().get(id);
                setFeature(player, feature, newValue);
                final var callback = feature.getAfterChangeCallback();
                if(callback != null) callback.accept(player, newValue);
                EngineerSBliss.LOGGER.info(
                    "Player {} changed their {} feature to {}.",
                    player.getName().getString(), feature.getId(), getFeature(player, feature)
                );
            }
            catch(ClassCastException e) {
                EngineerSBliss.LOGGER.error(
                    "Received feature update packet from {} with bad type. Feature ID: {}. {}",
                    player.getName().getString(), id, e.getMessage(), e
                );
            }
            catch(IndexOutOfBoundsException e) {
                EngineerSBliss.LOGGER.error(
                    "Received feature update packet from {} for unknown ID #{}. {}",
                    player.getName().getString(), id, e.getMessage(), e
                );
            }
        });
    }
}



