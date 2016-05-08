package com.triangleleft.flashcards.service.common;

import com.triangleleft.flashcards.service.common.error.CommonError;

public interface IListener<Result extends IProviderResult<?>> {

    void onResult(Result result);

    void onFailure(CommonError error);
}
