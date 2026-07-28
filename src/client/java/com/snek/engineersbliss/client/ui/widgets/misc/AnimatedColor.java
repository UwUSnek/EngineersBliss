package com.snek.engineersbliss.client.ui.widgets.misc;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Easing;
import com.snek.engineersbliss.utils.Easings;
import com.snek.engineersbliss.utils.Utils;




/**
 * A wrapper for an ARGB color that supports transitions.
 */
public class AnimatedColor {
    private int last;
    private int target;
    private long transitionDuration;
    private long lastChangeTime;
    private Easing easing;


    public long getTransitionDuration() { return transitionDuration; }
    public void setTransitionDuration(final long transitionDuration) { this.transitionDuration = transitionDuration; }


    public AnimatedColor(final int initialValue, final int transitionDuration) {
        this(initialValue, transitionDuration, Easings.sineInOut);
    }
    public AnimatedColor(final int initialValue, final int transitionDuration, final @NotNull Easing easing) {
        this.last = this.target = initialValue;
        this.transitionDuration = transitionDuration;
        this.easing = easing;
    }


    public int calcCurrentColor() {
        long elapsed = System.currentTimeMillis() - lastChangeTime;
        double t = Math.clamp(elapsed / (double)transitionDuration, 0.0, 1.0);
        return Utils.interpolatePackedARGB(last, target, (float)easing.compute(t));
    }


    public void startNewTransition(final int newTargetColor) {
        last = target;
        target = newTargetColor;
        lastChangeTime = System.currentTimeMillis();
    }
}
