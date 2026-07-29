package com.snek.engineersbliss.client.ui.data_types.animated;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Easing;
import com.snek.engineersbliss.utils.Utils;




/**
 * A wrapper for an int value that supports transitions.
 */
public class AnimatedInt extends __base_AnimatedValue<Integer> {


    public AnimatedInt(final int initialValue, final int transitionDuration) {
        super(initialValue, transitionDuration);
    }
    public AnimatedInt(final int initialValue, final int transitionDuration, final @NotNull Easing easing) {
        super(initialValue, transitionDuration, easing);
    }


    @Override
    public Integer interpolate(final Integer last, final Integer target, final double t) {
        return Utils.interpolateI(last, target, (float)t);
    }
}
