package io.github.alxiw.icq.geekbot.disp;

import io.github.alxiw.icq.geekbot.sys.AppLogger;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.EditTextRequest;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static io.github.alxiw.icq.geekbot.disp.Button.*;
import static io.github.alxiw.icq.geekbot.disp.Button.SUCCESS_NEXT;
import static io.github.alxiw.icq.geekbot.disp.ButtonsUtils.ERROR_TEXT;
import static io.github.alxiw.icq.geekbot.disp.ButtonsUtils.buildKeyboard;

public class ResponsesDispatcher {

    private final BotApiClientController controller;

    public ResponsesDispatcher(BotApiClientController controller) {
        this.controller = controller;
    }

    public void sendError(String error, Message message) {
        AppLogger.e(error);
        List<List<InlineKeyboardButton>> keyboard = buildKeyboard(Collections.singletonList(2), ERROR_RETRY, ERROR_OK);
        if (keyboard == null) {
            AppLogger.i("keyboard is null");
        }
        try {
            controller.editText(new EditTextRequest()
                    .setChatId(message.getChatId())
                    .setMsgId(message.getId())
                    .setNewText(ERROR_TEXT)
                    .setKeyboard(keyboard));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSuccess(String joke, Message message) {
        AppLogger.i("joke: " + joke);
        List<List<InlineKeyboardButton>> keyboard = buildKeyboard(Collections.singletonList(2), SUCCESS_REFRESH, SUCCESS_NEXT);
        if (keyboard == null) {
            AppLogger.i("keyboard is null");
        }
        try {
            controller.editText(new EditTextRequest()
                    .setChatId(message.getChatId())
                    .setMsgId(message.getId())
                    .setNewText(joke)
                    .setKeyboard(keyboard));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
