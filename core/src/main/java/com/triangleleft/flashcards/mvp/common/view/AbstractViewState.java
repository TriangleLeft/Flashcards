package com.triangleleft.flashcards.mvp.common.view;

public abstract class AbstractViewState<View extends IView> implements IViewState {

    private final View view;

    public AbstractViewState(View view) {
        this.view = view;
    }

    protected View getView() {
        return view;
    }
}
