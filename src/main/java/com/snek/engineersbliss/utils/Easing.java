package com.snek.engineersbliss.utils;

import java.util.function.UnaryOperator;

import org.jetbrains.annotations.NotNull;








/**
 * A class that can store and compute a unary operator on request.
 * <p>
 * This is meant for interpolation easings.
 * @since v1.1.0
 */
public final class Easing {
    private final @NotNull UnaryOperator<@NotNull Double> f;


    /**
     * Creates a new Easing with the specified operator.
     * @param f The operator.
     *     This function takes the linear progress and returns the corresponding progress that
     *     the custom easing would produce at the same point in time.
     */
    public Easing(final @NotNull UnaryOperator<@NotNull Double> f) {
        this.f = f;
    }


    /**
     * This function takes the linear progress and returns the corresponding progress that
     * the custom easing would produce at the same point in time.
     * @param x The linear progress.
     * @return The progress with the custom easing applied.
     */
    public double compute(final double x) {
        return f.apply(x);
    }
}




//TODO replace with a proper frameworklib dependency
//TODO replace with a proper frameworklib dependency
//TODO replace with a proper frameworklib dependency
//TODO replace with a proper frameworklib dependency