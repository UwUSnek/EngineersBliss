package com.snek.engineersbliss.mixin.overlay_data;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;




/**
 * A mixin that tracks the computed output level of comparators
 */
@Mixin(ComparatorBlockEntity.class)
public class ComparatorInputChangeTrackerMixin {


    @Inject(method = "setOutputSignal", at = @At("RETURN"))
	private void setOutputSignal(final int value, final CallbackInfo ci) {
        final ComparatorBlockEntity be = (ComparatorBlockEntity)(Object)this;
        final Level level = be.getLevel();
        if(level == null || level.isClientSide()) return;


        // Send update packet to all players that can see the block
        for(ServerPlayer player : PlayerLookup.tracking((ServerLevel)level, be.getBlockPos())) {

            //! Only send packet to players with this mod installed
            if(ServerPlayNetworking.canSend(player, ComparatorUpdatePayload.TYPE)) {
                ServerPlayNetworking.send(player, new ComparatorUpdatePayload(be.getBlockPos(), value));
            }
        }
    }
}


//TODO updates might need batching. this needs to be implemented manually
//TODO as of right now, thousands of block updates of interest send thousands of packets to each player