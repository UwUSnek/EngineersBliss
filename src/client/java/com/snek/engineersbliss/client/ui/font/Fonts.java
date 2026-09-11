package com.snek.engineersbliss.client.ui.font;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.mixin.accessors.FontAccessor;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;
import com.snek.engineersbliss.utils.data_types.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.glyphs.EffectGlyph;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;








public class Fonts {
    private static final float FONT_MAX_SIZE   = 10;                                                // Maximum available font size
    private static final float FONT_SIZE_STEP = 0.25f;                                              // Increment between adjacent font sizes
    private static final float FONT_UNIT_RATIO = 1f / FONT_SIZE_STEP;                               // The inverse of the step
    private static final int   FONT_SIZES_NUMBER = Math.round(FONT_MAX_SIZE * FONT_UNIT_RATIO);     // The number of available sizes for a font
    private Fonts() {}




    // All Font instances by name. One instance for each scale.
    //! Scale advanced by FONT_SIZE_STEP each index.

    private static       List<@Nullable Pair<Font, FontDescription>>       defaultFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable Pair<Font, FontDescription>>   monoRegularFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));

    private static final List<@Nullable Pair<Font, FontDescription>>   smoothLightFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable Pair<Font, FontDescription>> smoothRegularFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable Pair<Font, FontDescription>>    smoothBoldFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));

    private static final List<@Nullable Pair<Font, FontDescription>>       uiLightFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable Pair<Font, FontDescription>>     uiRegularFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable Pair<Font, FontDescription>>        uiBoldFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));

    private static final List<@Nullable Pair<Font, FontDescription>>     codeLightFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable Pair<Font, FontDescription>>   codeRegularFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));
    private static final List<@Nullable Pair<Font, FontDescription>>      codeBoldFonts = new ArrayList<>(Collections.nCopies(FONT_SIZES_NUMBER, null));





    /** The default font. This usually maps to the pixellated Minecraft font, but resourcepacks can override it. */
    public static final class _default {
        private _default() {}
        public static FontFamily           medium = (final float sizeMultiplier) -> createScaledFont(defaultFonts, null, sizeMultiplier, false);
        public static FontFamily invariant_medium = (final float sizeMultiplier) -> createScaledFont(defaultFonts, null, sizeMultiplier, true);
    }

    /** A monospace font. All characters have the same width. */
    public static final class mono {
        private mono() {}
        public static FontFamily           regular = (final float sizeMultiplier) -> createScaledFont(monoRegularFonts, "mono_regular", sizeMultiplier, false);
        public static FontFamily invariant_regular = (final float sizeMultiplier) -> createScaledFont(monoRegularFonts, "mono_regular", sizeMultiplier, true);
    }

    /** A smoother, more symmetrical font than the default UI font. */
    public static final class smooth {
        private smooth() {}
        public static FontFamily             light = (final float sizeMultiplier) -> createScaledFont(smoothLightFonts,   "smooth_light",   sizeMultiplier, false);
        public static FontFamily           regular = (final float sizeMultiplier) -> createScaledFont(smoothRegularFonts, "smooth_regular", sizeMultiplier, false);
        public static FontFamily              bold = (final float sizeMultiplier) -> createScaledFont(smoothBoldFonts,    "smooth_bold",    sizeMultiplier, false);
        public static FontFamily   invariant_light = (final float sizeMultiplier) -> createScaledFont(smoothLightFonts,   "smooth_light",   sizeMultiplier, true);
        public static FontFamily invariant_regular = (final float sizeMultiplier) -> createScaledFont(smoothRegularFonts, "smooth_regular", sizeMultiplier, true);
        public static FontFamily    invariant_bold = (final float sizeMultiplier) -> createScaledFont(smoothBoldFonts,    "smooth_bold",    sizeMultiplier, true);
    }

    /** The default font for Engineer's Bliss UIs. Not monospace. */
    public static final class ui {
        private ui() {}
        public static FontFamily             light = (final float sizeMultiplier) -> createScaledFont(uiLightFonts,   "ui_light",   sizeMultiplier, false);
        public static FontFamily           regular = (final float sizeMultiplier) -> createScaledFont(uiRegularFonts, "ui_regular", sizeMultiplier, false);
        public static FontFamily              bold = (final float sizeMultiplier) -> createScaledFont(uiBoldFonts,    "ui_bold",    sizeMultiplier, false);
        public static FontFamily   invariant_light = (final float sizeMultiplier) -> createScaledFont(uiLightFonts,   "ui_light",   sizeMultiplier, true);
        public static FontFamily invariant_regular = (final float sizeMultiplier) -> createScaledFont(uiRegularFonts, "ui_regular", sizeMultiplier, true);
        public static FontFamily    invariant_bold = (final float sizeMultiplier) -> createScaledFont(uiBoldFonts,    "ui_bold",    sizeMultiplier, true);
    }

    /** A monospace font meant for code snippets. */
    public static final class code {
        private code() {}
        public static FontFamily             light = (final float sizeMultiplier) -> createScaledFont(codeLightFonts,   "code_light",   sizeMultiplier, false);
        public static FontFamily           regular = (final float sizeMultiplier) -> createScaledFont(codeRegularFonts, "code_regular", sizeMultiplier, false);
        public static FontFamily              bold = (final float sizeMultiplier) -> createScaledFont(codeBoldFonts,    "code_bold",    sizeMultiplier, false);
        public static FontFamily   invariant_light = (final float sizeMultiplier) -> createScaledFont(codeLightFonts,   "code_light",   sizeMultiplier, true);
        public static FontFamily invariant_regular = (final float sizeMultiplier) -> createScaledFont(codeRegularFonts, "code_regular", sizeMultiplier, true);
        public static FontFamily    invariant_bold = (final float sizeMultiplier) -> createScaledFont(codeBoldFonts,    "code_bold",    sizeMultiplier, true);
    }








    private static ScaledFont createScaledFont(final List<@Nullable Pair<Font, FontDescription>> fontList, final @Nullable String fontName, final float sizeMultiplier, final boolean scaleInvariant) {
        final int currentScaleIndex = scaleInvariant ? 0 : SettingsFeatureHandler.getCurrentGuiScaleIndex();
        final @NotNull Pair<Font, FontDescription> font = createFontIfNeeded(fontList, fontName, sizeMultiplier, currentScaleIndex);
        return new ScaledFont(null, font.getFirst(), sizeMultiplier, scaleInvariant, font.getSecond());
    }



    /**
     * Tries to fetch the correct font based on the provided name and size multiplier and the current GUI Scale option.
     * Creates the font instance if it's not already available.
     * @param fontList The list to fetch the font instance from.
     * @param fontName The name of the font (used to create the instance). Uses the default Minecraft font if null.
     * @param sizeMultiplier The size multiplier.
     * @return The existing or newly created Font instance and its FontDescription.
     */
    private static Pair<Font, FontDescription> createFontIfNeeded(final List<@Nullable Pair<Font, FontDescription>> fontList, final @Nullable String fontName, final float sizeMultiplier, final int guiScaleIndex) {
        final int fontIndex = getFontIndexForScale(sizeMultiplier, guiScaleIndex);
        final @Nullable Pair<Font, FontDescription> requestedFont = fontList.get(fontIndex);
        if(requestedFont != null) {
            return requestedFont;
        }
        else {

            // Fetch default provider from Minecraft and create a custom font description
            final @NotNull Font.Provider defaultProvider = ((FontAccessor)Minecraft.getInstance().font).getProvider();
            final @NotNull FontDescription fontDescription = fontName == null
                ? Style.EMPTY.getFont()
                : new FontDescription.Resource(getFontIdForScale(fontName, sizeMultiplier, guiScaleIndex))
            ;

            // Create the custom font provider.
            //! This returns custom glyphs but default effect.
            final @NotNull Font.Provider provider = new Font.Provider() {
                @Override public GlyphSource glyphs(final FontDescription font) {
                    return defaultProvider.glyphs(fontDescription);
                }
                @Override public EffectGlyph effect() {
                    return defaultProvider.effect();
                }
            };

            // Create the new Font instance and update the list, then return it
            final @NotNull Pair<Font, FontDescription> r = Pair.from(new Font(provider), fontDescription);
            fontList.set(fontIndex, r);
            return r;
        }
    }




    /**
     * Calculates the index of the font instance in its containing list based on the current GUI Scale option and the provided scale multiplier.
     * @param sizeMultiplier The size multiplier.
     * @param guiScale The current GUI Scale option value.
     * @return The index of the optimal font instance.
     */
    private static int getFontIndexForScale(final float sizeMultiplier, final int guiScaleIndex) {

        // Snap to nearest 0.25 increment, clamped between 0.25 and FONT_MAX_SIZE, then convert to index
        final float guiScale = SettingsServerFeatureSet.GUI_SCALE.getValues().get(guiScaleIndex);
        return Math.clamp(Math.round(guiScale * sizeMultiplier * FONT_UNIT_RATIO), 1, FONT_SIZES_NUMBER) - 1;
    }




    /**
     * Fetches the ID of the font provider of the specified font that is most optimal for rendering text of the specified size.
     * This takes into account the current GUI Scale option.
     * ! Available providers are the ones bundled with the mod. Specifying a non-existent font will cause the client to crash.
     * @param baseName The name of the font to fetch. This doesn't include the size or the file extension.
     * @param sizeMultiplier The size factor. This should match the size of the text you intend to display relative to the default size (size 1).
     *              This is clamped between 0.25 and 10 and rounded to the nearest multiple of 0.25 units.
     *              For pixel-perfect rendering, ensure the text size is a multiple of 0.25 units (4px minecraft text height)
     * @param guiScale The current GUI Scale option value.
     * @return The ID of the font provider.
     */
    private static Identifier getFontIdForScale(final String baseName, final float sizeMultiplier, final int guiScaleIndex) {

        // Snap to nearest 0.25 increment, clamped between 0.25 and FONT_MAX_SIZE
        final float guiScale = SettingsServerFeatureSet.GUI_SCALE.getValues().get(guiScaleIndex);
        final float snapped = Math.round(guiScale * sizeMultiplier * FONT_UNIT_RATIO) / FONT_UNIT_RATIO;
        final float clamped = Math.clamp(snapped, FONT_SIZE_STEP, FONT_MAX_SIZE);

        // Format as "1" for whole numbers, "1.5" for decimal steps, to matches atlas filenames
        final String sizeStr = (clamped == Math.floor(clamped))
            ? String.valueOf((int)clamped)
            : String.valueOf(clamped)
        ;

        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("%s_%sx", baseName, sizeStr));
    }
}




//TODO force large VBO on startup