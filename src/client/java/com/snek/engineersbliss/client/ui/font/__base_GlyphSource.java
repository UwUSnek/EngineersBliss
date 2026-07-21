package com.snek.engineersbliss.client.ui.font;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.font.GlyphInfo;

import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used
//TODO remove if not used



public class __base_GlyphSource implements GlyphSource {
    private final Map<Integer, BakedGlyph> glyphs = new HashMap<>();
    private final BakedGlyph fallback;

    public __base_GlyphSource(final Map<Integer, Float> advances, final float fallbackAdvance) {
        for(final Map.Entry<Integer, Float> entry : advances.entrySet()) {
            glyphs.put(entry.getKey(), bakedGlyphOf(entry.getValue()));
        }
        this.fallback = bakedGlyphOf(fallbackAdvance);
    }


    private static BakedGlyph bakedGlyphOf(final float advance) {
        final GlyphInfo info = GlyphInfo.simple(advance);
        return new BakedGlyph() {
            @Override
            public GlyphInfo info() {
                return info;
            }

            //! Not actually used by Minecraft but the interface requires an override
            @Override
            public TextRenderable.Styled createGlyph(final float x, final float y, final int color, final int shadowColor, final Style style, final float boldOffset, final float shadowOffset) {
                return null;
            }
        };
    }


    @Override
    public BakedGlyph getGlyph(final int codepoint) {
        return glyphs.getOrDefault(codepoint, fallback);
    }

    @Override
    public BakedGlyph getRandomGlyph(final RandomSource random, final int width) {
        return getGlyph(random.nextInt() % glyphs.size());
        //FIXME implement this in a better way instead of creating random characters of random width
    }
}