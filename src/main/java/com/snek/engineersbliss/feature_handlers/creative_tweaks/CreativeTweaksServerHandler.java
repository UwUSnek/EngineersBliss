package com.snek.engineersbliss.feature_handlers.creative_tweaks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;








public class CreativeTweaksServerHandler {
    private CreativeTweaksServerHandler() {}
    public static final int DEFAULT_INTERACTION_RADIUS = 1;

    private static final float DEFAULT_REACH = 4.5f; //FIXME get this from somewhere instead of hard coding it
    private static final Identifier REACH_MODIFIER_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "creative_tweaks.reach");


    private static final Map<UUID, Integer> interactionRadii = new HashMap<>();
    private static @Nullable BlockPos pickOverride = null;
    public static @Nullable BlockPos getPickOverride() { return pickOverride; }




    /**
     * Updates the configured interaction radius for the specified player.
     * @param player The player.
     * @param value The new interaction radius, in blocks.
     */
    public static void updateInteractionRadius(final ServerPlayer player, final int value) {
        if(!player.getAbilities().instabuild) return;
        interactionRadii.put(player.getUUID(), value);
    }




    /**
     * Updates the reach distance attribute for the specified player.
     * @param player The player.
     * @param value The new reach distance, in blocks.
     */
    public static void updateReachDistance(final ServerPlayer player, final double value) {
        if(!player.getAbilities().instabuild) return;


        var blockAttr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
        if(blockAttr != null) {
            blockAttr.addOrUpdateTransientModifier(new AttributeModifier(
                REACH_MODIFIER_ID,
                value - DEFAULT_REACH,
                AttributeModifier.Operation.ADD_VALUE
            ));
        }
        var entityAttr = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        if(entityAttr != null) {
            entityAttr.addOrUpdateTransientModifier(new AttributeModifier(
                REACH_MODIFIER_ID,
                value - DEFAULT_REACH,
                AttributeModifier.Operation.ADD_VALUE
            ));
        }
    }







    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(CreativeTweaksServerHandler::beforeBlockBreak);
        UseBlockCallback.EVENT.register(CreativeTweaksServerHandler::afterBlockUse);
    }



    private static boolean processingCustomBreak = false;
    private static boolean beforeBlockBreak(Level level, Player player, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity) {
        if(level.isClientSide()) return true;
        if(!player.getAbilities().instabuild) return true;


        // Break all blocks in a radius, only if the current event was not triggered by a custom radius block break
        if(!processingCustomBreak) {
            processingCustomBreak = true;
            int radius = interactionRadii.getOrDefault(player.getUUID(), DEFAULT_INTERACTION_RADIUS) - 1;

            BlockPos.betweenClosed(
                pos.offset(-radius, -radius, -radius),
                pos.offset( radius,  radius,  radius)
            ).forEach(pos2 -> {
                if(!pos2.equals(pos)) { //! Skip vanilla block
                    if(new Vec3(pos2).distanceTo(new Vec3(pos)) <= radius) {
                        level.destroyBlock(pos2, false, player);
                    }
                }
            });
            processingCustomBreak = false;
        }

        // Return true, letting vanilla break the original block
        return true;
    }




    private static boolean processingCustomPlace = false;
    private static InteractionResult afterBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.PASS;
        if(!player.getAbilities().instabuild) return InteractionResult.PASS;


        // Break all blocks in a radius, only if the current event was not triggered by a custom radius block break
        if(!processingCustomPlace) {
            processingCustomPlace = true;
            int radius = interactionRadii.getOrDefault(player.getUUID(), DEFAULT_INTERACTION_RADIUS) - 1;
            Vec3 vec3Pos = blockHitResult.getLocation();
            BlockPos pos = BlockPos.containing(vec3Pos);

            final ItemStack stack = player.getActiveItem();
            BlockPos.betweenClosed(
                pos.offset(-radius, -radius, -radius),
                pos.offset( radius,  radius,  radius)
            ).forEach(pos2 -> {
                if(!pos2.equals(pos)) { //! Skip vanilla block
                    if(new Vec3(pos2).distanceTo(new Vec3(pos)) <= radius) {
                        final ItemStack stackCopy = stack.copy(); //! Use a copy so entity items don't run out in creative mode
                        final InteractionResult result = stackCopy.useOn(new UseOnContext(
                            level, player, hand, stackCopy,
                            new BlockHitResult(Vec3.atCenterOf(pos2), blockHitResult.getDirection(), pos2, false)
                        ));
                        if(result == InteractionResult.PASS) {
                            pickOverride = pos2;
                            stackCopy.use(level, player, hand);
                            pickOverride = null;
                        }
                    }
                }
            });
            processingCustomPlace = false;
        }

        // Return PASS, letting vanilla click the original block
        return InteractionResult.PASS;
    }
}
