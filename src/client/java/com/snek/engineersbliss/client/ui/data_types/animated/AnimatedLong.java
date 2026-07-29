package com.snek.engineersbliss.client.ui.data_types.animated;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Easing;
import com.snek.engineersbliss.utils.Utils;




/**
 * A wrapper for an long value that supports transitions.
 */
public class AnimatedLong extends __base_AnimatedValue<Long> {


    public AnimatedLong(final long initialValue, final int transitionDuration) {
        super(initialValue, transitionDuration);
    }
    public AnimatedLong(final long initialValue, final int transitionDuration, final @NotNull Easing easing) {
        super(initialValue, transitionDuration, easing);
    }


    @Override
    public Long interpolate(final Long last, final Long target, final double t) {
        return Utils.interpolateI(last, target, (float)t);
    }
}
