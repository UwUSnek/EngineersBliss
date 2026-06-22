package com.snek.engineersbliss.network.overlay_data.resolvers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;








public class RailInputDataResolver {
    private RailInputDataResolver() {}


    /**
     * Finds the power level of the rail at the specified block position.
     * @param level The level containing the rail block.
     * @param pos The position of the rail block.
     * @return The power level, 0 to 9.
     */
    public static int calcPowerLevel(final Level level, final BlockPos pos) {
        return Math.max(
            calcPowerLevel(level, pos, +1),
            calcPowerLevel(level, pos, -1)
        );
    }



//TODO TRACK SOURCES
//TODO DRAW ARROWS TO SOURCE BLOCK
//TODO ADD THIS TO CUSTOM RENDERING OVERLAYS

//TODO TRACK SOURCES
//TODO DRAW ARROWS TO SOURCE BLOCK
//TODO ADD THIS TO CUSTOM RENDERING OVERLAYS



    /**
     * Calculates the power levelo of the rail at the specified position, checking only in the specified direction (+1/forward or -1/backwards)
     */
    private static int calcPowerLevel(final Level level, final BlockPos startPos, final int direction) {
        int power = 9;
        int x = startPos.getX();
        int y = startPos.getY();
        int z = startPos.getZ();
        @Nullable RailShape prevShape = null;
        final PoweredRailBlock rail = (PoweredRailBlock)(level.getBlockState(startPos).getBlock());


        // Iterate towards the specified direction until the block is too far to receive power
        while(power > 0) {
            BlockPos curPos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(curPos);

            // If the block is not a PoweredRailBlock, move to the block below it
            //! This is never called on the first block, as the first block is always a PoweredRailBlock
            if(!(state.getBlock() instanceof PoweredRailBlock)) {
                curPos = new BlockPos(x, --y, z);
                state = level.getBlockState(curPos);

                // If the block below is not a connected rail of the same type, return power 0 (No source found, no other possible paths left)
                if(!isSameRailAndConnected(level, curPos, prevShape, rail)) {
                    return 0;
                }
            }

            // If this isn't the first block in the chain, check that it's a connected rail of the same type. Return power 0 if not
            if(prevShape != null && !isSameRailAndConnected(level, curPos, prevShape, rail)) {
                return 0;
            }

            // If this block is the source, return the power level
            if(level.hasNeighborSignal(curPos)) {
                return power;
            }

            // If this block is not the source, fetch its shape and update the coordinates for the next iteration
            final RailShape shape = state.getValue(rail.getShapeProperty());
            switch(shape) {
                case NORTH_SOUTH, ASCENDING_NORTH, ASCENDING_SOUTH: { z += direction; break; }
                case EAST_WEST,   ASCENDING_EAST,  ASCENDING_WEST:  { x += direction; break; }
                default: break;
            }
            switch(shape) {
                case ASCENDING_NORTH: { if (direction == -1) ++y; break; }
                case ASCENDING_SOUTH: { if (direction == +1) ++y; break; }
                case ASCENDING_EAST:  { if (direction == +1) ++y; break; }
                case ASCENDING_WEST:  { if (direction == -1) ++y; break; }
                default: break;
            }

            // Update previous shape value and decrease power level
            prevShape = shape;
            --power;
        }

        return power;
    }




    /**
     * This is just a copy of Vanilla's own check but with a simpler logic
     */
    private static boolean isSameRailAndConnected(final Level level, final BlockPos pos, final RailShape prevShape, final PoweredRailBlock rail) {
        BlockState state = level.getBlockState(pos);
        if(!state.is(rail)) {
            return false;
        }

        RailShape curShape = state.getValue(rail.getShapeProperty());
        boolean prevEastWest =
            prevShape == RailShape.EAST_WEST      ||
            prevShape == RailShape.ASCENDING_EAST ||
            prevShape == RailShape.ASCENDING_WEST
        ;
        boolean curEastWest =
            curShape == RailShape.EAST_WEST       ||
            curShape == RailShape.ASCENDING_EAST  ||
            curShape == RailShape.ASCENDING_WEST
        ;

        return prevEastWest == curEastWest;
    }
}
