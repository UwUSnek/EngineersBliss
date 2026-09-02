package com.snek.engineersbliss.client.ui.data_types;

import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;








public class UiSize {
    private float px;
    private float widthFrac;
    private float heightFrac;
    private final __base_UiWidget widget; //! Width and height are read from this element




    public UiSize(final __base_UiWidget widget) {
        this.widget = widget;
    }




    public UiSize addPx(final float v) {
        px += v; return this;
    }
    public UiSize addWF(final float v) {
        widthFrac += v; return this;
    }
    public UiSize addHF(final float v) {
        heightFrac += v; return this;
    }




    public UiSize setPx(final float v) {
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




    public float getPx() {
        return px + widthFrac  * widget.getWidthF() + heightFrac * widget.getHeightF();
    }
}