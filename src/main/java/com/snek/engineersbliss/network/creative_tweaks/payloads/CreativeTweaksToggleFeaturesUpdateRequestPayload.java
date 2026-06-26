package com.snek.engineersbliss.network.creative_tweaks.payloads;

import com.snek.engineersbliss.EngineerSBliss;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;




public record CreativeTweaksToggleFeaturesUpdateRequestPayload(long mask) implements CustomPacketPayload {
    public static final Type<CreativeTweaksToggleFeaturesUpdateRequestPayload> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "creative_tweaks_toggle_features_change_request"))
    ;

    public static final StreamCodec<ByteBuf, CreativeTweaksToggleFeaturesUpdateRequestPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.LONG,
            CreativeTweaksToggleFeaturesUpdateRequestPayload::mask,
            CreativeTweaksToggleFeaturesUpdateRequestPayload::new
        )
    ;

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}