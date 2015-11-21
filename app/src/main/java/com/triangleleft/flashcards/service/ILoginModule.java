package com.triangleleft.flashcards.service;

public interface ILoginModule {

    boolean isLogged();
    void login(String login, String password);
    void registerListener(ICommonListener listener);
    void unregisterListener(ICommonListener listener);
}
