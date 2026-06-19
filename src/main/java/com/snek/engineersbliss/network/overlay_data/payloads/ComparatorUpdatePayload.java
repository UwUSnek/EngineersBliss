package com.snek.engineersbliss.network.overlay_data.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record ComparatorUpdatePayload(BlockPos pos, int back, int side, int out) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ComparatorUpdatePayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "comparator_update"))
    ;

    public static final StreamCodec<RegistryFriendlyByteBuf, ComparatorUpdatePayload> CODEC =
        StreamCodec.composite(
            BlockPos.STREAM_CODEC, ComparatorUpdatePayload::pos,
            ByteBufCodecs.VAR_INT, ComparatorUpdatePayload::back,
            ByteBufCodecs.VAR_INT, ComparatorUpdatePayload::side,
            ByteBufCodecs.VAR_INT, ComparatorUpdatePayload::out,
            ComparatorUpdatePayload::new
        );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
}