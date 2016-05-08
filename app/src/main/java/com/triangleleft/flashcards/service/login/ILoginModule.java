package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.service.common.IListener;
import com.triangleleft.flashcards.service.common.IProvider;

public interface ILoginModule extends IProvider {

    void login(ILoginRequest request, IListener<ILoginResult> listener);

    LoginStatus getLoginStatus();

}
