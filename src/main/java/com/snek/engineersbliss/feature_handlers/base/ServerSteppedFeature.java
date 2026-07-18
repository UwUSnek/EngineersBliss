package com.snek.engineersbliss.feature_handlers.base;

import java.util.List;




/**
 * A server feature that can assume a limited amount of predetermined values.
 * ! The type of this feature is technically Integer, as the default and current values
 * ! represent the index of the selected value instead of the actual numerical value.
 */
public class ServerSteppedFeature<T> extends __base_ServerFeature<Integer> {
    protected final List<T> values;

    public List<T> getValues() { return values; }


    public ServerSteppedFeature(final String id, final List<T> values, final int defaultIndex) {
        super(id, defaultIndex);
        this.values = values;
    }
}
