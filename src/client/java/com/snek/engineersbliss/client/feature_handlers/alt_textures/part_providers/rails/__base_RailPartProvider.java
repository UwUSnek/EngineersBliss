package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;




public abstract class __base_RailPartProvider extends __base_PartProvider {
    protected abstract String getRailTypeName();
    private static final List<RailShape> CURVED_SHAPES = List.of(RailShape.NORTH_EAST, RailShape.NORTH_WEST, RailShape.SOUTH_EAST, RailShape.SOUTH_WEST);
    private static final List<String> railPathForSet = List.of("2d", "3d");



    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {

        //! Sloped shape names have the format "ascending_<direction>" so this uses that directly by removing "ascending_" as that matches the json file names perfectly.
        //! Non-sloped shape names already match json models so no changes are needed there. This includes curved normal rails.
        //! All shape names are already lowercase.
        final String railName = getRailTypeName();
        final @NotNull RailShape shape = state.getValue(((BaseRailBlock)state.getBlock()).getShapeProperty());
        final String shapeName = shape.isSlope() ? "raised" : (CURVED_SHAPES.contains(shape) ? "corner" : "flat");
        final String directionName = switch(shape) {
            case ASCENDING_NORTH, NORTH_SOUTH, NORTH_EAST -> "_n";
            case ASCENDING_EAST,  EAST_WEST,   SOUTH_EAST -> "_e";
            case ASCENDING_SOUTH,              SOUTH_WEST -> "_s";
            case ASCENDING_WEST,               NORTH_WEST -> "_w";
        };
        final String poweredStateName = state.getBlock() != Blocks.RAIL ?
            (state.getValue(BlockStateProperties.POWERED).booleanValue() ? "_on" : "_off") :
            ""
        ;

        return List.of(String.format("rails/consistent_sloped/%s/%s/%s%s%s", railPathForSet.get(modelSetIndex), railName, shapeName, poweredStateName, directionName));
    }
    //! Dependencies are defined by subclasses




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return
            AltTexturesHandler.getFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS) ||
            AltTexturesHandler.getFeature(AltTextureFeature.RAILS_3D)
        ;
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }




    @Override
    public int getModelSetNumber() {
        return 2;
    }
    @Override
    public int calcCurrentModelSetIndex() {
        return AltTexturesHandler.getFeature(AltTextureFeature.RAILS_3D) ? 1 : 0;
    }
}
