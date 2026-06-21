package com.snek.engineersbliss.mixin.overlay_data;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.mixin.accessors.ComparatorBlockAccessor;
import com.snek.engineersbliss.mixin.accessors.DiodeBlockAccessor;
import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
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

    @Unique
    private static final Map<BlockPos, Integer> lastSignals = new HashMap<>();
    @Unique
    private static boolean recording = false;
    private static BlockPos signalSourcePos = null;



    @Inject(method = "updateState", at = @At("HEAD"))
	private void updateState(final BlockState state, final Level level, final BlockPos pos, final Block block, final CallbackInfo ci) {
        if(level == null || level.isClientSide()) return;

        // Calculate new signals, return if input is identical to the last one
        final Integer last = lastSignals.get(pos);
        // final int back = ((ComparatorBlockAccessor)       Blocks.COMPARATOR).invokeGetInputSignal       (level, pos, state);
        // final int side = ((DiodeBlockAccessor)(DiodeBlock)Blocks.COMPARATOR).invokeGetAlternateSignal   (level, pos, state);
        if(last != null && last == ) return;
        final int out  = ((ComparatorBlockAccessor)       Blocks.COMPARATOR).invokeCalculateOutputSignal(level, pos, state);
        lastSignals.put(pos, new int[]{ back, side, out });


        // // Send update packet to all players that can see the block
        // for(final ServerPlayer player : PlayerLookup.tracking((ServerLevel)level, pos)) {

        //     //! Only send packet to players with this mod installed
        //     if(ServerPlayNetworking.canSend(player, ComparatorUpdatePayload.TYPE)) {
        //         ServerPlayNetworking.send(player, new ComparatorUpdatePayload(pos, back, side, out));
        //     }
        // }
    }
}


//TODO updates might need batching. this needs to be implemented manually
//TODO as of right now, thousands of block updates of interest send thousands of packets to each player