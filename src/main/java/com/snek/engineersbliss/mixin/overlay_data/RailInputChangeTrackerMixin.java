package com.snek.engineersbliss.mixin.overlay_data;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.network.overlay_data.payloads.RailUpdatePayload;
import com.snek.engineersbliss.network.overlay_data.resolvers.RailInputDataResolver;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;




/**
 * A mixin that tracks the input signals of rail blocks
 */
@Mixin(PoweredRailBlock.class)
public class RailInputChangeTrackerMixin {
    private static final Map<BlockPos, Integer> lastSignals = new HashMap<>();




    @Inject(method = "updateState", at = @At("HEAD"))
	private void updateState(final BlockState state, final Level level, final BlockPos pos, final Block block, final CallbackInfo ci) {
        if(level == null || level.isClientSide()) return;

        // Calculate new data
        int newSignal = RailInputDataResolver.calcPowerLevel(level, pos);


        // Return if input is identical to the last one. Update map otherwise
        final Integer lastSignal = lastSignals.get(pos);
        if(lastSignal != null && lastSignal == newSignal) return;
        lastSignals.put(pos, newSignal);


        // If signal is different, send update packets to all players that can see the block
        for(final ServerPlayer player : PlayerLookup.tracking((ServerLevel)level, pos)) {

            //! Only send packet to players with this mod installed
            if(ServerPlayNetworking.canSend(player, RailUpdatePayload.TYPE)) {
                ServerPlayNetworking.send(player, new RailUpdatePayload(pos, newSignal));
            }
        }
    }
}


//TODO updates might need batching. this needs to be implemented manually
//TODO as of right now, thousands of block updates of interest send thousands of packets to each player