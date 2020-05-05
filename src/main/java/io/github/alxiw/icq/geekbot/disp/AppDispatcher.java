package io.github.alxiw.icq.geekbot.disp;

import io.github.alxiw.icq.geekbot.api.Service;
import io.github.alxiw.icq.geekbot.api.Joke;
import io.github.alxiw.icq.geekbot.api.ServiceProvider;
import io.github.alxiw.icq.geekbot.sys.AppLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.AnswerCallbackQueryRequest;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.CallbackQueryEvent;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static io.github.alxiw.icq.geekbot.disp.Button.*;
import static io.github.alxiw.icq.geekbot.disp.ButtonsUtils.HELLO_TEXT;
import static io.github.alxiw.icq.geekbot.disp.ButtonsUtils.buildKeyboard;

public class AppDispatcher implements ButtonsClickListener {

    private final BotApiClientController controller;
    private final ButtonsDispatcher buttons;
    private final ResponsesDispatcher responses;
    private final Service service;

    public AppDispatcher(BotApiClientController controller) {
        this.controller = controller;
        this.buttons = new ButtonsDispatcher(controller, this);
        this.responses = new ResponsesDispatcher(controller);
        this.service = ServiceProvider.getService();
    }

    public void onNewMessageEvent(NewMessageEvent event) {
        String chatId = event.getChat().getChatId();
        List<List<InlineKeyboardButton>> keyboard = buildKeyboard(Collections.singletonList(1), HELLO_START);
        if (keyboard == null) {
            AppLogger.i("keyboard is null");
        }
        handleNewMessageEvent(chatId, keyboard);
    }

    public void onCallbackQueryEvent(CallbackQueryEvent event) {
        String code = event.getCallbackData();
        Button cb = Button.fromCode(code);
        if (cb == null) {
            AppLogger.e("callback button is null");
            return;
        }
        handleCallbackQueryEvent(event, cb);
    }

    @Override
    public void requestJoke(Message message) {
        service.getJoke().enqueue(new Callback<Joke>() {
                    @Override
                    public void onResponse(Call<Joke> call, Response<Joke> response) {
                        if (response.isSuccessful()) {
                            Joke joke = response.body();
                            if (joke == null) {
                                responses.sendError("response does not contain body", message);
                                return;
                            }
                            String text = joke.getJoke();
                            if (text == null) {
                                responses.sendError("response body does not contain a joke", message);
                            }
                            responses.sendSuccess(text, message);
                        } else {
                            responses.sendError("response is null", message);
                        }
                    }

                    @Override
                    public void onFailure(Call<Joke> call, Throwable t) {
                        responses.sendError(t.getMessage(), message);
                    }
                });
    }

    private void handleNewMessageEvent(String chatId, List<List<InlineKeyboardButton>> keyboard) {
        try {
            controller.sendTextMessage(
                    new SendTextRequest()
                            .setChatId(chatId)
                            .setText(HELLO_TEXT)
                            .setKeyboard(keyboard)
            );
        } catch (IOException e) {
            AppLogger.e(e, "error while try to handle new message event");
        }
    }

    private void handleCallbackQueryEvent(CallbackQueryEvent event, Button cb) {
        try {
            switch (cb) {
                case HELLO_START:
                    buttons.onHelloStartClick(event);
                    break;
                case ERROR_RETRY:
                    buttons.onErrorRetryClick(event);
                    break;
                case ERROR_OK:
                    buttons.onErrorOkClick(event);
                    break;
                case SUCCESS_REFRESH:
                    buttons.onSuccessRefreshClick(event);
                    break;
                case SUCCESS_NEXT:
                    buttons.onSuccessNextClick(event);
                    break;
                default:
                    AppLogger.i("callback is not match any exist");
                    controller.answerCallbackQuery(new AnswerCallbackQueryRequest()
                            .setQueryId(event.getQueryId())
                            .setShowAlert(false));
            }
        } catch (IOException e) {
            AppLogger.e(e, "error while try to handle callback query event");
        }
    }
}
