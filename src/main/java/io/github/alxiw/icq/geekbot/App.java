package io.github.alxiw.icq.geekbot;

import io.github.alxiw.icq.geekbot.core.AppEventListener;
import io.github.alxiw.icq.geekbot.disp.AppDispatcher;
import io.github.alxiw.icq.geekbot.sys.AppLogger;
import ru.mail.im.botapi.BotApiClient;
import ru.mail.im.botapi.BotApiClientController;

public class App {

    public static void main(String[] args) {
        String token = System.getenv("TOKEN");
        if (token == null) {
            AppLogger.e(new RuntimeException(), "token is null");
            return;
        }
        BotApiClient client = new BotApiClient(token);
        BotApiClientController controller = BotApiClientController.startBot(client);
        AppDispatcher dispatcher = new AppDispatcher(controller);
        AppLogger.i("app successfully started");
        client.addOnEventFetchListener(new AppEventListener(dispatcher));
    }
}
