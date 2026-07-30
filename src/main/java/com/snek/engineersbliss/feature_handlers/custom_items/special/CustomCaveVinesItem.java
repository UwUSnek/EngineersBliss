package com.snek.engineersbliss.feature_handlers.custom_items.special;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemProperties;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.state.BlockState;




public class CustomCaveVinesItem extends CustomBlockItem {
    private final boolean berries;

    public CustomCaveVinesItem(Block block, final boolean berries, CustomItemProperties properties, final @Nullable List<Block> mappedBlocks) {
        super(block, properties, mappedBlocks);
        this.berries = berries;
    }


    // Force set berries on the placed block
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if(state != null && state.hasProperty(CaveVinesBlock.BERRIES)) {
            state = state.setValue(CaveVinesBlock.BERRIES, berries);
        }
        return state;
    }
}