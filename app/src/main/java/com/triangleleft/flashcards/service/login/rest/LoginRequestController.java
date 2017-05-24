package com.triangleleft.flashcards.service.login.rest;

import com.google.gson.annotations.SerializedName;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;

@FunctionsAreNonnullByDefault
public class LoginRequestController {
    @SerializedName("login")
    private String login;
    @SerializedName("password")
    private String password;

    public LoginRequestController(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
