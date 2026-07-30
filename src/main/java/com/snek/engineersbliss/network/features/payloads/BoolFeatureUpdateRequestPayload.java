package com.snek.engineersbliss.network.features.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record BoolFeatureUpdateRequestPayload(int id, boolean value) implements CustomPacketPayload {
    public static final Type<BoolFeatureUpdateRequestPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "bool_feature_change_request"))
    ;

    public static final StreamCodec<ByteBuf, BoolFeatureUpdateRequestPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT, BoolFeatureUpdateRequestPayload::id,
            ByteBufCodecs.BOOL,    BoolFeatureUpdateRequestPayload::value,
            BoolFeatureUpdateRequestPayload::new
        )
    ;

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}