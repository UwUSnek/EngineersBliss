package com.snek.engineersbliss.client.ui.base;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.TextAlignmentY;
import com.snek.engineersbliss.client.ui.widgets.base.FeatureInputWidget;
import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;








/**
 * A __base_UiSidebarScreen for a feature set.
 * It provides an interface for creating and loading presets, keeping the central part of the screen empty.
 */
public class __base_UiFeatureSetScreen extends __base_UiSidebarScreen {

    // Hover data
    public static final long HOVER_OFF_DELAY_MS = 250;
    private long lastHoverTime = 0;
    protected FeatureInputWidget lastHoveredFeatureWidget = null;

    //! "is (preview OFF) on cooldown?"
    protected boolean isPreviewOffOnCooldown() {
        final long now = System.currentTimeMillis();
        return now - lastHoverTime < HOVER_OFF_DELAY_MS;
    }



    // Parent feature set and constructor
    protected final __base_ClientFeatureSet<?> featureSet;
    protected __base_UiFeatureSetScreen(final __base_ClientFeatureSet<?> featureSet) {
        super();
        this.featureSet = featureSet;
    }








    // Initializer function
    @Override
    protected void init() {
        super.init();

        // Add left sidebar title
        final UiTxt titleText = new UiTxt(featureSet.calcName().get(), 2f);
        final int titleHeight = titleText.getScaledFont().getLineHeight();
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, titleText, TextAlignment.LEFT, Layout.fgColor), titleHeight);
    }




    @Override
    public void _extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        updateHoveredFeatureReference(graphics, mouseX, mouseY, delta);
        super._extractRenderState(graphics, mouseX, mouseY, delta);
    }




    private void updateHoveredFeatureReference(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {

        // Update hovered feature entry data
        final @Nullable UiWidgetList.Entry entry = (UiWidgetList.Entry)leftSidebar.getHoveredChild();
        final @Nullable AbstractWidget widget = entry != null ? entry.getWidget() : null;
        if(widget != null && widget instanceof FeatureInputWidget featureWidget) {
            lastHoverTime = System.currentTimeMillis();
            if(featureWidget != lastHoveredFeatureWidget) {
                lastHoveredFeatureWidget = featureWidget;
                onFeatureHoverChange(lastHoveredFeatureWidget);
            }
        }

        // Stop the preview from disappearing when dragging sliders
        else if(isDragging()) {
            lastHoverTime = System.currentTimeMillis();
        }

        // Clear preview if not dragging, not hovering, and the cooldown has expired
        else if(!isPreviewOffOnCooldown()) {
            lastHoveredFeatureWidget = null;
            onFeatureHoverChange(lastHoveredFeatureWidget);
        }
    }




    protected void onFeatureHoverChange(final @Nullable FeatureInputWidget newWidget) {

        // Clear right sidebar
        rightSidebar.clearEntries();

        // Show profile UI if no new widget is being hovered
        if(newWidget == null) {
            rightSidebar.setIsScrollable(true); //! Re-enable scrolling in case the feature info branch disabled it.
            //TODO
        }

        // Show feature info UI otherwise
        else {

            // Feature name
            final UiTxt nameText = new UiTxt(newWidget.getClientFeature().calcName().get(), 2f);
            final int nameHeight = nameText.getScaledFont().getLineHeight();
            rightSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
            rightSidebar.addWidget(new UiTextWidget(this, nameText, TextAlignment.CENTER, Layout.fgColor), nameHeight);

            // Feature description
            rightSidebar.setIsScrollable(false); //! Disable scrolling so the element doesn't show a scroll bar. There is always enough space for the description.
            final UiTxt descriptionText = newWidget.getClientFeature().calcDesc();
            final int descriptionHeight = height - nameHeight - Layout.BIG_SEPARATOR_HEIGHT; //! Might not be pixel perfect but it doesn't matter, can't scroll the element anyway.
            final UiTextWidget descriptionWidget = new UiTextWidget(this, descriptionText, TextAlignment.CENTER, true, Layout.fgColor);
            descriptionWidget.setVerticalAlignment(TextAlignmentY.TOP);
            rightSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
            rightSidebar.addWidget(descriptionWidget, descriptionHeight);
        }
    }
}
