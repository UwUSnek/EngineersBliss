package com.snek.engineersbliss.network.creative_tweaks.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record InteractionRadiusChangeRequestPayload(int radius) implements CustomPacketPayload {
    public static final Type<InteractionRadiusChangeRequestPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "interaction_radius_change_request"))
    ;

    public static final StreamCodec<ByteBuf, InteractionRadiusChangeRequestPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.INT,
            InteractionRadiusChangeRequestPayload::radius,
            InteractionRadiusChangeRequestPayload::new
        )
    ;

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}