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
import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.redstone.Orientation;




/**
 * A mixin that tracks the inputs of comparators
 */
// @Mixin(ComparatorBlock.class)
@Mixin(DiodeBlock.class)
public class ComparatorInputChangeTrackerMixin {


    //FIXME filter per dimension too
    // Stores the last fetched input signals for each tracked comparator block
    // ! Output is recalculated every time so there is no need to store it in the map.
    @Unique private static final Map<BlockPos, int[]> lastSignals = new HashMap<>();




    @Inject(method = "neighborChanged", at = @At("HEAD"), cancellable = false)
    private void neighborChanged(final BlockState state, final Level level, final BlockPos pos, final Block block, @Nullable final Orientation orientation, final boolean movedByPiston, final CallbackInfo ci) {

        // Return if level isnt valid, this is firing on the client, or the block is not a Comparator
        if(level == null || level.isClientSide()) return;
        if(!state.is(Blocks.COMPARATOR)) return;


        // Calculate new signals
        final int[] last = lastSignals.get(pos);
        final int back = ((ComparatorBlockAccessor)Blocks.COMPARATOR).invokeGetInputSignal    (level, pos, state);
        final int side = ((     DiodeBlockAccessor)Blocks.COMPARATOR).invokeGetAlternateSignal(level, pos, state);


        // Return if inputs are identical to the last ones, update map otherwise
        if(last != null && last[0] == back && last[1] == side) return;
        final int out = ((ComparatorBlockAccessor)Blocks.COMPARATOR).invokeCalculateOutputSignal(level, pos, state);
        lastSignals.put(pos, new int[]{ back, side });


        // Fetch comparator mode and send update packet to all players that can see the block
        final boolean mode = state.getValue(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT;
        for(final ServerPlayer player : PlayerLookup.tracking((ServerLevel)level, pos)) {

            //! Only send packet to players with this mod installed
            if(ServerPlayNetworking.canSend(player, ComparatorUpdatePayload.TYPE)) {
                System.out.println("SENDING SIDE " + side + " from server");
                //BUG this goes down to 0 in steps of 2 power??? what the actual f
                //BUG send timestamp with the packet to ensure outdated ones are discarded,
                //BUG make this a base packet class or something idk

                //BUG though that wouldn't make sense cause minecraft already uses TCP?? which is guaranteed to receive in the same order??
                ServerPlayNetworking.send(player, new ComparatorUpdatePayload(pos, back, side, out, mode));
            }
        }
    }
}


//TODO updates might need batching. this needs to be implemented manually
//TODO as of right now, thousands of block updates of interest send thousands of packets to each player