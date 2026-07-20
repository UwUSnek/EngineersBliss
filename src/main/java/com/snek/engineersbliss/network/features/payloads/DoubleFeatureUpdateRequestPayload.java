package com.snek.engineersbliss.network.features.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record DoubleFeatureUpdateRequestPayload(int id, double value) implements CustomPacketPayload {
    public static final Type<DoubleFeatureUpdateRequestPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "double_feature_change_request"))
    ;

    public static final StreamCodec<ByteBuf, DoubleFeatureUpdateRequestPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT, DoubleFeatureUpdateRequestPayload::id,
            ByteBufCodecs.DOUBLE,  DoubleFeatureUpdateRequestPayload::value,
            DoubleFeatureUpdateRequestPayload::new
        )
    ;

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}