package com.snek.engineersbliss.client.ui.widgets.base;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedInt;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.TextCursorUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Util;
















public abstract class __base_UiTextHandlerWidget extends __base_UiWidget {
    public static final long CURSOR_BLINK_START_MS = 500;

    private final FontFamily fontFamily;
    private final ScaledFont font;
    private final UiTxt hint;
    protected final List<StringBuilder> lines;
    private final List<UiTxt> renderLines;
    protected int totalLength;
    protected int maxLength;
    protected final boolean multiline;

    protected int cursorLine;
    protected int cursorCol;
    protected AnimatedInt visualCursorPosPx;
    protected AnimatedInt visualCursorLine;
    protected int highlightLine;
    protected int highlightCol;

    protected int scrollPx; // horizontal scroll offset, in pixels
    protected AnimatedInt visualScrollPx;
    protected int scrollLinePx; // vertical scroll offset, in pixels
    protected AnimatedInt visualScrollLinePx;

    protected boolean editable;
    protected long focusedTime;
    protected long lastMoveTime;





    protected __base_UiTextHandlerWidget(final Screen screen, final FontFamily fontFamily, final UiTxt hint, final TextAlignment alignment, final boolean multiline) {
        super(screen, new UiTxt(), alignment);
        this.fontFamily         = fontFamily;
        this.font               = fontFamily.get(1f);
        this.hint               = hint;
        this.lines              = new ArrayList<>();
        this.lines.add(new StringBuilder());
        this.multiline    = multiline;
        this.totalLength        = 0;
        this.maxLength          = Integer.MAX_VALUE;
        this.cursorLine         = 0;
        this.cursorCol          = 0;
        this.visualCursorPosPx  = new AnimatedInt(0, 50);
        this.visualCursorLine   = new AnimatedInt(0, 50);
        this.highlightLine      = 0;
        this.highlightCol       = 0;
        this.scrollPx           = 0;
        this.visualScrollPx     = new AnimatedInt(0, 50);
        this.scrollLinePx       = 0;
        this.visualScrollLinePx = new AnimatedInt(0, 50);
        this.editable           = true;
        this.focusedTime        = Util.getMillis();
        this.lastMoveTime       = Util.getMillis();
        this.renderLines = new ArrayList<>();
    }

    @Override
    public void relayoutSelf() {
        // Empty
    }



    protected void updateLabel() {
        renderLines.clear();
        if(lines.size() == 1 && lines.get(0).isEmpty() && !isFocused() && hint != null) {
            renderLines.add(hint);
        }
        else {
            for(final StringBuilder line : lines) {
                renderLines.add(new UiTxt(line.toString(), fontFamily));
            }
            //FIXME this is rly inefficient. this should cache each line when it's changed, not recalculate everything every time anything changes
        }
        // setLabel(renderLines.get(0)); //TODO remove
        //! No setLabel call. This class handles text rendering on its own.
    }

    // @Override
    // public void relayoutSelf() {
    //     updateLabel();
    // }

    // protected abstract void updateLabel();


    // public String getValue() { //TODO remove
    //     if(lines.size() == 1) return lines.get(0).toString();
    //     final StringBuilder sb = new StringBuilder();
    //     for(int i = 0; i < lines.size(); i++) {
    //         if(i > 0) sb.append('\n');
    //         sb.append(lines.get(i));
    //     }
    //     return sb.toString();
    // }

    public void setValue(final String newValue) {
        lines.clear();
        for(final String line : newValue.split("\n", -1)) {
            lines.add(new StringBuilder(line));
        }
        totalLength = lines.size() - 1;
        for(final StringBuilder line : lines) {
            totalLength += line.length();
        }

        if(totalLength > maxLength) {
            final int[] cut = positionAtIndex(maxLength);
            lines.get(cut[0]).setLength(cut[1]);
            for(int l = lines.size() - 1; l > cut[0]; l--) {
                lines.remove(l);
            }
            totalLength = maxLength;
        }

        cursorLine = lines.size() - 1;
        cursorCol = lines.get(cursorLine).length();
        highlightLine = cursorLine;
        highlightCol = cursorCol;
        scrollPx = 0;
        onValueChange();
    }

