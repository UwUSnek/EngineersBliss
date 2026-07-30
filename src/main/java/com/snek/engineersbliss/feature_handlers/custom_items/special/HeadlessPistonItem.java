package com.snek.engineersbliss.feature_handlers.custom_items.special;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;




public class HeadlessPistonItem extends __base_CustomBlockItem {
    public HeadlessPistonItem(Block block, Properties properties) {
        super(block, properties);
    }


    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if(state != null && state.hasProperty(PistonBaseBlock.EXTENDED)) {
            state = state.setValue(PistonBaseBlock.EXTENDED, true);
        }
        return state;
    }
}