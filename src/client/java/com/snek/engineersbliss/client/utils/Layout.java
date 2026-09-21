package com.snek.engineersbliss.client.utils;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.resources.Identifier;




public class Layout {
    private Layout() {}
    public static final Identifier PLACEHOLDER_TEXTURE_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "gui/placeholder_texture");


    public static int fgColor                = 0xFFEEEEEE;
    public static int fgColorHint            = 0xAABBBBBB;
    public static int highlightOverlay       = 0x40FFFFFF;

    public static int bgColor                = 0x80151515;
    public static int bgColorAlt             = 0xAA886A99;
    public static int screenBgColor          = bgColor | 0xFF000000; //! Multiplied by the background opacity setting

    public static int handleColor            = 0x80999999;
    public static int handleColorActive      = 0x80DDDDDD;
    public static int handleColorTransparent = 0x10999999;

    public static int borderColor            = 0xFF2E2C2C;
    public static int shadowColor            = 0x22000000;
    public static int shadowSizePx           = 16;

    public static float disabledAlpha = 0.25f;

    public static int textMarginPx = 4;
    public static int textLargeMarginPx = 16;

    public static int SliderGraphFillColor = bgColorAlt;
    public static int SliderGraphLineColor = fgColorHint;




    public static int statusBarBgColor = 0xFF1E1E1E;




    public static int toggleTransitionDuration = 250;
    public static int hoverTransitionDuration  = 100;
    public static int slideTransitionDuration  = 120;
    public static int guiScaleTransitionDuration  = 250;




    public static final int BORDER_HEIGHT = 2;
    public static final int BUTTON_HEIGHT = 16;

    public static final int   SEPARATOR_HEIGHT = 2;
    public static final int   BIG_SEPARATOR_HEIGHT = 16;
    public static final int   HEADER_HEIGHT = 24;
    public static final float HEADER_TEXT_SCALE = 1.5f;
}
