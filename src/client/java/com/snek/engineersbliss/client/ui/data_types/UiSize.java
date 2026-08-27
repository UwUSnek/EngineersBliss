package com.snek.engineersbliss.client.ui.data_types;

import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;








public class UiSize {
    private int px;
    private float widthFrac;
    private float heightFrac;
    private final __base_UiWidget widget; //! Width and height are read from this element




    public UiSize(final __base_UiWidget widget) {
        this.widget = widget;
    }




    public UiSize addPx(final int v) {
        px += v; return this;
    }
    public UiSize addWF(final float v) {
        widthFrac += v; return this;
    }
    public UiSize addHF(final float v) {
        heightFrac += v; return this;
    }




    public UiSize setPx(final int v) {
        px = v; return this;
    }
    public UiSize setWF(final float v) {
        widthFrac = v; return this;
    }
    public UiSize setHF(final float v) {
        heightFrac = v; return this;
    }




    public UiSize clear() {
        px = 0;
        widthFrac = 0;
        heightFrac = 0;
        return this;
    }




    public int getPx() {
        return px + (int)(widthFrac  * widget.getWidth() + heightFrac * widget.getHeight());
    }
}