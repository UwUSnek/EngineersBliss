package com.snek.engineersbliss.custom_items.special;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom_items.CustomItemProperties;
import com.snek.engineersbliss.custom_items.base.CustomBlockItem;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;




public class CustomCauldronItem extends CustomBlockItem {
    private final int level;

    public CustomCauldronItem(Block block, CustomItemProperties properties, final @Nullable List<Block> mappedBlocks, final int level) {
        super(block, properties, mappedBlocks);
        this.level = level;
    }


    // Force set levels on the placed block
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if(state != null && state.hasProperty(LayeredCauldronBlock.LEVEL)) {
            state = state.setValue(LayeredCauldronBlock.LEVEL, level);
        }
        return state;
    }
}