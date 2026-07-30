package com.snek.engineersbliss.client.ui.data_types.animated;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Easing;
import com.snek.engineersbliss.utils.Utils;




/**
 * A wrapper for an double value that supports transitions.
 */
public class AnimatedDouble extends __base_AnimatedValue<Double> {


    public AnimatedDouble(final double initialValue, final int transitionDuration) {
        super(initialValue, transitionDuration);
    }
    public AnimatedDouble(final double initialValue, final int transitionDuration, final @NotNull Easing easing) {
        super(initialValue, transitionDuration, easing);
    }


    @Override
    public Double interpolate(final Double last, final Double target, final double t) {
        return Utils.interpolateF(last, target, (float)t);
    }
}
