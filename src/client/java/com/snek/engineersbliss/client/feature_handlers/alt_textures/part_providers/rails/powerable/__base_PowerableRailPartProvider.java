package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.powerable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.rails.__base_RailPartProvider;

import java.util.List;




public abstract class __base_PowerableRailPartProvider extends __base_RailPartProvider {

    @Override
    public List<String> calcDependencyNames() {
        final String railName = getRailTypeName();
        return List.of(
            String.format("rails/consistent_sloped/2d/%s/raised_off", railName),
            String.format("rails/consistent_sloped/2d/%s/raised_on", railName),
            String.format("rails/consistent_sloped/2d/%s/flat_off", railName),
            String.format("rails/consistent_sloped/2d/%s/flat_on", railName),
            String.format("rails/consistent_sloped/3d/%s/raised_off", railName),
            String.format("rails/consistent_sloped/3d/%s/raised_on", railName),
            String.format("rails/consistent_sloped/3d/%s/flat_off", railName),
            String.format("rails/consistent_sloped/3d/%s/flat_on", railName)
        );
    }
}
