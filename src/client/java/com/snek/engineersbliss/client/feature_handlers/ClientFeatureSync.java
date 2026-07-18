package com.snek.engineersbliss.client.feature_handlers;

import com.snek.engineersbliss.feature_handlers.FeaturePlayerData;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;

import net.minecraft.world.entity.player.Player;








/**
 * This class sends feature update packets to the server.
 * It also keeps track of the current state of the local player.
 */
public class ClientFeatureSync {
    private ClientFeatureSync() {}
    private static final FeaturePlayerData playerData = new FeaturePlayerData();




    public static <T> T getFeature(final __base_ServerFeature<T> feature) {
        return playerData.getValue(feature);
    }

    /**
     * Sets a feature to the specified value.
     * This also updates the local feature data cache and sends a feature update packet to the server.
     */
    public static <T> void setFeature(final __base_ServerFeature<T> feature, final T value) {
        playerData.setValue(feature, value);
    }




    /**
     * Checks if a creative mode player has the specified feature set to the specified value.
     * Returns false if the entity is not a Player or is not in Creative Mode.
     * ! This doesn't work when called from the dedicated server. Use ServerFeatureSync.creativePlayerHasFeature(Player, __base_ServerFeature) instead.
     */
    public static <T> boolean creativePlayerHasFeature(final Object entity, final __base_ServerFeature<T> feature, final T value) {
        if(entity instanceof final Player player) {
            if(player.isCreative()) {
                return getFeature(feature) == value;
            }
        }
        return false;
    }
    /**
     * Checks if a creative mode player has the specified toggle feature set to TRUE.
     * Returns false if the entity is not a Player or is not in Creative Mode or the feature is not a toggle feature.
     * ! This doesn't work when called from the dedicated server. Use ServerFeatureSync.creativePlayerHasFeature(Player, __base_ServerFeature) instead.
     */
    public static <T> boolean creativePlayerHasFeature(final Object entity, final __base_ServerFeature<T> feature) {
        if(feature instanceof ServerToggleFeature) {
            return creativePlayerHasFeature(entity, (__base_ServerFeature<Boolean>)feature, true);
        }
        return false;
    }
    public static boolean shouldPlayerPhaseThroughBlocks(final Object entity) {
        return creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.PHASE_THROUGH_BLOCKS_FLY) && ((Player)entity).getAbilities().flying;
    }
}