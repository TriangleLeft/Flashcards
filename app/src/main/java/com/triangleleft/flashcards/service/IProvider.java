package com.triangleleft.flashcards.service;

import android.support.annotation.NonNull;

public interface IProvider<Request extends IProviderRequest,Result extends IProviderResult<?>> {

    void registerListener(@NonNull Request request, @NonNull IListener listener);

    void unregisterListener(@NonNull Request request, @NonNull IListener listener);

    void doRequest(@NonNull Request request, @NonNull IListener listener);

    @NonNull
    Result getResult(@NonNull Request request);

}
