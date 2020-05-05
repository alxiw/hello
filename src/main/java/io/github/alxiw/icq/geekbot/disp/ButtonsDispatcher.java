package io.github.alxiw.icq.geekbot.disp;

import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.AnswerCallbackQueryRequest;
import ru.mail.im.botapi.api.entity.EditTextRequest;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.CallbackQueryEvent;
import ru.mail.im.botapi.response.MessageResponse;

import java.io.IOException;

import static io.github.alxiw.icq.geekbot.disp.ButtonsUtils.UNRESPONSIVE_TEXT;
import static io.github.alxiw.icq.geekbot.disp.ButtonsUtils.WAITING_TEXT;

public class ButtonsDispatcher {

    private final BotApiClientController controller;
    private final ButtonsClickListener listener;

    public ButtonsDispatcher(BotApiClientController controller, ButtonsClickListener listener) {
        this.controller = controller;
        this.listener = listener;
    }

    public void onHelloStartClick(CallbackQueryEvent event) throws IOException {
        controller.answerCallbackQuery(new AnswerCallbackQueryRequest()
                .setQueryId(event.getQueryId())
                .setShowAlert(false));
        String chatId = event.getMessageChat().getChatId();
        long messageId = event.getMessageId();
        controller.editText(
                new EditTextRequest()
                        .setChatId(chatId)
                        .setMsgId(messageId)
                        .setNewText(WAITING_TEXT)
        );
        Message message = new Message(messageId, chatId);
        listener.requestJoke(message);
    }

    public void onErrorRetryClick(CallbackQueryEvent event) throws IOException {
        controller.answerCallbackQuery(new AnswerCallbackQueryRequest()
                .setQueryId(event.getQueryId())
                .setShowAlert(false));
        String chatId = event.getMessageChat().getChatId();
        long messageId = event.getMessageId();
        controller.editText(
                new EditTextRequest()
                        .setChatId(chatId)
                        .setMsgId(messageId)
                        .setNewText(WAITING_TEXT)
        );
        Message message = new Message(messageId, chatId);
        listener.requestJoke(message);
    }

    public void onErrorOkClick(CallbackQueryEvent event) throws IOException {
        controller.answerCallbackQuery(new AnswerCallbackQueryRequest()
                .setQueryId(event.getQueryId())
                .setShowAlert(false));
        String chatId = event.getMessageChat().getChatId();
        controller.editText(
                new EditTextRequest()
                        .setChatId(chatId)
                        .setMsgId(event.getMessageId())
                        .setNewText(UNRESPONSIVE_TEXT)
        );
    }

    public void onSuccessRefreshClick(CallbackQueryEvent event) throws IOException {
        controller.answerCallbackQuery(new AnswerCallbackQueryRequest()
                .setQueryId(event.getQueryId())
                .setShowAlert(false));
        String chatId = event.getMessageChat().getChatId();
        long messageId = event.getMessageId();
        controller.editText(
                new EditTextRequest()
                        .setChatId(chatId)
                        .setMsgId(event.getMessageId())
                        .setNewText(WAITING_TEXT)
        );
        Message message = new Message(messageId, chatId);
        listener.requestJoke(message);
    }

    public void onSuccessNextClick(CallbackQueryEvent event) throws IOException {
        controller.answerCallbackQuery(new AnswerCallbackQueryRequest()
                .setQueryId(event.getQueryId())
                .setShowAlert(false));
        String chatId = event.getMessageChat().getChatId();
        long msgId = event.getMessageId();
        String text = event.getMessageText();
        controller.editText(
                new EditTextRequest()
                        .setChatId(chatId)
                        .setMsgId(msgId)
                        .setNewText(text)
        );

        MessageResponse response = controller.sendTextMessage(
                new SendTextRequest()
                        .setChatId(chatId)
                        .setText(WAITING_TEXT)
        );
        long messageId = response.getMsgId();
        Message message = new Message(messageId, chatId);
        listener.requestJoke(message);
    }
}
