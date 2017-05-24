package com.triangleleft.flashcards.ui.common;

import com.triangleleft.flashcards.ui.ViewState;

public interface ViewAction<VS extends ViewState> {

    VS reduce(VS initialState);

}
