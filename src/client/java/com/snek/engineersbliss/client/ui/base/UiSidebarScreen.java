package com.snek.engineersbliss.client.ui.base;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;








/**
 * A UiScreen that comes with sidebars.
 */
public abstract class UiSidebarScreen extends UiScreen {
    public static float DEFAULT_SIDEBAR_WIDTH = 0.2f;

    // Elements and layout
    protected final boolean hasLeftSidebar;
    protected final boolean hasRightSidebar;
    protected final float leftSidebarWidth;  //TODO replace with UiSiz
    protected final float rightSidebarWidth; //TODO replace with UiSize
    protected UiWidgetList leftSidebar;
    protected UiWidgetList rightSidebar;




    /**
     * Creates a screen with left and right sidebars of default width.
     */
    protected UiSidebarScreen() {
        this(DEFAULT_SIDEBAR_WIDTH, DEFAULT_SIDEBAR_WIDTH);
    }


    /**
     * Creates a screen with left and right sidebar of the specified width.
     * @param leftSidebarWidth The width of the left sidebar. Can be null to disble the left sidebar.
     * @param rightSidebarWidth The width of the right sidebar. Can be null to disble the right sidebar.
     */
    protected UiSidebarScreen(final @Nullable Float leftSidebarWidth, final @Nullable Float rightSidebarWidth) {
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
            leftSidebar = new UiWidgetList(this, Layout.BUTTON_HEIGHT);
            leftSidebar.setBorderRightPx(1);
            addRenderableWidget(leftSidebar);
        }

        // Add right sidebar
        if(hasRightSidebar) {
            rightSidebar = new UiWidgetList(this, Layout.BUTTON_HEIGHT);
            rightSidebar.setBorderLeftPx(1);
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
}
