package com.snek.engineersbliss.custom.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;




public class FrictionBlock extends TransparentBlock {
    public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 0, 15);


    public FrictionBlock(BlockBehaviour.Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(HEIGHT, 15));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HEIGHT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        double y = ctx.getClickLocation().y;
        int pixel = Mth.clamp((int) Math.floor((y - Math.floor(y)) * 16), 0, 15);
        return this.defaultBlockState().setValue(HEIGHT, pixel);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        int h = state.getValue(HEIGHT);
        return Block.box(0, h, 0, 16, h + 1f, 16);
    }
}
