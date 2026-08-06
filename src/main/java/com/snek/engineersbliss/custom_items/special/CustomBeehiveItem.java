package com.snek.engineersbliss.custom_items.special;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom_items.CustomItemProperties;
import com.snek.engineersbliss.custom_items.base.CustomBlockItem;
import com.snek.engineersbliss.mixin.accessors.BeeAccessor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;




public class CustomBeehiveItem extends CustomBlockItem {
    private final int beeCount;
    private final int honeyLevel;

    public CustomBeehiveItem(Block block, CustomItemProperties properties, int beeCount, int honeyLevel, final @Nullable List<Block> mappedBlocks) {
        super(block, properties, mappedBlocks);
        this.beeCount  = beeCount;
        this.honeyLevel = honeyLevel;
    }


    // Force set honey level on the placed block
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if(state != null && state.hasProperty(BeehiveBlock.HONEY_LEVEL)) {
            state = state.setValue(BeehiveBlock.HONEY_LEVEL, honeyLevel);
        }
        return state;
    }


    // Put bees in the block after it's placed
    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);

        if(result.consumesAction() && context.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pos = context.getClickedPos();
            if(serverLevel.getBlockEntity(pos) instanceof BeehiveBlockEntity hive) {
                for(int i = 0; i < beeCount; i++) {
                    Bee bee = EntityType.BEE.create(serverLevel, EntitySpawnReason.STRUCTURE);
                    if(bee != null) {
                        ((BeeAccessor)bee).invokeSetHasNectar(true);
                        hive.addOccupant(bee);
                    }
                }
                hive.setChanged();
            }
        }
        return result;
    }
}