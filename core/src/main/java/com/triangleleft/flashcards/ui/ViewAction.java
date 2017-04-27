package com.triangleleft.flashcards.ui;

public interface ViewAction<VS extends ViewState> {

    VS reduce(VS initialState);
}
