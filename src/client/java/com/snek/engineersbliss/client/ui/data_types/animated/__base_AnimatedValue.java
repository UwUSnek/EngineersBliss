package com.snek.engineersbliss.client.ui.data_types.animated;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Easing;
import com.snek.engineersbliss.utils.Easings;








/**
 * The base class for any animated value.
 * This handles the entire transition system and related calculations.
 * Subclasses only need to define the interpolation method.
 */
public abstract class __base_AnimatedValue<T> {
    private T last;
    private T target;
    private long transitionDuration;
    private long lastChangeTime;
    private Easing easing;


    public long getTransitionDuration() { return transitionDuration; }
    public void setTransitionDuration(final long transitionDuration) { this.transitionDuration = transitionDuration; }
    public Easing getEasing() { return easing; }
    public void   setEasing(final Easing easing) { this.easing = easing; }




    protected __base_AnimatedValue(final T initialValue, final int transitionDuration) {
        this(initialValue, transitionDuration, Easings.sineInOut);
    }
    protected __base_AnimatedValue(final T initialValue, final int transitionDuration, final @NotNull Easing easing) {
        this.last = this.target = initialValue;
        this.transitionDuration = transitionDuration;
        this.easing = easing;
    }




    /**
     * Computes the interpolated value based on the current system time.
     * @return The interpolated value.
     */
    public T compute() {
        final long elapsed = System.currentTimeMillis() - lastChangeTime;
        final double t = Math.clamp(elapsed / (double)transitionDuration, 0.0, 1.0);
        return interpolate(last, target, easing.compute(t));
    }
    protected abstract T interpolate(final T last, final T target, final double t);


    /**
     * Starts a new interpolation using the stored interpolation time and easing and the provided new target value.
     * @param newTarget The new target value.
     */
    public void startNewTransition(final T newTarget) {
        last = target;
        target = newTarget;
        lastChangeTime = System.currentTimeMillis();
    }
}
