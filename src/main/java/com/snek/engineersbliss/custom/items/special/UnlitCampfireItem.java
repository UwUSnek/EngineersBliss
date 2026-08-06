package com.snek.engineersbliss.custom.items.special;

import com.snek.engineersbliss.custom.items.CustomItemProperties;
import com.snek.engineersbliss.custom.items.base.CustomBlockItem;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;




public class UnlitCampfireItem extends CustomBlockItem {

    public UnlitCampfireItem(Block block, CustomItemProperties properties) {
        super(block, properties, null);
    }


    // Force set unlit state on the placed block
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if(state != null && state.hasProperty(CampfireBlock.LIT)) {
            state = state.setValue(CampfireBlock.LIT, false);
        }
        return state;
    }
}