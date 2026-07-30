package com.snek.engineersbliss.feature_handlers.custom_items.special;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;




public class PistonHeadItem extends CustomBlockItem {
    private final boolean sticky;
    private final boolean _short;


    public PistonHeadItem(Block block, final boolean sticky, final boolean _short, Properties properties) {
        super(block, properties);
        this.sticky = sticky;
        this._short = _short;
    }


    @Override
    protected boolean mustSurvive() {
        return false;
    }


    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if(state != null && state.hasProperty(PistonHeadBlock.FACING)) {

            //! Define custom placement direction.
            //! Vanilla piston heads don't have this since they aren't meant to be placed by players.
            Direction facing = context.getNearestLookingDirection().getOpposite();
            state = state.setValue(PistonHeadBlock.FACING, facing);

            // Make the piston head sticky if needed. Both sticky and non sticky heads share the same block.
            if(sticky) state = state.setValue(PistonHeadBlock.TYPE, PistonType.STICKY);

            // Set piston head length property
            state = state.setValue(PistonHeadBlock.SHORT, _short);
        }
        return state;
    }
}