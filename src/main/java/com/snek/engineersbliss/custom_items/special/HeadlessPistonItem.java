package com.snek.engineersbliss.custom_items.special;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom_items.CustomItemProperties;
import com.snek.engineersbliss.custom_items.base.CustomBlockItem;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;




public class HeadlessPistonItem extends CustomBlockItem {
    public HeadlessPistonItem(Block block, CustomItemProperties properties, final @Nullable List<Block> mappedBlocks) {
        super(block, properties, mappedBlocks);
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