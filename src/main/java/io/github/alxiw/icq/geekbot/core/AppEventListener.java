package io.github.alxiw.icq.geekbot.core;

import io.github.alxiw.icq.geekbot.disp.AppDispatcher;
import ru.mail.im.botapi.fetcher.OnEventFetchListener;
import ru.mail.im.botapi.fetcher.event.Event;

import java.util.List;

public class AppEventListener implements OnEventFetchListener {

    private final AppDispatcher dispatcher;

    public AppEventListener(AppDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void onEventFetch(List<Event> events) {
        events.forEach(e -> e.accept(new AppEventVisitor(dispatcher), null));
    }
}
