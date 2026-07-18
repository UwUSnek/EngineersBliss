package com.snek.engineersbliss.feature_handlers.base;




/**
 * A server feature that can only be toggled ON or OFF.
 */
public class ServerToggleFeature extends __base_ServerFeature<Boolean> {

    public ServerToggleFeature(final String id, final boolean defaultValue) {
        super(id, defaultValue);
    }
}
