package com.snek.engineersbliss.client.ui.widgets.base;



public interface DualPreviewFeatureInputWidget extends FeatureInputWidget {

    /**
     * Returns the suffix of the name of the preview to be shown on the left side of the screen.
     * This doesn't include the numerical suffix of atlases.
     */
    public String getLeftPreviewSuffix();

    /**
     * Returns the title to be shown behind the left preview texture.
     */
    public String getLeftTitle();

    /**
     * Returns the suffix of the name of the preview to be shown on the right side of the screen.
     * This doesn't include the numerical suffix of atlases.
     */
    public String getRightPreviewSuffix();

    /**
     * Returns the title to be shown behind the right preview texture.
     */
    public String getRightTitle();
}