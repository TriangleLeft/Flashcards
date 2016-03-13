package com.triangleleft.flashcards.android;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.mvp.common.view.IViewDelegate;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractViewDelegate<View extends IView> implements IViewDelegate<View> {

    private View view;

    private final ViewHandler handler = new ViewHandler();

    @Override
    public synchronized void onBind(@NonNull View view) {
        this.view = view;
        handler.resume();
    }

    public View getView() {
        return view;
    }

    @Override
    public synchronized void onUnbind() {
        handler.pause();
        this.view = null;
    }

    public Handler getHandler() {
        return handler;
    }

    private static class ViewHandler extends Handler {

        private final List<Message> messageQueueBuffer = Collections.synchronizedList(new ArrayList<>());

        private volatile boolean isPaused = false;

        public synchronized void resume() {
            while (messageQueueBuffer.size() > 0) {
                final Message msg = messageQueueBuffer.get(0);
                messageQueueBuffer.remove(0);
                sendMessage(msg);
            }
            isPaused = false;
        }

        public synchronized void pause() {
            isPaused = true;
        }

        @Override
        public synchronized void handleMessage(Message msg) {
            if (isPaused) {
                final Message msgCopy = new Message();
                msgCopy.copyFrom(msg);
                messageQueueBuffer.add(msgCopy);
            } else {
                msg.getCallback().run();
            }
        }
    }
}
