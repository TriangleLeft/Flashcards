package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.IProvider;
import com.triangleleft.flashcards.service.common.IProviderRequest;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import android.support.annotation.Nullable;

@FunctionsAreNonnullByDefault
public interface LoginModule extends IProvider {

    void login(ILoginRequest request, IListener<ILoginResult> listener);

    LoginStatus getLoginStatus();

    @Nullable
    String getUserId();

    void cancelRequest(IProviderRequest request);

    String getLogin();
}
