package com.snek.engineersbliss.feature_handlers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
import com.snek.engineersbliss.network.features.payloads.BoolFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.FloatFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.IntFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.DoubleFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.LongFeatureUpdateRequestPayload;
import com.snek.engineersbliss.utils.scheduler.ServerScheduler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.player.Player;








/**
 * This class accepts feature update packets from clients.
 * It also keeps track of the current state of all features of any connected player.
 */
public class ServerFeatureSync {
    private ServerFeatureSync() {}
    private static final Map<UUID, FeaturePlayerData> playerData = new ConcurrentHashMap<>();




    private static FeaturePlayerData getPlayerData(final Player player) {
        return playerData.compute(player.getUUID(), (k, v) -> v == null ? new FeaturePlayerData() : v);
    }
    public static <T> T getFeature(final Player player, final __base_ServerFeature<T> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static boolean getFeatureB(final Player player, final __base_ServerFeature<Boolean> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static int getFeatureI(final Player player, final __base_ServerFeature<Integer> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static long getFeatureL(final Player player, final __base_ServerFeature<Long> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static Float getFeatureF(final Player player, final __base_ServerFeature<Float> feature) {
        return getPlayerData(player).getValue(feature);
    }
    public static double getFeatureD(final Player player, final __base_ServerFeature<Double> feature) {
        return getPlayerData(player).getValue(feature);
    }


    //! Private because other stuff shouldn't change the server's feature states. Only the packet receiver can (this class).
    private static <T> void setFeature(final Player player, final __base_ServerFeature<T> feature, final T value) {
        getPlayerData(player).setValue(feature, value);
    }




    /**
     * Checks if a creative mode player has the specified feature set to the specified value.
     * Returns false if the entity is not a Player or is not in Creative Mode.
     * ! This doesn't work when called by the client on a dedicated server. Use ClientFeatureSync.creativePlayerHasFeature(__base_ServerFeature) instead.
     */
    public static <T> boolean creativePlayerHasFeature(final Object entity, final __base_ServerFeature<T> feature, final T value) {
        if(entity instanceof final Player player) {
            if(player.isCreative()) {
                return getFeature(player, feature) == value;
            }
        }
        return false;
    }


    /**
     * Checks if a creative mode player has the specified toggle feature set to TRUE.
     * Returns false if the entity is not a Player or is not in Creative Mode or the feature is not a toggle feature.
     * ! This doesn't work when called by the client on a dedicated server. Use ClientFeatureSync.creativePlayerHasFeature(__base_ServerFeature) instead.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean creativePlayerHasFeature(final Object entity, final __base_ServerFeature<T> feature) {
        if(feature instanceof ServerToggleFeature) {
            return creativePlayerHasFeature(entity, (__base_ServerFeature<Boolean>)feature, true);
        }
        return false;
    }


    public static boolean shouldPlayerPhaseThroughBlocks(final Object entity) {
        return creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.PHASE_THROUGH_BLOCKS_FLY) && ((Player)entity).getAbilities().flying;
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
