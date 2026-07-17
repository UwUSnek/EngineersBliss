package com.snek.engineersbliss.network.features.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record FloatFeatureUpdateRequestPayload(int id, float value) implements CustomPacketPayload {
    public static final Type<FloatFeatureUpdateRequestPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "float_feature_change_request"))
    ;

    public static final StreamCodec<ByteBuf, FloatFeatureUpdateRequestPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT, FloatFeatureUpdateRequestPayload::id,
            ByteBufCodecs.FLOAT,   FloatFeatureUpdateRequestPayload::value,
            FloatFeatureUpdateRequestPayload::new
        )
    ;

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}