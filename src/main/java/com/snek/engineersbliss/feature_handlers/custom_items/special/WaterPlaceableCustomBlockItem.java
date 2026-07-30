package com.snek.engineersbliss.feature_handlers.custom_items.special;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;




/**
 * A CustomBlockItem that only allows placement in water or flowing water.
 */
public class WaterPlaceableCustomBlockItem extends CustomBlockItem {

    public WaterPlaceableCustomBlockItem(Block block, CustomItemProperties p, final @Nullable List<Block> mappedBlocks) {
        super(block, p, mappedBlocks);
    }


    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        final BlockState state = super.getPlacementState(context);
        final @NotNull BlockPos pos = context.getClickedPos();
        final @NotNull FluidState fluidState = context.getLevel().getFluidState(pos);
        return state != null && (fluidState.is(Fluids.WATER) || fluidState.is(Fluids.FLOWING_WATER)) ? state : null;
    }
}
