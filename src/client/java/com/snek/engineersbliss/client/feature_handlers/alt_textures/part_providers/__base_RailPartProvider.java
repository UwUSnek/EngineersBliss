package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;




public abstract class __base_RailPartProvider extends __base_PartProvider {
    protected abstract String getRailTypeName();
    private static final List<RailShape> CURVED_SHAPES = List.of(RailShape.NORTH_EAST, RailShape.NORTH_WEST, RailShape.SOUTH_EAST, RailShape.SOUTH_WEST);




    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        final boolean isConsistent = AltTexturesHandler.getFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS);
        final boolean is3D         = AltTexturesHandler.getFeature(AltTextureFeature.RAILS_3D);

        //! Consistent models are forced if 3D models are in use
        if(isConsistent || is3D) {

            //! Sloped shape names have the format "ascending_<direction>" so this uses that directly by removing "ascending_" as that matches the json file names perfectly.
            //! Non-sloped shape names already match json models so no changes are needed there. This includes curved normal rails.
            //! All shape names are already lowercase.
            final RailShape shape = state.getValue(((BaseRailBlock)state.getBlock()).getShapeProperty());
            final String shapeName = shape.isSlope() ? "raised" : (CURVED_SHAPES.contains(shape) ? "corner" : "flat");
            final String directionName = switch(shape) {
                case ASCENDING_NORTH, NORTH_SOUTH, NORTH_EAST -> "_n";
                case ASCENDING_EAST,  EAST_WEST,   SOUTH_EAST -> "_e";
                case ASCENDING_SOUTH,              SOUTH_WEST -> "_s";
                case ASCENDING_WEST,               NORTH_WEST -> "_w";
            };
            final String poweredStateName = state.getBlock() != Blocks.RAIL ?
                state.getValue(BlockStateProperties.POWERED).booleanValue() ? "_on" : "_off" :
                ""
            ;
            // return List.of("rails/consistent_sloped/" + (is3D ? "3d" : "2d") + "/" + getRailTypeName() + "/" + shapeName + "_" + poweredStateName);
            return List.of("rails/consistent_sloped/" + ("2d") + "/" + getRailTypeName() + "/" + shapeName + poweredStateName + directionName);
        }
        else {
            return null;
        }
    }




    @Override
    public boolean shouldKeepVanilla(BlockState state) {
        return
            ! AltTexturesHandler.getFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS) &&
            ! AltTexturesHandler.getFeature(AltTextureFeature.RAILS_3D)
        ;
    }
}
