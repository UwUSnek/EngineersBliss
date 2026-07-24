package com.snek.engineersbliss.client.ui.base;

import com.snek.engineersbliss.client.ui.widgets.UiWidgetList;








/**
 * A __base_UiScreen that comes with sidebars.
 */
public abstract class __base_UiSidebarScreen extends __base_UiScreen {

    // Elements and layout
    private final boolean hasLeftSidebar;
    private final boolean hasRightSidebar;
    protected static UiWidgetList leftSidebar;
    protected static UiWidgetList rightSidebar;
    public static final float LEFT_SIDEBAR_WIDTH = 0.25f;
    public static final float RIGHT_SIDEBAR_WIDTH = 0.25f;




    protected __base_UiSidebarScreen(final boolean hasLeftSidebar, final boolean hasRightSidebar) {
        super();
        this.hasLeftSidebar  = hasLeftSidebar;
        this.hasRightSidebar = hasRightSidebar;
    }




    // Initializer function
    @Override
    protected void init() {
        super.init();

        // Add left sidebar
        if(hasLeftSidebar) {
            final int leftSidebarWidth = (int)(width * LEFT_SIDEBAR_WIDTH);
            leftSidebar = new UiWidgetList(leftSidebarWidth, height, 0, 0, BUTTON_HEIGHT);
            addRenderableWidget(leftSidebar);
        }

        // Add right sidebar
        if(hasRightSidebar) {
            final int rightSidebarWidth = (int)(width * RIGHT_SIDEBAR_WIDTH);
            rightSidebar = new UiWidgetList(rightSidebarWidth, height, width - rightSidebarWidth, 0, BUTTON_HEIGHT);
            addRenderableWidget(rightSidebar);
        }
    }
}
