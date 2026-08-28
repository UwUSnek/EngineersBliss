package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedInt;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.TextCursorUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringUtil;
import net.minecraft.util.Util;
















public class UiEditBox extends __base_UiWidget {

    private static final Style HINT_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);

    private final UiTxt hint;
    private final Consumer<String> responder;

    private String value;
    private int maxLength;
    private int cursorPos;
    private AnimatedInt visualCursorPosPx;
    private int highlightPos;
    private AnimatedInt scrollPx; // horizontal scroll offset, in pixels
    private boolean editable;
    private long focusedTime;


    public UiEditBox(final Screen screen, final UiTxt hint, final Consumer<String> responder) {
        super(screen, new UiTxt(Component.empty()), TextAlignment.LEFT);
        this.hint         = hint;
        this.responder    = responder;
        this.value        = "";
        this.maxLength    = Integer.MAX_VALUE;
        this.cursorPos    = 0;
        this.visualCursorPosPx = new AnimatedInt(0, 50);
        this.highlightPos = 0;
        this.scrollPx     = new AnimatedInt(0, 50);
        this.editable     = true;
        this.focusedTime  = Util.getMillis();
        setBgColor(Layout.bgColor);
        updateLabel();
    }

    private Font getFont() {
        return getLabel().getScaledFont().getFont();
    }


    @Override
    public void relayoutSelf() {
        updateLabel();
    }


    public String getValue() { return value; }

    public void setValue(final String newValue) {
        value = newValue.length() > maxLength ? newValue.substring(0, maxLength) : newValue;
        cursorPos = value.length();
        highlightPos = cursorPos;
        scrollPx.startNewTransition(0); //BUG idk why this is set to 0
        onValueChange();
    }

    public void setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
        if(value.length() > maxLength) {
            value = value.substring(0, maxLength);
            onValueChange();
        }
    }

    public void setEditable(final boolean editable) { this.editable = editable; }

    public String getHighlighted() {
        final int start = Math.min(cursorPos, highlightPos);
        final int end = Math.max(cursorPos, highlightPos);
        return value.substring(start, end);
    }

    private void onValueChange() {
        if(responder != null) responder.accept(value);
        updateLabel();
    }


    public void insertText(final String input) {
        final int start = Math.min(cursorPos, highlightPos);
        final int end = Math.max(cursorPos, highlightPos);
        int maxInsertionLength = maxLength - value.length() - (start - end);
        if(maxInsertionLength > 0) {
            String text = StringUtil.filterText(input);
            int insertionLength = text.length();
            if(maxInsertionLength < insertionLength) {
                if(Character.isHighSurrogate(text.charAt(maxInsertionLength - 1))) maxInsertionLength--;
                text = text.substring(0, maxInsertionLength);
                insertionLength = maxInsertionLength;
            }
            value = new StringBuilder(value).replace(start, end, text).toString();
            setCursorPosition(start + insertionLength);
            setHighlightPos(cursorPos);
            onValueChange();
        }
    }

    private void deleteCharsToPos(final int pos) {
        if(!value.isEmpty()) {
            if(highlightPos != cursorPos) insertText("");
            else {
                final int start = Math.min(pos, cursorPos);
                final int end = Math.max(pos, cursorPos);
                if(start != end) {
                    value = new StringBuilder(value).delete(start, end).toString();
                    setCursorPosition(start);
                    setHighlightPos(start);
                    onValueChange();
                }
            }
        }
    }

    public void deleteChars(final int dir) {
        deleteCharsToPos(Util.offsetByCodepoints(value, cursorPos, dir));
    }

    public void deleteWords(final int dir) {
        if(!value.isEmpty()) {
            if(highlightPos != cursorPos) insertText("");
            else deleteCharsToPos(getWordPosition(dir));
        }
    }

    private int getWordPosition(final int dir) {
        return getWordPosition(dir, cursorPos);
    }

    private int getWordPosition(final int dir, final int from) {
        int result = from;
        final boolean reverse = dir < 0;
        final int abs = Math.abs(dir);
        for(int i = 0; i < abs; i++) {
            if(!reverse) {
                final int length = value.length();
                result = value.indexOf(' ', result);
                if(result == -1) result = length;
                else while(result < length && value.charAt(result) == ' ') result++;
            }
            else {
                while(result > 0 && value.charAt(result - 1) == ' ') result--;
                while(result > 0 && value.charAt(result - 1) != ' ') result--;
            }
        }
        return result;
    }


    public void moveCursor(final int dir, final boolean extendSelection) {
        moveCursorTo(Util.offsetByCodepoints(value, cursorPos, dir), extendSelection);
    }

    public void moveCursorTo(final int pos, final boolean extendSelection) {
        setCursorPosition(pos);
        if(!extendSelection) setHighlightPos(cursorPos);
        updateLabel();
    }

    public void moveCursorToStart(final boolean extendSelection) { moveCursorTo(0, extendSelection); }
    public void moveCursorToEnd(final boolean extendSelection) { moveCursorTo(value.length(), extendSelection); }

    private void setCursorPosition(final int pos) {
        cursorPos = Math.clamp(pos, 0, value.length());
        scrollTo(cursorPos);
    }

    private void setHighlightPos(final int pos) {
        highlightPos = Math.clamp(pos, 0, value.length());
        scrollTo(highlightPos);
    }

    private void scrollTo(final int pos) {
        final Font font = getFont();
        final int innerWidth = getInnerWidth();
        final int posX = font.width(value.substring(0, pos));
        int curScrollPx = scrollPx.compute();

        if(posX - curScrollPx > innerWidth) curScrollPx = posX - innerWidth;
        if(posX - curScrollPx < 0)          curScrollPx = posX;

        final int maxScroll = Math.max(0, font.width(value) - innerWidth);
        scrollPx.startNewTransition(Math.clamp(curScrollPx, 0, maxScroll));
    }


    private void updateLabel() {
        if(value.isEmpty() && !isFocused() && hint != null) setLabel(hint.get().copy().withStyle(HINT_STYLE));
        else setLabel(Component.literal(value));
    }


    @Override
    public boolean keyPressed(final KeyEvent event) {
        if(!isActive() || !isFocused()) return false;
        final boolean ctrl = event.hasControlDownWithQuirk();
        final boolean shift = event.hasShiftDown();
        switch(event.key()) {
            case GLFW.GLFW_KEY_BACKSPACE: {
                if(editable) deleteCharsToPos(ctrl ? getWordPosition(-1) : Util.offsetByCodepoints(value, cursorPos, -1));
                return true;
            }
            case GLFW.GLFW_KEY_DELETE: {
                if(editable) deleteCharsToPos(ctrl ? getWordPosition(1) : Util.offsetByCodepoints(value, cursorPos, 1));
                return true;
            }
            case GLFW.GLFW_KEY_RIGHT: {
                moveCursorTo(ctrl ? getWordPosition(1) : Util.offsetByCodepoints(value, cursorPos, 1), shift);
                return true;
            }
            case GLFW.GLFW_KEY_LEFT: {
                moveCursorTo(ctrl ? getWordPosition(-1) : Util.offsetByCodepoints(value, cursorPos, -1), shift);
                return true;
            }
            case GLFW.GLFW_KEY_HOME: {
                moveCursorToStart(shift);
                return true;
            }
            case GLFW.GLFW_KEY_END: {
                moveCursorToEnd(shift);
                return true;
            }
            default: {
                if(event.isSelectAll()) {
                    moveCursorToEnd(false);
                    setHighlightPos(0);
                    updateLabel();
                    return true;
                }
                if(event.isCopy()) {
                    Minecraft.getInstance().keyboardHandler.setClipboard(getHighlighted());
                    return true;
                }
                if(event.isPaste()) {
                    if(editable) insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                    return true;
                }
                if(event.isCut()) {
                    Minecraft.getInstance().keyboardHandler.setClipboard(getHighlighted());
                    if(editable) insertText("");
                    return true;
                }
                return false;
            }
        }
    }

    @Override
    public boolean charTyped(final CharacterEvent event) {
        if(!isActive() || !isFocused() || !editable || !event.isAllowedChatCharacter()) return false;
        insertText(event.codepointAsString());
        return true;
    }


    private int findClickedPositionInText(final MouseButtonEvent event) {
        final int targetPx = Math.max(0, (int)Math.floor(event.x()) - getInnerX() + scrollPx.compute());
        return getFont().plainSubstrByWidth(value, targetPx).length();
    }

    @Override
    public void onClick(final MouseButtonEvent event, final boolean doubleClick) {
        if(doubleClick) {
            final int clickedPosition = findClickedPositionInText(event);
            moveCursorTo(getWordPosition(-1, clickedPosition), false);
            moveCursorTo(getWordPosition(1, clickedPosition), true);
        }
        else moveCursorTo(findClickedPositionInText(event), event.hasShiftDown());
    }

    @Override
    protected void onDrag(final MouseButtonEvent event, final double dx, final double dy) {
        moveCursorTo(findClickedPositionInText(event), true);
    }

    @Override
    public void setFocused(final boolean focused) {
        super.setFocused(focused);
        if(focused) focusedTime = Util.getMillis();
        if(editable) Minecraft.getInstance().onTextInputFocusChange(this, focused);
        updateLabel();
    }


    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        updateLabel();

        if(isFocused() || highlightPos != cursorPos) {
            final Font font = getFont();
            final int textX = getInnerX() - scrollPx.compute();
            final int textY = getY() + (getHeight() - 9) / 2;
            visualCursorPosPx.startNewTransition(textX + font.width(value.substring(0, cursorPos)));
            final int cursorX = visualCursorPosPx.compute();

            if(highlightPos != cursorPos) {
                final int highlightX = textX + font.width(value.substring(0, highlightPos));
                graphics.textHighlight(Math.min(cursorX, getRight()), textY - 1, Math.min(highlightX - 1, getRight()), textY + 1 + 9, true);
            }
            else if(isFocused() && TextCursorUtils.isCursorVisible(Util.getMillis() - focusedTime)) {
                if(cursorPos < value.length()) TextCursorUtils.extractInsertCursor(graphics, cursorX - 1, textY, Layout.fgColor, 9 + 1);
                else TextCursorUtils.extractAppendCursor(graphics, font, cursorX, textY, Layout.fgColor, false);
            }
        }

        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        if(isHoveredOrBeingDragged()) graphics.requestCursor(editable ? CursorTypes.IBEAM : CursorTypes.NOT_ALLOWED);
    }


    @Override
    protected void extractLabel(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        final UiTxt label = getLabel();
        if(label != null && label.length() > 0) {
            final int y = getY() + (getHeight() - label.getScaledFont().getLineHeight()) / 2;
            graphics.enableScissor(getInnerX(), getY(), getInnerRight(), getBottom());
            RenderingUtils.extractTxt(graphics, label, getInnerX() - scrollPx.compute(), y, Layout.fgColor, TextAlignment.LEFT, getInnerWidth(), false);
            graphics.disableScissor();
        }
    }
}