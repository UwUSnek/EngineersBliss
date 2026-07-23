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
    private static final String FEATURE_SET_ID = CreativeTweaksServerFeatureSet.INSTANCE.getId();
    private static final int DEFAULT_INTERACTION_RADIUS = 1;
    private static final float DEFAULT_REACH = 4.5f; //FIXME get this from somewhere instead of hard coding it
    private static final Identifier REACH_MODIFIER_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, FEATURE_SET_ID + ".reach");
    private static final Identifier SPEED_MODIFIER_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, FEATURE_SET_ID + ".walking_speed");


    private static final Map<UUID, Integer> interactionRadii = new HashMap<>();
    private static @Nullable BlockPos pickOverride = null;
    public static @Nullable BlockPos getPickOverride() { return pickOverride; }




    //! Walking speed needs a custom attribute.
    //! player.getAbilities().setWalkingSpeed(n) doesn't actually set the walking speed. It only changes FOV.
    public static void updateWalkingSpeed(final Player player, final int valueIndex) {
        if(!player.isCreative()) return;


        final float newValue = CreativeTweaksServerFeatureSet.WALKING_SPEED.getValues().get(valueIndex);
        final var attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if(attribute != null) {
            attribute.addOrUpdateTransientModifier(new AttributeModifier(
                SPEED_MODIFIER_ID,
                newValue - 1,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }
    }




    public static void updateInteractionRadius(final Player player, final int valueIndex) {
        if(!player.isCreative()) return;
        final int newValue = CreativeTweaksServerFeatureSet.INTERACTION_RADIUS.getValues().get(valueIndex);
        interactionRadii.put(player.getUUID(), newValue);
    }




    public static void updateInteractionDistance(final Player player, final int valueIndex) {
        if(!player.isCreative()) return;


        final float newValue = CreativeTweaksServerFeatureSet.INTERACTION_DISTANCE.getValues().get(valueIndex);
        final var blockAttr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
        if(blockAttr != null) {
            blockAttr.addOrUpdateTransientModifier(new AttributeModifier(
                REACH_MODIFIER_ID,
                newValue - DEFAULT_REACH,
                AttributeModifier.Operation.ADD_VALUE
            ));
        }
        final var entityAttr = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        if(entityAttr != null) {
            entityAttr.addOrUpdateTransientModifier(new AttributeModifier(
                REACH_MODIFIER_ID,
                newValue - DEFAULT_REACH,
                AttributeModifier.Operation.ADD_VALUE
            ));
        }
    }







    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(CreativeTweaksServerHandler::beforeBlockBreak);
        UseBlockCallback.EVENT.register(CreativeTweaksServerHandler::afterBlockUse);
    }



    private static boolean processingCustomBreak = false;

    @SuppressWarnings("java:S3516")
    private static boolean beforeBlockBreak(final Level level, final Player player, final BlockPos pos, final BlockState blockState, @Nullable final BlockEntity blockEntity) {
        if(level.isClientSide()) return true;
        if(!player.isCreative()) return true;


        // Break all blocks in a radius, only if the current event was not triggered by a custom radius block break
        if(!processingCustomBreak) {
            processingCustomBreak = true;
            final int radius = interactionRadii.getOrDefault(player.getUUID(), DEFAULT_INTERACTION_RADIUS) - 1;

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
    private static InteractionResult afterBlockUse(final Player player, final Level level, final InteractionHand hand, final BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.PASS;
        if(!player.isCreative()) return InteractionResult.PASS;


        // Break all blocks in a radius, only if the current event was not triggered by a custom radius block break
        if(!processingCustomPlace) {
            processingCustomPlace = true;
            final int radius = interactionRadii.getOrDefault(player.getUUID(), DEFAULT_INTERACTION_RADIUS) - 1;
            final Vec3 vec3Pos = blockHitResult.getLocation();
            final BlockPos pos = BlockPos.containing(vec3Pos);

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
