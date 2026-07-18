package com.snek.engineersbliss.feature_handlers.base;




/**
 * A server feature that can assume an unlimited number of possible values.
 */
public class ServerAnalogueFeature<T> extends __base_ServerFeature<T> {
    protected final T min;
    protected final T max;

    public T getMin() { return min; }
    public T getMax() { return max; }


    public ServerAnalogueFeature(final String id, final T min, final T max, final T defaultValue) {
        super(id, defaultValue);
        this.min = min;
        this.max = max;
    }
}
