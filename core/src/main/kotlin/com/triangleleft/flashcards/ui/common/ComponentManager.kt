package com.triangleleft.flashcards.ui.common

import com.triangleleft.flashcards.di.IComponent
import com.triangleleft.flashcards.di.scope.ApplicationScope
import org.slf4j.LoggerFactory
import java.util.*

@ApplicationScope
class ComponentManager {
    private val components: MutableMap<Class<*>, IComponent>

    init {
        components = HashMap<Class<*>, IComponent>()
    }

    @Suppress("UNCHECKED_CAST")
    fun <Component : IComponent> restoreComponent(clazz: Class<*>): Component? {
        logger.debug("restoreComponent() called with: clazz = [{}]", clazz)
        val component: IComponent? = components.remove(clazz)
        logger.debug("restoreComponent() returned: [{}]", component)
        return component as Component?
    }

    fun saveComponent(clazz: Class<*>, component: IComponent) {
        logger.debug("saveComponent() called with: clazz = [{}], component = [{}]", clazz, component)
        components.put(clazz, component)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(ComponentManager::class.java)

        fun buildDefault(): ComponentManager {
            return ComponentManager()
        }
    }


}