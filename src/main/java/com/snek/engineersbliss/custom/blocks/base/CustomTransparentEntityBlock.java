package com.snek.engineersbliss.custom.blocks.base;

import java.util.function.BiFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;








public class CustomTransparentEntityBlock extends CustomEntityBlock {
    public CustomTransparentEntityBlock(Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFactory) {
        super(properties, blockEntityFactory);
    }

    @Override
    protected boolean skipRendering(final BlockState state, final BlockState neighborState, final Direction direction) {
        return neighborState.is(this) ? true : super.skipRendering(state, neighborState, direction);
    }

    @Override
    protected VoxelShape getVisualShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected float getShadeBrightness(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(final BlockState state) {
        return true;
    }
}
