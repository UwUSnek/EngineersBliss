package com.snek.engineersbliss.client.ui.base;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;








/**
 * A UiScreen that comes with sidebars.
 */
public abstract class UiSidebarScreen extends UiScreen {
    public static float DEFAULT_SIDEBAR_WIDTH = 0.2f;
    public static int DEFAULT_SIDEBAR_COLOR = 0xAA1F1F1F;

    // Elements and layout
    protected boolean hasLeftSidebar;
    protected boolean hasRightSidebar;
    protected float leftSidebarWidth;  //TODO replace with UiSiz
    protected float rightSidebarWidth; //TODO replace with UiSize
    protected UiWidgetList leftSidebar;
    protected UiWidgetList rightSidebar;




    /**
     * Creates a screen with left and right sidebars of default width.
     */
    protected UiSidebarScreen() {
        this(DEFAULT_SIDEBAR_WIDTH, DEFAULT_SIDEBAR_WIDTH);
    }



    protected UiSidebarScreen(final @Nullable Float leftSidebarWidth, final @Nullable Float rightSidebarWidth, final int initialBgColor, final float initialBgBlurRadius) {
        super(initialBgColor, initialBgBlurRadius);
        finalizeInit(leftSidebarWidth, rightSidebarWidth);
    }
    protected UiSidebarScreen(final @Nullable Float leftSidebarWidth, final @Nullable Float rightSidebarWidth, final float initialBgBlurRadius) {
        super(initialBgBlurRadius);
        finalizeInit(leftSidebarWidth, rightSidebarWidth);
    }
    protected UiSidebarScreen(final @Nullable Float leftSidebarWidth, final @Nullable Float rightSidebarWidth, final int initialBgColor) {
        super(initialBgColor);
        finalizeInit(leftSidebarWidth, rightSidebarWidth);
    }
    protected UiSidebarScreen(final @Nullable Float leftSidebarWidth, final @Nullable Float rightSidebarWidth) {
        super();
        finalizeInit(leftSidebarWidth, rightSidebarWidth);
    }


    private void finalizeInit(final @Nullable Float leftSidebarWidth, final @Nullable Float rightSidebarWidth) {
        this.hasLeftSidebar  =  leftSidebarWidth != null;
        this.hasRightSidebar = rightSidebarWidth != null;
        this.leftSidebarWidth  =  hasLeftSidebar ? leftSidebarWidth  : 0;
        this.rightSidebarWidth = hasRightSidebar ? rightSidebarWidth : 0;
    }




    // Initializer function
    @Override
    protected void init() {
        super.init();

        // Add left sidebar
        if(hasLeftSidebar) {
            leftSidebar = new UiWidgetList(this, Layout.BUTTON_HEIGHT);
            leftSidebar.setBgColor(DEFAULT_SIDEBAR_COLOR);
            leftSidebar.setBorderRightPx(1);
            leftSidebar.setShadowRightPx(Layout.shadowSizePx);
            addRenderableWidget(leftSidebar);
        }

        // Add right sidebar
        if(hasRightSidebar) {
            rightSidebar = new UiWidgetList(this, Layout.BUTTON_HEIGHT);
            rightSidebar.setBgColor(DEFAULT_SIDEBAR_COLOR);
            rightSidebar.setBorderLeftPx(1);
            rightSidebar.setShadowLeftPx(Layout.shadowSizePx);
            addRenderableWidget(rightSidebar);
        }
    }


    // Layout logic
    @Override
    public void relayoutSelf() {
        if(hasLeftSidebar) {
            final float leftSidebarWidthPx = width * leftSidebarWidth;
            leftSidebar.setSize( leftSidebarWidthPx, height);
            leftSidebar.setPosition(0, 0);
        }
        if(hasRightSidebar) {
            final float rightSidebarWidthPx = width * rightSidebarWidth;
            rightSidebar.setSize(rightSidebarWidthPx, height);
            rightSidebar.setPos(width - rightSidebarWidthPx, 0);
        }
    }


    // Only blur behind sidebars
    @Override
    public void extractBlurredBackground(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        final float radius = animatedBgBlurRadius.compute();
        if(radius > 0) {
            if(hasLeftSidebar) {
                final float leftSidebarWidthPx = width * leftSidebarWidth;
                graphics.gaussianBlur(0, 0, leftSidebarWidthPx, height, radius);
            }
            if(hasRightSidebar) {
                final float rightSidebarWidthPx = width * rightSidebarWidth;
                graphics.gaussianBlur(width, 0, width - rightSidebarWidthPx, height, radius);
            }
        }
    }
}
