package io.github.alxiw.icq.geekbot.disp;

import ru.mail.im.botapi.api.entity.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ButtonsUtils {

    public static final String HELLO_TEXT = "Hello! \uD83D\uDC4B\n" +
            "I am a bot that knows a lot of jokes about geeks and programming. \uD83D\uDCBE " +
            "If you are in, I can cheer you up. \uD83E\uDD13 " +
            "Let's try it, just push the button below";
    public static final String WAITING_TEXT = "⏳ Waiting";
    public static final String ERROR_TEXT = "⚠️ Error";
    public static final String UNRESPONSIVE_TEXT = "⛔ Jokes are unresponsive, try again later";

    public static List<List<InlineKeyboardButton>> buildKeyboard(List<Integer> config, Button... buttons) {
        int sum = 0;
        for (Integer i : config) {
            sum += i;
        }
        if (sum != buttons.length) {
            return null;
        }
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> line = new ArrayList<>();
        int row = 0;
        for (int i = 0; i < config.size(); i++) {
            for (int j = 0; j < config.get(row); j++) {
                Button btn = buttons[i + j];
                line.add(InlineKeyboardButton.callbackButton(btn.getDescription(), btn.getCode()));
            }
            row++;
            keyboard.add(line);
            line = new ArrayList<>();
        }
        return keyboard;
    }
}
