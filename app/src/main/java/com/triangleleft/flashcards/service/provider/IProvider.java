package com.triangleleft.flashcards.service.provider;

import android.support.annotation.NonNull;

public interface IProvider<Request extends IProviderRequest,Result extends IProviderResult<?>> {

    void registerListener(@NonNull Request request, @NonNull IListener<Result> listener);

    void unregisterListener(@NonNull Request request, @NonNull IListener<Result> listener);

    void doRequest(@NonNull Request request, @NonNull IListener<Result> listener);

}
