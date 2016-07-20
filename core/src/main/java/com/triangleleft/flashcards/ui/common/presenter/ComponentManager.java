package com.triangleleft.flashcards.ui.common.presenter;

import android.support.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.triangleleft.flashcards.ui.common.di.component.IComponent;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@FunctionsAreNonnullByDefault
public class ComponentManager {

    private static final Logger logger = LoggerFactory.getLogger(ComponentManager.class);
    private final Cache<Class, IComponent> components;

    public ComponentManager(long maxSize, long expirationValue, TimeUnit expirationUnit) {
        components = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expirationValue, expirationUnit)
                .build();
    }

    @Nullable
    public <Component extends IComponent> Component restoreComponent(Class clazz) {
        logger.debug("restoreComponent() called with: clazz = [{}]", clazz);
        Component component = (Component) components.getIfPresent(clazz);
        components.invalidate(clazz);
        logger.debug("restoreComponent() returned: [{}]", component);
        return component;
    }

    public void saveComponent(Class clazz, IComponent component) {
        logger.debug("saveComponent() called with: clazz = [{}], component = [{}]", clazz, component);
        components.put(clazz, component);
    }


}