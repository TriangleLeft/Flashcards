package com.triangleleft.flashcards.ui.common.presenter;

import com.triangleleft.flashcards.ui.common.view.IView;
import com.triangleleft.flashcards.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public abstract class AbstractPresenter<View extends IView>
        implements IPresenter<View> {

    public static final String VIEW_EXECUTOR = "viewExecutor";

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);
    private final View viewState;
    private final List<Map.Entry<Method, Object[]>> viewCalls = new ArrayList<>();
    private final Executor executor;
    private volatile View view;
    private boolean destroyed = false;
    private State<View> currentState;

    public AbstractPresenter(Class<View> viewClass, Executor executor) {
        this.executor = executor;
        viewState = viewClass.cast(Proxy.newProxyInstance(viewClass.getClassLoader(),
                new Class<?>[]{viewClass}, (proxy, method, args) -> {
                    if (method.getName().equals("toString") && (args == null || args.length == 0)) {
                        return "ProxyView view = [" + view + "]";
                    }
                    Utils.checkState(method.getReturnType().equals(Void.TYPE),
                            "Trying to invoke non-void view method: " + method);
                    View localView = view;
                    if (localView != null) {
                        invokeMethod(localView, method, args);
                    } else if (!destroyed) {
                        // No view is bound, but we are not destroyed yet, remember this call
                        // Save call to be invoked when view is bound again
                        viewCalls.add(new AbstractMap.SimpleEntry<>(method, args));
                    } else {
                        throw new RuntimeException(
                                "Wanted to invoke " + method + " when presenter was already destroyed");
                    }
                    return null;
                }));
    }

    @Override
    public void onCreate() {
    }

    @UiThread
    @CallSuper
    @Override
    public void onBind(View view) {
        logger.debug("onBind() called with: view = [{}]", view);
        this.view = view;
        if (currentState != null) {
            currentState.apply(view);
        }
    }

    @UiThread
    @CallSuper
    @Override
    // FIXME: synchronized is not actually synchronized with anything, why?
    public synchronized void onRebind(View view) {
        logger.debug("onRebind() called with: view = [{}]", view);
        this.view = view;
        for (Map.Entry<Method, Object[]> entry : viewCalls) {
            invokeMethod(view, entry.getKey(), entry.getValue());
        }
        viewCalls.clear();
    }

    private void invokeMethod(View target, Method method, Object[] args) {
        executor.execute(() -> {
            try {
                method.invoke(target, args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @CallSuper
    @Override
    public void onUnbind() {
        logger.debug("onUnbind() called");
        view = null;
    }

    @Override
    public void onDestroy() {
        destroyed = true;
    }

    @Override
    public View getView() {
        return viewState;
    }

    protected void applyState(State<View> state) {
        currentState = state;
        View localView = view;
        // Apply state only if we have view bound
        // Otherwise it would get called when view is bound
        if (localView != null) {
            executor.execute(() -> {
                currentState.apply(localView);
            });
        }
    }

    protected interface State<View> {
        void apply(View view);
    }

}
