package com.triangleleft.flashcards.mvp.common.view.delegate;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.mvp.common.view.IView;

/**
 * Static factory capable of building view delegates.
 * Before using, you have to call {@link #setDefault(IViewDelegateFactory)} and set factory that would be actually
 * building delegates.
 */
public class ViewDelegateFactory {

    private ViewDelegateFactory() {
        // Static use only
    }

    private static IViewDelegateFactory delegateFactory;

    /**
     * Set default factory to use when building view delegates.
     *
     * @param factory factory to use.
     */
    public static void setDefault(IViewDelegateFactory factory) {
        ViewDelegateFactory.delegateFactory = factory;
    }

    /**
     * Build new view delegate.
     *
     * @param <View> type of view to build delegate for
     * @return view delegate
     */
    public static <View extends IView> IViewDelegate<View> buildViewDelegate() {
        Preconditions.checkState(delegateFactory != null, "No default factory set.");
        return delegateFactory.buildViewDelegate();
    }
}
