package com.triangleleft.flashcards.ui.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import com.triangleleft.flashcards.util.TextUtils;

public class CustomEditText extends TextInputEditText {

    private boolean ignoreChange;
    private ITextChangedListener listener;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onChanged() {
                if (listener != null && !ignoreChange) {
                    listener.onChanged(getText().toString());
                }
            }
        });
    }

    public void setOnTextChangedListener(@Nullable ITextChangedListener listener) {
        this.listener = listener;
    }

    public void replaceText(@Nullable String text) {
        if (TextUtils.notEquals(text, getText().toString())) {
            ignoreChange = true;
            setText(text);
            ignoreChange = false;
        }
    }


}
