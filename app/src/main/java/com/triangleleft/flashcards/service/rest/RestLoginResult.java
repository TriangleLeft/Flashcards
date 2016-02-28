package com.triangleleft.flashcards.service.rest;

import com.triangleleft.flashcards.service.ILoginResult;
import com.triangleleft.flashcards.service.LoginStatus;
import com.triangleleft.flashcards.service.error.CommonError;

public class RestLoginResult implements ILoginResult {
    @Override
    public boolean isSuccess() {
        return getError() == null;
    }

    @Override
    public CommonError getError() {
        return null;
    }

    @Override
    public LoginStatus getResult() {
        return null;
    }
}
