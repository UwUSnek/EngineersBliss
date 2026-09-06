package com.snek.engineersbliss.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.ui.font.ScaledFont;








public class RenderingUtils {
    private RenderingUtils() {}




    /**
     * Wraps the provided UiTxt so each line never goes past the width limit.
     * @param text The text to wrap.
     * @param maxWidth The maximum width of a line.
     * @return A list of UiTxt, each containing the formatted characters in a line.
     */
    public static List<UiTxt> wrapLines(final UiTxt text, final int maxWidth) {


        // Create line list and calculate data
        final @NotNull ScaledFont scaledFont = text.getScaledFont();
        final @NotNull List<UiTxt> lines = new ArrayList<>();
        final @NotNull String raw = text.getString();
        final int len = raw.length();
        int lineStart = 0;
        int lastSpace = -1;


        // Split lines
        for(int i = 0; i < len; i++) {
            final char c = raw.charAt(i);

            if(c == '\n') {
                lines.add((UiTxt)text.substring(lineStart, i));
                lineStart = i + 1;
                lastSpace = -1;
                continue;
            }

            if(c == ' ') {
                lastSpace = i;
            }

            if(scaledFont.calcWidth(raw.substring(lineStart, i + 1)) > maxWidth) { //TODO this is prob inefficient
                if(lastSpace >= lineStart) {
                    lines.add((UiTxt)text.substring(lineStart, lastSpace));
                    lineStart = lastSpace + 1;
                }
                else {
                    lines.add((UiTxt)text.substring(lineStart, i));
                    lineStart = i;
                }
                lastSpace = -1;
            }
        }

        if(lineStart < len) {
            lines.add((UiTxt)text.substring(lineStart, len));
        }


        return lines;
    }
}
