package com.triangleleft.flashcards.service.login.rest;

import com.triangleleft.flashcards.Action;
import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.service.RestService;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.login.LoginModule;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@FunctionsAreNonnullByDefault
public class RestLoginModule implements LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(RestLoginModule.class);

    private final RestService service;
    private final AccountModule accountModule;
    private final SettingsModule settingsModule;

    @Inject
    public RestLoginModule(RestService service, SettingsModule settingsModule, AccountModule accountModule) {
        this.service = service;
        this.settingsModule = settingsModule;
        this.accountModule = accountModule;
    }

    @Override
    public Call<Object> login(String login, String password) {
        accountModule.setLogin(login);
        Call<LoginResponseModel> call = service.login(new LoginRequestController(login, password));
        return new Call<Object>() {
            @Override
            public void enqueue(Action<Object> onData, Action<Throwable> onError) {
                call.enqueue(model -> {
                    if (model.isSuccess()) {
                        accountModule.setUserId(model.getUserId());
                        settingsModule.loadUserData().enqueue(data -> {
                            accountModule.setUserData(data);
                            onData.call(new Object());
                        }, onError::call);
                    } else {
                        onError.call(model.getError());
                    }
                }, onError::call);
            }

            @Override
            public void cancel() {
                call.cancel();
            }
        };
    }


}