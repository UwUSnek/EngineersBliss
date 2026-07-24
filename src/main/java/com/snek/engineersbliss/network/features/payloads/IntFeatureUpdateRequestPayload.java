package com.snek.engineersbliss.network.features.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record IntFeatureUpdateRequestPayload(int id, int value) implements CustomPacketPayload {
    public static final Type<IntFeatureUpdateRequestPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "int_feature_change_request"))
    ;

    public static final StreamCodec<ByteBuf, IntFeatureUpdateRequestPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT, IntFeatureUpdateRequestPayload::id,
            ByteBufCodecs.VAR_INT, IntFeatureUpdateRequestPayload::value,
            IntFeatureUpdateRequestPayload::new
        )
    ;

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}