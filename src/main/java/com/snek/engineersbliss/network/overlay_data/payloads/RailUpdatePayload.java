package com.snek.engineersbliss.network.overlay_data.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record RailUpdatePayload(BlockPos pos, int input) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RailUpdatePayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "rail_update"))
    ;

    public static final StreamCodec<RegistryFriendlyByteBuf, RailUpdatePayload> CODEC =
        StreamCodec.composite(
            BlockPos.STREAM_CODEC, RailUpdatePayload::pos,
            ByteBufCodecs.VAR_INT, RailUpdatePayload::input,
            RailUpdatePayload::new
        );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
}