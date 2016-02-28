package com.triangleleft.flashcards.service;

public interface ILoginRequest extends IProviderRequest {
    String getLogin();

    String getPassword();
}
