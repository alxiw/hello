package io.github.alxiw.icq.geekbot.disp;

import org.junit.Test;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.alxiw.icq.geekbot.disp.Button.*;
import static org.junit.Assert.*;

public class ButtonsUtilsTest {

    @Test
    public void build1x1Keyboard() {
        List<List<InlineKeyboardButton>> keyboard = ButtonsUtils.buildKeyboard(Collections.singletonList(1), HELLO_START);
        assertNotNull("keyboard is null", keyboard);
        int rows = keyboard.size();
        assertEquals(1, rows);
        int columns = keyboard.get(0).size();
        assertEquals(1, columns);
    }

    @Test
    public void build1x2Keyboard() {
        List<List<InlineKeyboardButton>> keyboard = ButtonsUtils.buildKeyboard(Collections.singletonList(2), SUCCESS_REFRESH, SUCCESS_NEXT);
        assertNotNull("keyboard is null", keyboard);
        int rows = keyboard.size();
        assertEquals(1, rows);
        int columns = keyboard.get(0).size();
        assertEquals(2, columns);
    }

    @Test
    public void build2x1Keyboard() {
        List<List<InlineKeyboardButton>> keyboard = ButtonsUtils.buildKeyboard(Arrays.asList(1, 1), SUCCESS_REFRESH, SUCCESS_NEXT);
        assertNotNull("keyboard is null", keyboard);
        int rows = keyboard.size();
        assertEquals(2, rows);
        int columns1 = keyboard.get(0).size();
        assertEquals(1, columns1);
        int columns2 = keyboard.get(1).size();
        assertEquals(1, columns2);
    }

    @Test
    public void build5x3v7v5v1v2KeyboardWithout2Buttons() {
        List<List<InlineKeyboardButton>> keyboard = ButtonsUtils.buildKeyboard(
                Arrays.asList(3, 7, 5, 1, 2),
                HELLO_START, SUCCESS_REFRESH, SUCCESS_NEXT,
                HELLO_START, HELLO_START, HELLO_START, HELLO_START, HELLO_START, HELLO_START, HELLO_START,
                SUCCESS_NEXT, SUCCESS_NEXT, SUCCESS_NEXT, SUCCESS_NEXT, SUCCESS_NEXT,
                SUCCESS_REFRESH
        );
        assertNull("keyboard is null", keyboard);
    }

    @Test
    public void build4x3v7v5v1Keyboard() {
        List<List<InlineKeyboardButton>> keyboard = ButtonsUtils.buildKeyboard(
                Arrays.asList(3, 7, 5, 1),
                HELLO_START, SUCCESS_REFRESH, SUCCESS_NEXT,
                HELLO_START, HELLO_START, HELLO_START, HELLO_START, HELLO_START, HELLO_START, HELLO_START,
                SUCCESS_NEXT, SUCCESS_NEXT, SUCCESS_NEXT, SUCCESS_NEXT, SUCCESS_NEXT,
                SUCCESS_REFRESH);
        assertNotNull("keyboard is null", keyboard);
        int rows = keyboard.size();
        assertEquals(4, rows);
        int columns1 = keyboard.get(0).size();
        assertEquals(3, columns1);
        int columns2 = keyboard.get(1).size();
        assertEquals(7, columns2);
        int columns3 = keyboard.get(2).size();
        assertEquals(5, columns3);
        int columns4 = keyboard.get(3).size();
        assertEquals(1, columns4);
        String code = keyboard.get(0).get(1).getCallbackData();
        assertEquals(SUCCESS_REFRESH, Button.fromCode(code));
    }
}
