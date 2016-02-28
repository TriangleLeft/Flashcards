package com.triangleleft.flashcards.service;

import com.triangleleft.flashcards.service.error.CommonError;

public interface IProviderResult<T> {
    boolean isSuccess();
    CommonError getError();
    T getResult();
}
