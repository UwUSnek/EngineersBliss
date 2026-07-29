package com.snek.engineersbliss.client.ui.data_types.animated;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Easing;
import com.snek.engineersbliss.utils.Utils;




/**
 * A wrapper for an float value that supports transitions.
 */
public class AnimatedFloat extends __base_AnimatedValue<Float> {


    public AnimatedFloat(final float initialValue, final int transitionDuration) {
        super(initialValue, transitionDuration);
    }
    public AnimatedFloat(final float initialValue, final int transitionDuration, final @NotNull Easing easing) {
        super(initialValue, transitionDuration, easing);
    }


    @Override
    public Float interpolate(final Float last, final Float target, final double t) {
        return Utils.interpolateF(last, target, (float)t);
    }
}
