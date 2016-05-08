package com.triangleleft.flashcards;

import com.triangleleft.flashcards.mvp.common.view.IView;
import com.triangleleft.flashcards.mvp.common.view.IViewAction;
import com.triangleleft.flashcards.mvp.common.view.delegate.IViewDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidViewDelegate<View extends IView> implements IViewDelegate<View> {

    private static final Logger logger = LoggerFactory.getLogger(AndroidViewDelegate.class);

    private View view;

    private final ViewHandler handler = new ViewHandler();

    @Override
    public synchronized void onBind(@NonNull View view) {
        logger.debug("onRebind() called with: view = [{}]", view);
        this.view = view;
        handler.resume();
    }

    public View getView() {
        return view;
    }

    @Override
    public synchronized void onUnbind() {
        logger.debug("onUnbind() called");
        handler.pause();
        this.view = null;
    }

    @Override
    public void post(@NonNull IViewAction<View> action) {
        // TODO: I believe this can be made simpler (sendMessage instead of post?)
        logger.debug("post() called with: action = [{}], paused = [{}], {}", action, handler.isPaused, this);
        getHandler().post(() -> action.apply(getView()));
    }

    private Handler getHandler() {
        return handler;
    }

    private static class ViewHandler extends Handler {

        private final List<Message> messageQueueBuffer = Collections.synchronizedList(new ArrayList<>());

        public volatile boolean isPaused = true;

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

        @Override
        public void dispatchMessage(Message msg) {
            if (msg.getCallback() != null) {
                handleMessage(msg);
            }
        }
    }
}
