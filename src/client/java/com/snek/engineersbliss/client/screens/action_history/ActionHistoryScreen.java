package com.snek.engineersbliss.client.screens.action_history;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiButton;
import com.snek.engineersbliss.client.screens.parts.UiSpacer;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;




public class ActionHistoryScreen extends __base_Screen {
    private static UiWidgetList leftSidebar;
    private static final float LEFT_SIDEBAR_WIDTH = 0.25f;


    public ActionHistoryScreen() {
        super();
    }




    @Override
    protected void init() {


        leftSidebar = new UiWidgetList((int)(width * LEFT_SIDEBAR_WIDTH), height, 0, 0, BUTTON_HEIGHT); {
            final String titleString = "Alternative Textures";
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            //! test //TODO remove
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("test //TODO remove", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidget(createButton(new UiTxt("undo"), new UiTxt(""), b -> {}, '\0', null));
            leftSidebar.addWidget(createButton(new UiTxt("redo"), new UiTxt(""), b -> {}, '\0', null));
        }
        addRenderableWidget(leftSidebar);
    }
}