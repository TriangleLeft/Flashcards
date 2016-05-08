package com.triangleleft.flashcards;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class SimpleTextWatcher implements TextWatcher {

    public abstract void onChanged();

    @Override
    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Do nothing
    }

    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {
        onChanged();
    }

    @Override
    public final void afterTextChanged(Editable s) {
        // Do nothing
    }
}
