package com.snek.engineersbliss.client.ui.base;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;








/**
 * A __base_UiScreen that comes with sidebars.
 */
public abstract class __base_UiSidebarScreen extends __base_UiScreen {
    public static float DEFAULT_SIDEBAR_WIDTH = 0.25f;

    // Elements and layout
    protected final boolean hasLeftSidebar;
    protected final boolean hasRightSidebar;
    protected final float leftSidebarWidth;
    protected final float rightSidebarWidth;
    protected UiWidgetList leftSidebar;
    protected UiWidgetList rightSidebar;




    /**
     * Creates a screen with left and right sidebars of default width.
     */
    protected __base_UiSidebarScreen() {
        this(DEFAULT_SIDEBAR_WIDTH, DEFAULT_SIDEBAR_WIDTH);
    }


    /**
     * Creates a screen with left and right sidebar of the specified width.
     * @param leftSidebarWidth The width of the left sidebar. Can be null to disble the left sidebar.
     * @param rightSidebarWidth The width of the right sidebar. Can be null to disble the right sidebar.
     */
    protected __base_UiSidebarScreen(final @Nullable Float leftSidebarWidth, final @Nullable Float rightSidebarWidth) {
        super();
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
            leftSidebar = new UiWidgetList(this, 0, 0, 0, 0, BUTTON_HEIGHT);
            addRenderableWidget(leftSidebar);
        }

        // Add right sidebar
        if(hasRightSidebar) {
            rightSidebar = new UiWidgetList(this, 0, 0, 0, 0, BUTTON_HEIGHT);
            addRenderableWidget(rightSidebar);
        }
    }


    // Layout logic
    @Override
    public void layoutWidgets() {
        if(hasLeftSidebar) {
            final int leftSidebarWidthPx = (int)(width * leftSidebarWidth);
            leftSidebar.setSize( leftSidebarWidthPx, height);
            leftSidebar.setPosition(0, 0);
        }
        if(hasRightSidebar) {
            final int rightSidebarWidthPx = (int)(width * rightSidebarWidth);
            rightSidebar.setSize(rightSidebarWidthPx, height);
            rightSidebar.setPosition(width - rightSidebarWidthPx, 0);
        }
        super.layoutWidgets();
    }
}
