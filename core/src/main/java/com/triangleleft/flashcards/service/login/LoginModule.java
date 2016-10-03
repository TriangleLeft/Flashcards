package com.triangleleft.flashcards.service.login;

import com.triangleleft.flashcards.Call;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public interface LoginModule {

    Call<Object> login(String login, String password);
}
