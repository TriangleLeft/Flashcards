package com.triangleleft.flashcards.mvp.common.presenter;

import android.support.annotation.CallSuper;

import com.google.common.base.Preconditions;
import com.triangleleft.flashcards.mvp.common.view.IView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractPresenter<View extends IView>
        implements IPresenter<View> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);
  private volatile View view;
  private final View viewState;
  private boolean destroyed = false;

  private final List<Map.Entry<Method, Object[]>> viewCalls = new ArrayList<>();

  public AbstractPresenter(Class<View> viewClass) {
    viewState = viewClass.cast(Proxy.newProxyInstance(viewClass.getClassLoader(),
            new Class<?>[]{viewClass}, (proxy, method, args) -> {
              if (method.getName().equals("toString") && (args == null || args.length == 0)) {
                return "ProxyView view = [" + view + "]";
              }
              Preconditions.checkState(method.getReturnType().equals(Void.TYPE),
                      "Trying to invoke non-void view method: " + method);
              View localView = null;
              // Check whether we have view attached
              // Do it while holding lock so we don't miss bind/unbind
              synchronized (AbstractPresenter.this) {
                if (view != null) {
                  localView = view;
                } else if (!destroyed) {
                  // No view is bound, but we are not destroyed yet, remember this call
                  // Save call to be invoked when view is bound again
                  viewCalls.add(new AbstractMap.SimpleEntry<>(method, args));
                } else {
                  throw new RuntimeException("Wanted to invoke " + method + " when presenter was already destroyed");
                }
              }
              // We had view attached, invoke call immediately
              // TODO: we do it on the caller thread and we probably don't want to do that on android, right?
              if (localView != null) {
                invokeMethod(localView, method, args);
              }
              return null;
            }));
  }

  @Override
  public void onCreate() {
  }

  @CallSuper
  @Override
  public void onBind(View view) {
    logger.debug("onBind() called with: view = [{}]", view);
    this.view = view;
  }

  @CallSuper
  @Override
  public synchronized void onRebind(View view) {
    logger.debug("onRebind() called with: view = [{}]", view);
    this.view = view;
    applyState();
  }

  private void applyState() {
    for (Map.Entry<Method, Object[]> entry : viewCalls) {
      invokeMethod(view, entry.getKey(), entry.getValue());
    }
    viewCalls.clear();
  }

  private void invokeMethod(View target, Method method, Object[] args) {
    try {
      method.invoke(target, args);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @CallSuper
  @Override
  public void onUnbind() {
    logger.debug("onUnbind() called");
    //        Preconditions.checkState(this.view != null, "Presenter is already unbound");
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

}
