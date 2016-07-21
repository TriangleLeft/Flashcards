package com.triangleleft.flashcards.ui.common.presenter;

import com.annimon.stream.Optional;
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
    public static final int MAX_SIZE = 20;
    public static final int EXPIRATION_MINUTES = 1;
    private final Cache<Class, IComponent> components;

    public static ComponentManager buildDefault() {
        return new ComponentManager(MAX_SIZE, EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    private ComponentManager(long maxSize, long expirationValue, TimeUnit expirationUnit) {
        components = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expirationValue, expirationUnit)
                .build();
    }

    public <Component extends IComponent> Optional<Component> restoreComponent(Class clazz) {
        logger.debug("restoreComponent() called with: clazz = [{}]", clazz);
        @SuppressWarnings("unchecked")
        Component component = (Component) components.getIfPresent(clazz);
        components.invalidate(clazz);
        logger.debug("restoreComponent() returned: [{}]", component);
        return Optional.ofNullable(component);
    }

    public void saveComponent(Class clazz, IComponent component) {
        logger.debug("saveComponent() called with: clazz = [{}], component = [{}]", clazz, component);
        components.put(clazz, component);
    }


}