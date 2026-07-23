package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class NormalRailPartProvider extends __base_RailPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.RAIL;
    }


    @Override
    protected String getRailTypeName() {
        return "rail";
    }


    @Override
    public List<String> calcDependencyNames() {
        final String railName = getRailTypeName();
        return List.of(
            String.format("rails/consistent_sloped/2d/%s/flat", railName),
            String.format("rails/consistent_sloped/2d/%s/raised", railName),
            String.format("rails/consistent_sloped/2d/%s/corner", railName),
            String.format("rails/consistent_sloped/3d/%s/flat", railName),
            String.format("rails/consistent_sloped/3d/%s/raised", railName),
            String.format("rails/consistent_sloped/3d/%s/corner", railName)
        );
    }
}
