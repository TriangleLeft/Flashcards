package com.triangleleft.flashcards.mvp.common.view.delegate;

import com.triangleleft.flashcards.mvp.common.view.IView;

/**
 * Interface for the factory capable of building view delegates.
 */
public interface IViewDelegateFactory {

    /**
     * Build new view delegate.
     *
     * @param <View> type of view to build delegate for
     * @return view delegate
     */
    <View extends IView> IViewDelegate<View> buildViewDelegate();
}
