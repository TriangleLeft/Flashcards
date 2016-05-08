package com.triangleleft.flashcards.mvp.common.presenter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.triangleleft.flashcards.mvp.common.di.component.IComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ComponentManager {

    private static final Logger logger = LoggerFactory.getLogger(ComponentManager.class);

    private final AtomicLong currentId;

    private final Cache<Long, IComponent> components;

    public ComponentManager(long maxSize, long expirationValue, TimeUnit expirationUnit) {
        currentId = new AtomicLong();

        components = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expirationValue, expirationUnit)
                .build();
    }

    public <P extends IComponent> P restoreComponent(long componentId) {
        logger.debug("restoreComponent() called with: componentId = [{}]", componentId);
        P component = (P) components.getIfPresent(componentId);
        components.invalidate(componentId);
        logger.debug("restoreComponent() returned: [{}]", component);
        return component;
    }

    public long saveComponent(IComponent component) {
        logger.debug("saveComponent() called with: component = [{}]", component);
        long componentId = currentId.incrementAndGet();
        components.put(componentId, component);
        logger.debug("saveComponent() returned: [{}]", componentId);
        return componentId;
    }
}