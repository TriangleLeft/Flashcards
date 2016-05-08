package com.triangleleft.flashcards.mvp.common.presenter;

import com.triangleleft.flashcards.mvp.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractPresenter<View extends IView>
        implements IPresenter<View> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);
    private View view;
    private View viewState;

    public AbstractPresenter() {
    }

    private Map<String, Map.Entry<Method, Object[]>> stateTypeCalls = new HashMap<>();
    private Map<Method, Object[]> stateCalls = new HashMap<>();
    private Map<Method, Object[]> statelessCalls = new HashMap<>();

    public AbstractPresenter(Class<View> viewClass) {
        viewState = viewClass.cast(Proxy.newProxyInstance(viewClass.getClassLoader(),
                new Class<?>[]{viewClass}, (proxy, method, args) -> {
                    View localView = view;
//                    boolean isStateless = method.isAnnotationPresent(Stateless.class);
//                    boolean isStateTyped = method.isAnnotationPresent(State.class);
//                    if (isStateless) {
                    if (localView != null) {
                        return method.invoke(localView, args);
                    } else {
                        statelessCalls.put(method, args);
                        return null;
                    }
//                    } else {
//                        if (isStateTyped) {
//                            String type = method.getAnnotation(State.class).value();
//                            stateTypeCalls.put(type, new AbstractMap.SimpleEntry<>(method, args));
//                        } else {
//                            stateCalls.put(method, args);
//                        }
//                        if (localView != null) {
//                            return method.invoke(localView, args);
//                        } else {
//                            return null;
//                        }
//                    }
                }));
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate() called");
    }

    @Override
    public void onBind(View view) {
        logger.debug("onBind() called with: view = [{}]", view);
        this.view = view;
        applyState(view, statelessCalls.entrySet());
    }

    @Override
    public void onRebind(View view) {
        logger.debug("onRebind() called with: view = [{}]", view);
        this.view = view;
        applyState(view, statelessCalls.entrySet());
    }

    private void applyState(View view, Collection<Map.Entry<Method, Object[]>> calls) {
        for (Map.Entry<Method, Object[]> call : calls) {
            try {
                call.getKey().invoke(view, call.getValue());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onUnbind() {
        logger.debug("onUnbind() called");
//        Preconditions.checkState(this.view != null, "Presenter is already unbound");
        view = null;
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy() called");
    }

    @Override
    public View getView() {
        return viewState;
    }

}
