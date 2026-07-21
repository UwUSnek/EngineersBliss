package com.snek.engineersbliss.client.ui.font;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.mixin.accessors.FontAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.glyphs.EffectGlyph;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;








public class Fonts {
    private static final int   FONT_MAX_SIZE  = 10;                                                 // Maximum available font size
    private static final float FONT_SCALE_STEP = 0.25f;                                             // Increment between adjacent font sizes
    private static final float FONT_UNIT_RATIO = 1f / FONT_SCALE_STEP;                              // The inverse of the step
    private static final int   FONT_SIZES_NUMBER = Math.round(FONT_MAX_SIZE * FONT_UNIT_RATIO);     // The number of available sizes for a font
    private Fonts() {}




    // All ScaledFont instances by name. One instance for each scale.
    //! Scale advanced by FONT_SCALE_STEP each index.
    private static            @Nullable ScaledFont      defaultFont = null;
    private static final List<@Nullable ScaledFont> monoMediumFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable ScaledFont>    uiLightFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable ScaledFont>  uiRegularFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable ScaledFont>     uiBoldFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));




    /**
     * The default font.
     * This usually maps to the pixellated Minecraft font, but resourcepacks can override it.
     */
    public static final class _default {
        private _default() {}
        public static FontFamily medium = (final float scaleMultiplier) -> {
            if(defaultFont == null) {
                defaultFont = new ScaledFont();
            }
            return defaultFont;
        };
    }

    /**
     * A monospace font. All characters have the same width.
     */
    public static final class mono {
        private mono() {}
        public static FontFamily medium = (final float scaleMultiplier) -> { return createFontIfNeeded(monoMediumFonts, "mono_medium", scaleMultiplier); };
    }

    /**
     * The default font for Engineer's Bliss UIs. Not monospace.
     */
    public static final class ui {
        private ui() {}
        public static FontFamily   light = (final float scaleMultiplier) -> { return createFontIfNeeded(uiLightFonts,   "ui_light",   scaleMultiplier); };
        public static FontFamily regular = (final float scaleMultiplier) -> { return createFontIfNeeded(uiRegularFonts, "ui_regular", scaleMultiplier); };
        public static FontFamily    bold = (final float scaleMultiplier) -> { return createFontIfNeeded(uiBoldFonts,    "ui_bold",    scaleMultiplier); };
    }








    /**
     * Tries to fetch the correct font based on the provided name and scale multiplier and the current GUI Scale option.
     * Creates the font instance if it's not already available.
     * @param fontList The list to fetch the font instance from.
     * @param fontName The name of the font (used to create the instance).
     * @param scaleMultiplier The scale multiplier.
     * @return The existing or newly created ScaledFont instance.
     */
    private static final ScaledFont createFontIfNeeded(final List<@Nullable ScaledFont> fontList, final String fontName, final float scaleMultiplier) {
        final float guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        final int fontIndex = getFontIndexForScale(scaleMultiplier, guiScale);
        final @Nullable ScaledFont requestedFont = fontList.get(fontIndex);
        if(requestedFont != null) {
            return requestedFont;
        }
        else {

            // Fetch default provider from Minecraft and create a custom font description
            final Font.Provider defaultProvider = ((FontAccessor)Minecraft.getInstance().font).getProvider();
            final FontDescription fontDescription = new FontDescription.Resource(getFontIdForScale(fontName, scaleMultiplier, guiScale));

            // Create the custom font provider.
            //! This returns custom glyphs but default effect.
            Font.Provider provider = new Font.Provider() {
                @Override public GlyphSource glyphs(final FontDescription font) {
                    return defaultProvider.glyphs(fontDescription);
                }
                @Override public EffectGlyph effect() {
                    return defaultProvider.effect();
                }
            };

            // Create the new ScaledFont instance and update the list, then return it
            final ScaledFont r = new ScaledFont(provider, 0f, guiScale * scaleMultiplier, fontDescription);
            fontList.set(fontIndex, r);
            return r;
        }
    }




    /**
     * Calculates the index of the font instance in its containing list based on the current GUI Scale option and the provided scale multiplier.
     * @param scaleMultiplier The scale multiplier.
     * @param guiScale The current GUI Scale option value.
     * @return The index of the optimal font instance.
     */
    private static int getFontIndexForScale(final float scaleMultiplier, final float guiScale) {

        // Snap to nearest 0.25 increment, clamped between 0.25 and FONT_MAX_SIZE, then convert to index
        return Math.clamp(Math.round(guiScale * scaleMultiplier * FONT_UNIT_RATIO), 1, FONT_SIZES_NUMBER) - 1;
    }




    /**
     * Fetches the ID of the font provider of the specified font that is most optimal for rendering text of the specified size.
     * This takes into account the current GUI Scale option.
     * ! Available providers are the ones bundled with the mod. Specifying a non-existent font will cause the client to crash.
     * @param baseName The name of the font to fetch. This doesn't include the scale or the file extension.
     * @param scaleMultiplier The scale factor. This should match the size of the text you intend to display relative to the default size (scale 1).
     *              This is clamped between 0.25 and 10 and rounded to the nearest multiple of 0.25 units.
     *              For pixel-perfect rendering, ensure the text size is a multiple of 0.25 units (4px minecraft text height)
     * @param guiScale The current GUI Scale option value.
     * @return The ID of the font provider.
     */
    private static Identifier getFontIdForScale(final String baseName, final float scaleMultiplier, final float guiScale) {

        // Snap to nearest 0.25 increment, clamped between 0.25 and FONT_MAX_SIZE
        float snapped = Math.round(guiScale * scaleMultiplier * FONT_UNIT_RATIO) / FONT_UNIT_RATIO;
        snapped = Math.clamp(snapped, FONT_SCALE_STEP, FONT_MAX_SIZE);

        // Format as "1" for whole numbers, "1.5" for decimal steps, to matches atlas filenames
        final String scaleStr = (snapped == Math.floor(snapped))
            ? String.valueOf((int)snapped)
            : String.valueOf(snapped)
        ;

        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("%s_%sx", baseName, scaleStr));
    }
}




//TODO force large VBO on startup