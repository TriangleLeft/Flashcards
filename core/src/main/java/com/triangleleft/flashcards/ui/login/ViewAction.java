package com.triangleleft.flashcards.ui.login;

public interface ViewAction<VS extends ViewState> {

    VS reduce(VS initialState);
}
