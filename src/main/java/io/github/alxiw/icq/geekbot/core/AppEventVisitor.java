package io.github.alxiw.icq.geekbot.core;

import io.github.alxiw.icq.geekbot.disp.AppDispatcher;
import io.github.alxiw.icq.geekbot.sys.AppLogger;
import ru.mail.im.botapi.fetcher.event.*;

public class AppEventVisitor implements EventVisitor<Void, Void> {

    private final AppDispatcher dispatcher;

    public AppEventVisitor(AppDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public Void visitUnknown(UnknownEvent event, Void v) {
        AppLogger.i("UnknownEvent, json:" + event.getJson());
        return v;
    }

    @Override
    public Void visitNewMessage(NewMessageEvent event, Void v) {
        AppLogger.i("NewMessageEvent, chat:" + event.getChat().getChatId());
        dispatcher.onNewMessageEvent(event);
        return v;
    }

    @Override
    public Void visitNewChatMembers(NewChatMembersEvent event, Void v) {
        AppLogger.i("LeftChatMembersEvent, chat:" + event.getChat().getChatId());
        return v;
    }

    @Override
    public Void visitLeftChatMembers(LeftChatMembersEvent event, Void v) {
        AppLogger.i("LeftChatMembersEvent, chat:" + event.getChat().getChatId());
        return v;
    }

    @Override
    public Void visitDeletedMessage(DeletedMessageEvent event, Void v) {
        AppLogger.i("DeletedMessageEvent, chat:" + event.getChat().getChatId());
        return v;
    }

    @Override
    public Void visitEditedMessage(EditedMessageEvent event, Void v) {
        AppLogger.i("EditedMessageEvent, chat:" + event.getChat().getChatId());
        return v;
    }

    @Override
    public Void visitPinnedMessage(PinnedMessageEvent event, Void v) {
        AppLogger.i("PinnedMessageEvent, chat:" + event.getChat().getChatId());
        return v;
    }

    @Override
    public Void visitUnpinnedMessage(UnpinnedMessageEvent event, Void v) {
        AppLogger.i("UnpinnedMessageEvent, chat:" + event.getChat().getChatId());
        return v;
    }

    @Override
    public Void visitCallbackQuery(CallbackQueryEvent event, Void v) {
        AppLogger.i("CallbackQueryEvent, chat:" + event.getMessageChat().getChatId());
        dispatcher.onCallbackQueryEvent(event);
        return v;
    }
}
