package com.snek.engineersbliss.feature_handlers.custom_items.special;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemProperties;
import com.snek.engineersbliss.utils.data_types.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;




public class ArmorStandWithArmsItem extends Item {
    public ArmorStandWithArmsItem(CustomItemProperties p) {
        super(p);
    }


    /**
     * 26.1 Vanilla's ArmorStandItem.useOn verbatim + the created armor stand in the return value.
     */
    public Pair<InteractionResult, ArmorStand> vanillaUseOn(UseOnContext context) {
        Direction clickedFace = context.getClickedFace();
        if(clickedFace == Direction.DOWN) {
            return Pair.from(InteractionResult.FAIL, null);
        }
        else {
            Level level = context.getLevel();
            BlockPlaceContext placeContext = new BlockPlaceContext(context);
            BlockPos blockPos = placeContext.getClickedPos();
            ItemStack itemStack = context.getItemInHand();
            Vec3 pos = Vec3.atBottomCenterOf(blockPos);
            AABB box = EntityType.ARMOR_STAND.getDimensions().makeBoundingBox(pos.x(), pos.y(), pos.z());
            ArmorStand entity = null;
            if(level.noCollision(null, box) && level.getEntities(null, box).isEmpty()) {
                if(level instanceof ServerLevel serverLevel) {
                    Consumer<ArmorStand> entityConfig = EntityType.createDefaultStackConfig(serverLevel, itemStack, context.getPlayer());
                    entity = EntityType.ARMOR_STAND.create(serverLevel, entityConfig, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);
                    if(entity == null) {
                        Pair.from(InteractionResult.FAIL, null);
                    }

                    float yRot = Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), yRot, 0.0F);
                    serverLevel.addFreshEntityWithPassengers(entity);
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                    entity.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
                }

                itemStack.shrink(1);
                return Pair.from(InteractionResult.SUCCESS, entity);
            }
            else {
                return Pair.from(InteractionResult.FAIL, null);
            }
        }
    }



    @Override
    public InteractionResult useOn(UseOnContext c) {
        final @NotNull var rp = vanillaUseOn(c);
        final @NotNull InteractionResult r = rp.getFirst();
        final @Nullable ArmorStand entity = rp.getSecond();
        if(r == InteractionResult.SUCCESS && entity != null) {
            entity.setShowArms(true);
        }
        return r;
    }
}