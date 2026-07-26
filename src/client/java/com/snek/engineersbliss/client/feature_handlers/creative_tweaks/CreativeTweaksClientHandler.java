package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.mixin.accessors.MinecraftAccessor;
import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;





public class CreativeTweaksClientHandler {
    private CreativeTweaksClientHandler() { }
    private static final float DEFAULT_FLYING_SPEED  = new Abilities().getFlyingSpeed();


    // Interaction count and event gate
    private static boolean isRepeating = false;
    private static int interactionCount;
    static {
        final @NotNull ServerSteppedFeature<Integer> interactionCountFeature = CreativeTweaksServerFeatureSet.INTERACTION_COUNT;
        interactionCount = interactionCountFeature.getValues().get(interactionCountFeature.getDefault());
    }
    public static int getInteractionCount() {
        return interactionCount;
    }



    public static void onFlyingSpeedChange(final Integer index, final Float value) {
        final @NotNull Player player = Minecraft.getInstance().player;
        if(player != null) {
            if(!player.isCreative()) return;
            player.getAbilities().setFlyingSpeed(value * DEFAULT_FLYING_SPEED);
        }
    }



    public static void onInteractionCountChange(final Integer index, final Integer value) {
        final @NotNull Player player = Minecraft.getInstance().player;
        if(player != null) {
            if(!player.isCreative()) return;
            interactionCount = value;
        }
    }




    public static void register() {
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> {
            repeat(player, true);
            return InteractionResult.PASS;
        });
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
            repeat(player, true);
            return InteractionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            repeat(player, false);
            return InteractionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
            repeat(player, false);
            return InteractionResult.PASS;
        });
        UseItemCallback.EVENT.register((player, level, hand) -> {
            repeat(player, false);
            return InteractionResult.PASS;
        });
    }


    private static void repeat(final Player player, final boolean isAttack) {
        if(isRepeating || !player.isCreative()) return;

        final int repeats = CreativeTweaksClientHandler.getInteractionCount();
        if(repeats <= 1) return;

        final Minecraft mc = Minecraft.getInstance();
        final MinecraftAccessor mca = (MinecraftAccessor)mc;
        isRepeating = true;
        try {
            for(int i = 1; i < repeats; i++) {
                mca.invokePick(mc.getDeltaTracker().getGameTimeDeltaPartialTick(false));
                if(isAttack) mca.invokeStartAttack();
                else mca.invokeStartUseItem();
            }
        }
        finally {
            isRepeating = false;
        }
    }
}
