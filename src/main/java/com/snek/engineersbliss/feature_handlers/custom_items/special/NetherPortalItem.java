package com.snek.engineersbliss.feature_handlers.custom_items.special;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemProperties;
import com.snek.engineersbliss.feature_handlers.custom_items.base.CustomBlockItem;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;




public class NetherPortalItem extends CustomBlockItem {

    public NetherPortalItem(Block block, CustomItemProperties p, @Nullable List<Block> mappedBlocks) {
        super(block, p, mappedBlocks, true);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        final BlockState state = super.getPlacementState(context);
        if(state == null) return null;

        final Direction.Axis facingAxis = context.getHorizontalDirection().getAxis();
        final Direction.Axis portalAxis = facingAxis == Direction.Axis.Z ? Direction.Axis.X : Direction.Axis.Z;
        return state.setValue(NetherPortalBlock.AXIS, portalAxis);
    }
}