    public void setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
        if(totalLength > maxLength) {
            final int[] cut = positionAtIndex(maxLength);
            lines.get(cut[0]).setLength(cut[1]);
            for(int l = lines.size() - 1; l > cut[0]; l--) lines.remove(l);
            totalLength = maxLength;

            if(comparePos(   cursorLine,    cursorCol, cut[0], cut[1]) > 0) setCursorPosition(cut[0], cut[1]);
            if(comparePos(highlightLine, highlightCol, cut[0], cut[1]) > 0)   setHighlightPos(cut[0], cut[1]);
            onValueChange();
        }
    }

    public void setEditable(final boolean editable) { this.editable = editable; }

    public String getHighlighted() {
        final boolean cursorFirst = comparePos(cursorLine, cursorCol, highlightLine, highlightCol) <= 0;
        final int startLine = cursorFirst ? cursorLine : highlightLine;
        final int startCol  = cursorFirst ? cursorCol  : highlightCol;
        final int endLine   = cursorFirst ? highlightLine : cursorLine;
        final int endCol    = cursorFirst ? highlightCol  : cursorCol;
        if(startLine == endLine) return lines.get(startLine).substring(startCol, endCol);  //TODO this is prob very inefficient

        final StringBuilder sb = new StringBuilder();
        sb.append(lines.get(startLine).substring(startCol));  //TODO this is prob very inefficient
        for(int l = startLine + 1; l < endLine; l++) sb.append('\n').append(lines.get(l));
        sb.append('\n').append(lines.get(endLine).substring(0, endCol));  //TODO this is prob very inefficient
        return sb.toString();
    }

    protected void onValueChange() {
        updateLabel();
    }


    private static int comparePos(final int lineA, final int colA, final int lineB, final int colB) {
        return lineA != lineB ? Integer.compare(lineA, lineB) : Integer.compare(colA, colB);
    }

    private int spanLength(final int startLine, final int startCol, final int endLine, final int endCol) {
        if(startLine == endLine) return endCol - startCol;
        int len = lines.get(startLine).length() - startCol;
        for(int l = startLine + 1; l < endLine; l++) len += lines.get(l).length() + 1;
        return len + 1 + endCol;
    }

    private void deleteSpan(final int startLine, final int startCol, final int endLine, final int endCol) {
        if(startLine == endLine && startCol == endCol) return;
        totalLength -= spanLength(startLine, startCol, endLine, endCol);

        final StringBuilder startSb = lines.get(startLine);
        final String tail = lines.get(endLine).substring(endCol);  //TODO this is prob very inefficient
        startSb.setLength(startCol);
        startSb.append(tail);
        for(int l = endLine; l > startLine; l--) lines.remove(l);

        setCursorPosition(startLine, startCol);
        setHighlightPos(startLine, startCol);
    }

    private int[] positionAtIndex(final int index) {
        int remaining = index;
        for(int l = 0; l < lines.size(); l++) {
            final int len = lines.get(l).length();
            if(remaining <= len) return new int[]{l, remaining};
            remaining -= len + 1;
        }
        final int last = lines.size() - 1;
        return new int[]{last, lines.get(last).length()};
    }



    public static boolean isValidCharacter(final int c) {
        return c >= 32 && c != 127;
    }

    public static String sanitizeText(final String input, final boolean allowMultiline) {
        StringBuilder builder = new StringBuilder();

        //! Exclude control characters and DEL
        for(char c : input.toCharArray()) {
            if(isValidCharacter(c) || (allowMultiline && c == '\n')) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public void insertText(final String input) {
        final boolean cursorFirst = comparePos(cursorLine, cursorCol, highlightLine, highlightCol) <= 0;
        final int startLine = cursorFirst ? cursorLine : highlightLine;
        final int startCol  = cursorFirst ? cursorCol  : highlightCol;
        final int endLine   = cursorFirst ? highlightLine : cursorLine;
        final int endCol    = cursorFirst ? highlightCol  : cursorCol;
        final int selectionLength = spanLength(startLine, startCol, endLine, endCol);

        int maxInsertionLength = maxLength - totalLength + selectionLength;
        if(maxInsertionLength <= 0) return;

        String text = sanitizeText(input, multiline);
        if(maxInsertionLength < text.length()) {
            if(Character.isHighSurrogate(text.charAt(maxInsertionLength - 1))) maxInsertionLength--;
            text = text.substring(0, maxInsertionLength);
        }

        if(selectionLength > 0) deleteSpan(startLine, startCol, endLine, endCol);

        final String[] parts = text.split("\n", -1); //TODO prob inefficient
        final StringBuilder line = lines.get(startLine);
        final String tail = line.substring(startCol);
        line.setLength(startCol);
        line.append(parts[0]);
        for(int i = 1; i < parts.length; i++) {
            lines.add(startLine + i, new StringBuilder(parts[i]));
        }
        final int lastLine = startLine + parts.length - 1;
        lines.get(lastLine).append(tail);

        totalLength += text.length();
        final int lastCol = parts.length == 1 ? startCol + parts[0].length() : parts[parts.length - 1].length();
        setCursorPosition(lastLine, lastCol);
        setHighlightPos(cursorLine, cursorCol);
        onValueChange();
    }

    protected void deleteCharsToPos(final int line, final int col) {
        if(cursorLine != highlightLine || cursorCol != highlightCol) insertText("");
        else {
            final boolean targetFirst = comparePos(line, col, cursorLine, cursorCol) <= 0;
            final int startLine = targetFirst ? line : cursorLine;
            final int startCol  = targetFirst ? col  : cursorCol;
            final int endLine   = targetFirst ? cursorLine : line;
            final int endCol    = targetFirst ? cursorCol  : col;
            if(startLine != endLine || startCol != endCol) {
                deleteSpan(startLine, startCol, endLine, endCol);
                onValueChange();
            }
        }
    }

    public void deleteChars(final int dir) {
        final int[] pos = offsetPosition(cursorLine, cursorCol, dir);
        deleteCharsToPos(pos[0], pos[1]);
    }

    public void deleteWords(final int dir) {
        if(cursorLine != highlightLine || cursorCol != highlightCol) insertText("");
        else {
            final int[] pos = getWordPosition(dir, cursorLine, cursorCol);
            deleteCharsToPos(pos[0], pos[1]);
        }
    }

    protected int[] getWordPosition(final int dir, final int fromLine, final int fromCol) {
        int l = fromLine, c = fromCol;
        final boolean reverse = dir < 0;
        final int abs = Math.abs(dir);
        for(int i = 0; i < abs; i++) {
            final String s = lines.get(l).toString(); //TODO this is prob very inefficient
            if(!reverse) {
                final int length = s.length();
                c = s.indexOf(' ', c);
                if(c == -1) c = length;
                else while(c < length && s.charAt(c) == ' ') c++;
            }
            else {
                while(c > 0 && s.charAt(c - 1) == ' ') c--;
                while(c > 0 && s.charAt(c - 1) != ' ') c--;
            }
        }
        return new int[]{l, c};
    }

    protected int[] offsetPosition(final int line, final int col, final int dir) {
        int l = line, c = col;
        final int steps = Math.abs(dir);
        final int stepDir = Integer.signum(dir);
        for(int i = 0; i < steps; i++) {
            final String s = lines.get(l).toString();  //TODO this is prob very inefficient
            if(stepDir > 0) {
                if(c < s.length()) c = Util.offsetByCodepoints(s, c, 1);
                else if(l < lines.size() - 1) { l++; c = 0; }
            }
            else {
                if(c > 0) c = Util.offsetByCodepoints(s, c, -1);
                else if(l > 0) { l--; c = lines.get(l).length(); }
            }
        }
        return new int[]{l, c};
    }


    public void moveCursor(final int dir, final boolean extendSelection) {
        final int[] pos = offsetPosition(cursorLine, cursorCol, dir);
        moveCursorTo(pos[0], pos[1], extendSelection);
    }

    public void moveCursorTo(final int line, final int col, final boolean extendSelection) {
        setCursorPosition(line, col);
        if(!extendSelection) setHighlightPos(cursorLine, cursorCol);
        updateLabel();
    }

    public void moveCursorToStart(final boolean extendSelection) {
        moveCursorTo(0, 0, extendSelection);
    }
    public void moveCursorToEnd(final boolean extendSelection) {
        moveCursorTo(lines.size() - 1, lines.get(lines.size() - 1).length(), extendSelection);
    }

    public void moveCursorToLineStart(final boolean extendSelection) {
        moveCursorTo(cursorLine, 0, extendSelection);
    }
    public void moveCursorToLineEnd(final boolean extendSelection) {
        moveCursorTo(cursorLine, lines.get(cursorLine).length(), extendSelection);
    }

    public boolean moveCursorVertical(final int dir, final boolean extendSelection) {
        final int targetLine = cursorLine + dir;
        if(targetLine < 0 || targetLine >= lines.size()) return false;
        final int columnPx = font.calcWidth(lines.get(cursorLine).substring(0, cursorCol));  //TODO this is prob very inefficient
        final int targetCol = font.getFont().plainSubstrByWidth(lines.get(targetLine).toString(), columnPx).length();  //TODO this is prob very inefficient
        moveCursorTo(targetLine, targetCol, extendSelection);
        return true;
    }

    protected void setCursorPosition(final int line, final int col) {
        cursorLine = Math.clamp(line, 0, lines.size() - 1);
        cursorCol = Math.clamp(col, 0, lines.get(cursorLine).length());
        lastMoveTime = Util.getMillis();
        visualCursorLine.startNewTransition(cursorLine);
        scrollTo(cursorLine, cursorCol);
    }

    protected void setHighlightPos(final int line, final int col) {
        highlightLine = Math.clamp(line, 0, lines.size() - 1);
        highlightCol = Math.clamp(col, 0, lines.get(highlightLine).length());
        scrollTo(highlightLine, highlightCol);
    }

    protected void scrollTo(final int line, final int col) {
        final int innerWidth = getInnerWidth();
        final int posX = font.calcWidth(lines.get(line).substring(0, col));  //TODO this is prob very inefficient

        if(posX - scrollPx > innerWidth) scrollPx = posX - innerWidth;
        if(posX - scrollPx < 0)          scrollPx = posX;

        final int maxScrollX = Math.max(0, font.calcWidth(lines.get(line).toString()) - innerWidth);  //TODO this is prob very inefficient
        visualScrollPx.startNewTransition(Math.clamp(scrollPx, 0, maxScrollX));

        final int lineHeight = font.getLineHeight();
        final int innerHeight = getBottom() - getY();
        final int posY = line * lineHeight;

        if(posY - scrollLinePx > innerHeight - lineHeight) scrollLinePx = posY - innerHeight + lineHeight;
        if(posY - scrollLinePx < 0)                         scrollLinePx = posY;

        final int maxScrollY = Math.max(0, lines.size() * lineHeight - innerHeight);
        visualScrollLinePx.startNewTransition(Math.clamp(scrollLinePx, 0, maxScrollY));
    }


    @Override
    public boolean keyPressed(final KeyEvent event) {
        if(!isActive() || !editable || !isFocused()) return false;
        boolean r;

        final boolean ctrl = event.hasControlDownWithQuirk();
        final boolean shift = event.hasShiftDown();
        switch(event.key()) {
            case GLFW.GLFW_KEY_BACKSPACE: {
                final int[] pos = ctrl ? getWordPosition(-1, cursorLine, cursorCol) : offsetPosition(cursorLine, cursorCol, -1);
                deleteCharsToPos(pos[0], pos[1]);
                r = true; break;
            }
            case GLFW.GLFW_KEY_DELETE: {
                final int[] pos = ctrl ? getWordPosition(1, cursorLine, cursorCol) : offsetPosition(cursorLine, cursorCol, 1);
                deleteCharsToPos(pos[0], pos[1]);
                r = true; break;
            }
            case GLFW.GLFW_KEY_RIGHT: {
                final int[] pos = ctrl ? getWordPosition(1, cursorLine, cursorCol) : offsetPosition(cursorLine, cursorCol, 1);
                moveCursorTo(pos[0], pos[1], shift);
                r = true; break;
            }
            case GLFW.GLFW_KEY_LEFT: {
                final int[] pos = ctrl ? getWordPosition(-1, cursorLine, cursorCol) : offsetPosition(cursorLine, cursorCol, -1);
                moveCursorTo(pos[0], pos[1], shift);
                r = true; break;
            }
            case GLFW.GLFW_KEY_UP: {
                r = moveCursorVertical(-1, shift);
                break;
            }
            case GLFW.GLFW_KEY_DOWN: {
                r = moveCursorVertical(1, shift);
                break;
            }
            case GLFW.GLFW_KEY_HOME: {
                moveCursorToLineStart(shift);
                r = true; break;
            }
            case GLFW.GLFW_KEY_END: {
                moveCursorToLineEnd(shift);
                r = true; break;
            }
            //TODO PAGE UP key
            //TODO PAGE DOWN key
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER: {
                if(multiline) insertText("\n");
                r = multiline; break;
            }
            default: {
                if(event.isSelectAll()) {
                    moveCursorToEnd(false);
                    setHighlightPos(0, 0);
                    updateLabel();
                    r = true; break;
                }
                if(event.isCopy()) {
                    Minecraft.getInstance().keyboardHandler.setClipboard(getHighlighted());
                    r = true; break;
                }
                if(event.isPaste()) {
                    insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                    r = true; break;
                }
                if(event.isCut()) {
                    Minecraft.getInstance().keyboardHandler.setClipboard(getHighlighted());
                    insertText("");
                    r = true; break;
                }
                r = false; break;
            }
        }
        if(r) playTypeSound();
        return r;
    }

    @Override
    public boolean charTyped(final CharacterEvent event) {
        if(!isActive() || !isFocused() || !editable || !isValidCharacter(event.codepoint())) return false;
        insertText(event.codepointAsString());
        playTypeSound();
        return true;
    }


    protected int[] findClickedPositionInText(final MouseButtonEvent event) {
        final int lineHeight = font.getLineHeight();
        final int relY = (int)Math.floor(event.y()) - getY() + visualScrollLinePx.compute();
        final int line = Math.clamp(relY / lineHeight, 0, lines.size() - 1);
        final int targetPx = Math.max(0, (int)Math.floor(event.x()) - getInnerX() + visualScrollPx.compute());
        final int col = font.getFont().plainSubstrByWidth(lines.get(line).toString(), targetPx).length();  //TODO this is prob very inefficient
        return new int[]{line, col};
    }

    @Override
    public void onClick(final MouseButtonEvent event, final boolean doubleClick) {
        final int[] pos = findClickedPositionInText(event);
        if(doubleClick) {
            final int[] start = getWordPosition(-1, pos[0], pos[1]);
            final int[] end = getWordPosition(1, pos[0], pos[1]);
            moveCursorTo(start[0], start[1], false);
            moveCursorTo(end[0], end[1], true);
        }
        else moveCursorTo(pos[0], pos[1], event.hasShiftDown());
    }

    @Override
    protected void onDrag(final MouseButtonEvent event, final double dx, final double dy) {
        final int[] pos = findClickedPositionInText(event);
        moveCursorTo(pos[0], pos[1], true);
    }

    @Override
    public void setFocused(final boolean focused) {
        super.setFocused(focused);
        if(focused) focusedTime = Util.getMillis();
        if(editable) Minecraft.getInstance().onTextInputFocusChange(this, focused);
        updateLabel();
    }
    protected int getTextOriginY() {
        final int lineHeight = font.getLineHeight();
        return multiline ? getY() : getY() + (getHeight() - lineHeight) / 2;
    }

    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        if(isFocused() || cursorLine != highlightLine || cursorCol != highlightCol) {

            final int computedCursorLine = Math.min(lines.size() - 1, visualCursorLine.compute()); //! Ensure the visual line number doesn't exceed the number of current lines
            final int computedCursorCol = Math.min(lines.get(computedCursorLine).length(), cursorCol); //! Ensure the cursor column doesn't exceed the line length in case of clamped line number
            final int lineHeight = font.getLineHeight();
            final int textX = getInnerX() - visualScrollPx.compute();
            final int textY = getTextOriginY() - visualScrollLinePx.compute();


            final int cursorX = visualCursorPosPx.compute();
            final int cursorY = textY + computedCursorLine * lineHeight;
            visualCursorPosPx.startNewTransition(textX + font.calcWidth(lines.get(computedCursorLine).substring(0, computedCursorCol)));  //TODO this is prob very inefficient

            if(cursorLine != highlightLine || cursorCol != highlightCol) {
                final boolean cursorFirst = comparePos(computedCursorLine, computedCursorCol, highlightLine, highlightCol) <= 0;
                final int startLine = cursorFirst ? computedCursorLine : highlightLine;
                final int startCol  = cursorFirst ? computedCursorCol  : highlightCol;
                final int endLine   = cursorFirst ? highlightLine : computedCursorLine;
                final int endCol    = cursorFirst ? highlightCol  : computedCursorCol;

                for(int line = startLine; line <= endLine; line++) {
                    final int selStart = line == startLine ? startCol : 0;
                    final int selEnd = line == endLine ? endCol : lines.get(line).length();

                    final boolean startEdgeIsCursor = line == startLine && cursorFirst;
                    final boolean endEdgeIsCursor   = line == endLine && !cursorFirst;

                    final int highlightX1 = startEdgeIsCursor ? cursorX : textX + font.calcWidth(lines.get(line).substring(0, selStart));  //TODO this is prob very inefficient
                    final int highlightX2 = endEdgeIsCursor   ? cursorX : textX + font.calcWidth(lines.get(line).substring(0, selEnd));  //TODO this is prob very inefficient
                    final int highlightY  = line == computedCursorLine ? cursorY : textY + line * lineHeight;
                    graphics.textHighlight(Math.min(highlightX1, getRight()), highlightY, Math.min(highlightX2 - 1, getRight()), highlightY + lineHeight, true);
                }
            }
            else if(isFocused() && (Util.getMillis() - lastMoveTime < CURSOR_BLINK_START_MS || TextCursorUtils.isCursorVisible(Util.getMillis() - focusedTime))) {
                // if(cursorCol < lines.get(computedCursorLine).length() || computedCursorLine < lines.size() - 1) {//TODO remove
                    TextCursorUtils.extractInsertCursor(graphics, cursorX - 1, cursorY, Layout.fgColor, lineHeight);
                // }//TODO remove
                // else {//TODO remove
                //     TextCursorUtils.extractAppendCursor(graphics, font.getFont(), cursorX, cursorY, Layout.fgColor, false);//TODO remove
                // }//TODO remove
            }
        }

        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        if(isHoveredOrBeingDragged()) graphics.requestCursor(editable ? CursorTypes.IBEAM : CursorTypes.NOT_ALLOWED);
    }

    @Override
    protected void extractLabel(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        if(renderLines.isEmpty()) return;
        final int lineHeight = font.getLineHeight();
        final int x = getInnerX() - visualScrollPx.compute();
        final int y = getTextOriginY() - visualScrollLinePx.compute();

        graphics.enableScissor(getInnerX(), getY(), getInnerRight(), getBottom());
        for(int i = 0; i < renderLines.size(); i++) {
            final UiTxt line = renderLines.get(i);
            if(line.length() > 0) {
                RenderingUtils.extractTxt(graphics, line, x, y + i * lineHeight, Layout.fgColor, TextAlignment.LEFT, getInnerWidth(), false);
            }
        }
        graphics.disableScissor();
    }


    // @Override
    // protected void extractLabel(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
    //     final UiTxt label = getLabel();
    //     if(label != null && label.length() > 0) {
    //         final int y = getY() + (getHeight() - label.getScaledFont().getLineHeight()) / 2;
    //         graphics.enableScissor(getInnerX(), getY(), getInnerRight(), getBottom());
    //         RenderingUtils.extractTxt(graphics, label, getInnerX() - visualScrollPx.compute(), y, Layout.fgColor, TextAlignment.LEFT, getInnerWidth(), false);
    //         graphics.disableScissor();
    //     }
    // }
}