package com.snek.engineersbliss.mixin.overlay_data;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.mixin.accessors.ComparatorBlockAccessor;
import com.snek.engineersbliss.mixin.accessors.DiodeBlockAccessor;
import com.snek.engineersbliss.mixin.accessors.PoweredRailBlockAccessor;
import com.snek.engineersbliss.network.overlay_data.handlers.RailInputDataHandler;
import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;
import com.snek.engineersbliss.network.overlay_data.payloads.RailUpdatePayload;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;




/**
 * A mixin that tracks the input signals of rail blocks
 */
@Mixin(PoweredRailBlock.class)
public class RailInputChangeTrackerMixin {
    private static final Map<BlockPos, Integer> lastSignals = new HashMap<>();
    private static final int NO_SOURCE_DISTANCE = 1024;




    @Unique
    private static int calcBlockRailDistance(final BlockPos src, final BlockPos dest) {
        int dx = Math.abs(src.getX() - dest.getX());
        int dz = Math.abs(src.getZ() - dest.getZ());
        return dx + dz;
        //! Path only ever goes in one cardinal direction, so one of the horizontal axes will always be 0 distance and the other 100% of it.
        //! Vertical distance is absorbed by slopes at no cost, as src and dst are always guaranteed to be connectible by straight rails.
    }





    @Inject(method = "updateState", at = @At("HEAD"))
	private void updateState(final BlockState state, final Level level, final BlockPos pos, final Block block, final CallbackInfo ci) {
        if(level == null || level.isClientSide()) return;
        int newSignal = 0;
        // final int back = ((ComparatorBlockAccessor)       Blocks.COMPARATOR).invokeGetInputSignal       (level, pos, state);
        // final int side = ((DiodeBlockAccessor)(DiodeBlock)Blocks.COMPARATOR).invokeGetAlternateSignal   (level, pos, state);


        // Calculate new data
        if(level.hasNeighborSignal(pos)) {
            newSignal = 9;
        }
        else {
            RailInputDataHandler.startRecording();
            ((PoweredRailBlockAccessor)state.getBlock()).invokeFindPoweredRailSignal(level, pos, state, true,  0);
            final @Nullable BlockPos sourceA = RailInputDataHandler.getRecordedSignalSourcePos();

            RailInputDataHandler.startRecording(); //! Required to set the source back to null. Not finding a source doesn't do that automatically.
            ((PoweredRailBlockAccessor)state.getBlock()).invokeFindPoweredRailSignal(level, pos, state, false, 0);
            final @Nullable BlockPos sourceB = RailInputDataHandler.getRecordedSignalSourcePos();
            RailInputDataHandler.stopRecording();

            final int distanceA = sourceA == null ? NO_SOURCE_DISTANCE : calcBlockRailDistance(pos, sourceA);
            final int distanceB = sourceB == null ? NO_SOURCE_DISTANCE : calcBlockRailDistance(pos, sourceB);
            final int distance = Math.min(distanceA, distanceB);
            newSignal = distance == NO_SOURCE_DISTANCE ? 0 : 8 - distance;
        }


        // Calculate new signal, return if input is identical to the last one
        final Integer lastSignal = lastSignals.get(pos);
        if(lastSignal != null && lastSignal == newSignal) return;
        // final int out  = ((ComparatorBlockAccessor)       Blocks.COMPARATOR).invokeCalculateOutputSignal(level, pos, state);
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