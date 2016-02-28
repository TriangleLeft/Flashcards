package com.triangleleft.flashcards.service;

import com.triangleleft.flashcards.service.error.CommonError;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProvider<Request extends IProviderRequest, Result extends IProviderResult<?>>
        implements IProvider<Request, Result> {

    private static final String TAG = AbstractProvider.class.getSimpleName();

    private final ConcurrentHashMap<String, IListener> listeners = new ConcurrentHashMap<>();
    private final Map<String, Boolean> notifyMap = new ConcurrentHashMap<>();

    @Override
    public void registerListener(@NonNull Request request, @NonNull IListener listener) {
        Log.d(TAG, "registerListener() called with: " + "listener = [" + listener + "]");
        
        if (!listeners.containsKey(request.getTag())) {
            throw new UnknownRequestException(request.getTag());
        }
        listeners.put(request.getTag(), listener);
    }

    @Override
    public void unregisterListener(@NonNull Request request, @NonNull IListener listener) {
        Log.d(TAG, "unregisterListener() called with: " + "listener = [" + listener + "]");
        listeners.remove(request.getTag());
    }

    protected void notifySuccess() {
        Log.d(TAG, "notifySuccess() called");
        for (IListener listener : listeners) {
            listener.onSuccess();
        }
    }

    protected void notifyFailure(@NonNull CommonError error) {
        Log.d(TAG, "notifyFailure() called with: " + "error = [" + error + "]");
        for (IListener listener : listeners) {
            listener.onError(error);
        }
    }
}
