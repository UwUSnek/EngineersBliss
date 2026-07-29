package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.mixin.accessors.MinecraftAccessor;
import com.snek.engineersbliss.feature_handlers.base.ServerSteppedFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;









public class CreativeTweaksClientHandler {
    private CreativeTweaksClientHandler() { }
    private static final float DEFAULT_FLYING_SPEED  = new Abilities().getFlyingSpeed();


    // Interaction count and event gate
    private static boolean isRepeatScheduled = false;
    private static boolean repeatLeftClick   = false;
    private static boolean repeatRightClick  = false;
    private static boolean isRepeating = false;



    public static void onFlyingSpeedChange(final Integer index, final Float value) {
        final @NotNull Player player = Minecraft.getInstance().player;
        if(player != null) {
            if(!player.isCreative()) return;
            player.getAbilities().setFlyingSpeed(value * DEFAULT_FLYING_SPEED);
        }
    }




    //! Events reschedule the actual repeat call to the end of the current tick.
    //! This is done to avoid breaking the initial even by firing a new event from within it. Minecraft doesn't like that.
    public static void register() {
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> {
            if(shouldScheduleRepeats(level, hand, true)) {
                repeatLeftClick = true;
                if(!isRepeatScheduled) {
                    isRepeatScheduled = true;
                    ClientScheduler.run(() -> maybeRepeat());
                }
            }
            return InteractionResult.PASS;
        });
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
            if(shouldScheduleRepeats(level, hand, true)) {
                repeatLeftClick = true;
                if(!isRepeatScheduled) {
                    isRepeatScheduled = true;
                    ClientScheduler.run(() -> maybeRepeat());
                }
            }
            return InteractionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            if(shouldScheduleRepeats(level, hand, false)) {
                repeatRightClick = true;
                if(!isRepeatScheduled) {
                    isRepeatScheduled = true;
                    ClientScheduler.run(() -> maybeRepeat());
                }
            }
            return InteractionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
            if(shouldScheduleRepeats(level, hand, false)) {
                repeatRightClick = true;
                if(!isRepeatScheduled) {
                    isRepeatScheduled = true;
                    ClientScheduler.run(() -> maybeRepeat());
                }
            }
            return InteractionResult.PASS;
        });
        UseItemCallback.EVENT.register((player, level, hand) -> {
            if(shouldScheduleRepeats(level, hand, false)) {
                repeatRightClick = true;
                if(!isRepeatScheduled) {
                    isRepeatScheduled = true;
                    ClientScheduler.run(() -> maybeRepeat());
                }
            }
            return InteractionResult.PASS;
        });
    }



    private static Integer getInteractionCount() {
        final @NotNull ServerSteppedFeature<Integer> interactionCountFeature = CreativeTweaksServerFeatureSet.INTERACTION_COUNT;
        return interactionCountFeature.getValues().get(ClientFeatureSync.getFeatureI(interactionCountFeature));
    }



    private static boolean shouldScheduleRepeats(final Level level, InteractionHand hand, final boolean isAttack) {
        if(!isRepeating && (isAttack ? !repeatLeftClick : !repeatRightClick)) {
            //! Force client side only to avoid feedback loops on integrated servers.
            //! Forcing main hand isn't required but it helps making the logic more clear. The events are always called twice, once for each hand.
            if(level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                final @NotNull Player player = Minecraft.getInstance().player;
                if(player.isCreative()) {
                    final int count = getInteractionCount();
                    if(count > 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }




    /**
     * Fires left and right clicks in the amount specified by the Interaction Count feature, but only when needed.
     */
    private static void maybeRepeat() {
        isRepeating = true;
        if(repeatLeftClick) {
            repeat(true);
            repeatLeftClick = false;
        }
        if(repeatRightClick) {
            repeat(false);
            repeatRightClick = false;
        }
        isRepeating = false;
        isRepeatScheduled = false;
    }


    private static void repeat(final boolean isAttack) {
        final @NotNull Minecraft mc = Minecraft.getInstance();
        final @NotNull MinecraftAccessor mca = (MinecraftAccessor)mc;

        final int times = getInteractionCount();
        for(int i = 1; i < times; i++) {
            mca.invokePick(mc.getDeltaTracker().getGameTimeDeltaPartialTick(false));
            if(isAttack) {
                mca.invokeStartAttack();
            }
            else {
                mca.invokeStartUseItem();
            }
        }
    }
}
