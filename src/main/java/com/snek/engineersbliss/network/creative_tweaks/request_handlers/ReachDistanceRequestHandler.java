package com.snek.engineersbliss.network.creative_tweaks.request_handlers;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;




public class ReachDistanceRequestHandler {
    private ReachDistanceRequestHandler() {}
    private static final float DEFAULT_REACH = 4.5f; //FIXME get this from somewhere instead of hard coding it
    private static final Identifier REACH_MODIFIER_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "creative_tweaks.reach");


    public static void handle(ReachDistanceChangeRequestPayload packet, final ServerPlayer player) {
        var attr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
        if(attr != null) {
            attr.addOrUpdateTransientModifier(new AttributeModifier(
                REACH_MODIFIER_ID,
                packet.reach() - DEFAULT_REACH,
                AttributeModifier.Operation.ADD_VALUE
            ));
        }
    }
}
