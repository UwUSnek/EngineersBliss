package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.unique_blocks.rails.powerable;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.unique_blocks.rails.__base_RailPartProvider;

import net.minecraft.world.level.block.Block;




public abstract class __base_PowerableRailPartProvider extends __base_RailPartProvider {
    protected __base_PowerableRailPartProvider(Block targetBlock) {
        super(targetBlock);
    }


    @Override
    public List<String> calcDependencyNames() {
        final String railName = getRailTypeName();
        return List.of(
            String.format("rails/consistent_sloped/2d/%s/raised_off", railName),
            String.format("rails/consistent_sloped/2d/%s/raised_on",  railName),
            String.format("rails/consistent_sloped/2d/%s/flat_off",   railName),
            String.format("rails/consistent_sloped/2d/%s/flat_on",    railName),
            String.format("rails/consistent_sloped/3d/%s/raised_off", railName),
            String.format("rails/consistent_sloped/3d/%s/raised_on",  railName),
            String.format("rails/consistent_sloped/3d/%s/flat_off",   railName),
            String.format("rails/consistent_sloped/3d/%s/flat_on",    railName)
        );
    }
}
