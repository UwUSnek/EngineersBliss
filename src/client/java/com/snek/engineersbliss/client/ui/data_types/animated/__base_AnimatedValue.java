package com.snek.engineersbliss.client.ui.data_types.animated;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Easing;
import com.snek.engineersbliss.utils.Easings;

import net.minecraft.client.Minecraft;








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
    public T getLast() { return last; }
    public T getTarget() { return target; }




    protected __base_AnimatedValue(final T initialValue, final int transitionDuration) {
        this(initialValue, transitionDuration, Easings.sineInOut);
    }
    protected __base_AnimatedValue(final T initialValue, final int transitionDuration, final @NotNull Easing easing) {
        this.last = this.target = initialValue;
        this.transitionDuration = transitionDuration;
        this.easing = easing;
    }




    /**
     * Calculates the current linear progress.
     * This depends on the current time, the starting time of the last interpolation, and the configured interpolation duration.
     * @return The computed linear progress. Clamped between 0 and 1.
     */
    public double calcLinearProgress() {
        final long elapsed = System.currentTimeMillis() - lastChangeTime;
        return Math.clamp(elapsed / (double)transitionDuration, 0.0, 1.0);
    }
    /**
     * Calculates the linear progress of the last frame.
     * This depends on the last frame's time, the starting time of the last interpolation, and the configured interpolation duration.
     * @return The computed linear progress. Clamped between 0 and 1.
     */
    public double calcLastLinearProgress() {
        long frameDuration = (long)(1000d / Minecraft.getInstance().getWindow().getRefreshRate());
        final long elapsed = System.currentTimeMillis() - frameDuration - lastChangeTime;
        return Math.clamp(elapsed / (double)transitionDuration, 0.0, 1.0);
    }




    /**
     * Computes the interpolated value based on the current system time.
     * @return The interpolated value.
     */
    public T compute() {
        return interpolate(last, target, easing.compute(calcLinearProgress()));
    }
    protected abstract T interpolate(final T last, final T target, final double t);




    /**
     * Computes the current speed of the active interpolation, measured in 0-1_progress/frame
     * and normalized against a linear transition of same duration.
     *
     * A value of 1.0 means the transition is currently moving at the same rate
     * a linear easing would over this transitionDuration. 0 means idle.
     *
     * @return The calculated normalized speed.
     */
    public double calcSpeed() {
        final double frameDuration = 1000d / Minecraft.getInstance().getWindow().getRefreshRate();
        final double linearSpeed = frameDuration / transitionDuration;
        final double easedSpeed = easing.compute(calcLinearProgress()) - easing.compute(calcLastLinearProgress());
        return easedSpeed / linearSpeed;
    }




    /**
     * Starts a new interpolation using the stored interpolation time and easing and the provided new target value.
     * Does nothing if the new target value is identical to the current target.
     * @param newTarget The new target value.
     */
    public void startNewTransition(final T newTarget) {
        if(!getTarget().equals(newTarget)) {
            last = target;
            target = newTarget;
            lastChangeTime = System.currentTimeMillis();
        }
    }
}
