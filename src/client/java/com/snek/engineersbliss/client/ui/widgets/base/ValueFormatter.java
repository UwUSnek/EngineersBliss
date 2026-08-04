package com.snek.engineersbliss.client.ui.widgets.base;


@FunctionalInterface
public interface ValueFormatter<T> {
    public String format(final T n, final boolean shortUnit);
}
