package com.snek.engineersbliss.custom_items.special;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom_items.CustomItemProperties;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;





public class CustomBedHalfBlockItem extends CustomHalfBlockItem {
    private boolean isFoot() { return isBottom(); }


    public CustomBedHalfBlockItem(Block block, CustomItemProperties p, final boolean isFoot) {
        super(block, p, isFoot);
    }

    public CustomBedHalfBlockItem(Block block, CustomItemProperties p, final boolean isFoot, final @Nullable List<Block> mappedBlocks) {
        super(block, p, isFoot, mappedBlocks);
    }



    // Force set half type on the placed block
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = buildCustomPlacementState(context);
        if(state != null && state.hasProperty(BedBlock.PART)) {
            state = state.setValue(BedBlock.PART, isFoot() ? BedPart.FOOT : BedPart.HEAD);
        }
        return state;
    }




    //! Inherit fixed place method from CustomHalfBlockItem
}
