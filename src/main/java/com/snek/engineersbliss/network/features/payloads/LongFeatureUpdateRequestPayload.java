package com.snek.engineersbliss.network.features.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record LongFeatureUpdateRequestPayload(int id, long value) implements CustomPacketPayload {
    public static final Type<LongFeatureUpdateRequestPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "long_feature_change_request"))
    ;

    public static final StreamCodec<ByteBuf, LongFeatureUpdateRequestPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT,  LongFeatureUpdateRequestPayload::id,
            ByteBufCodecs.VAR_LONG, LongFeatureUpdateRequestPayload::value,
            LongFeatureUpdateRequestPayload::new
        )
    ;

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}