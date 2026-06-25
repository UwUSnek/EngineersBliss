package com.snek.engineersbliss.network.creative_tweaks.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record ReachDistanceChangeRequestPayload(float reach) implements CustomPacketPayload {
    public static final Type<ReachDistanceChangeRequestPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "reach_distance_change_request"))
    ;

    public static final StreamCodec<ByteBuf, ReachDistanceChangeRequestPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ReachDistanceChangeRequestPayload::reach,
            ReachDistanceChangeRequestPayload::new
        )
    ;

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}