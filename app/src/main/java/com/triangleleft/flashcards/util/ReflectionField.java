package com.triangleleft.flashcards.util;

import java.lang.reflect.Field;

public class ReflectionField<T> {

    private final Field field;
    private final Object target;
    private final Class<T> clazz;

    public ReflectionField(Object target, String fieldName, Class<T> clazz) {
        this.target = target;
        this.clazz = clazz;
        for (Field targetField : target.getClass().getDeclaredFields()) {
            if (targetField.getName().equals(fieldName)) {
                field = targetField;
                field.setAccessible(true);
                return;
            }
        }
        throw new RuntimeException(fieldName + " not found in " + target);
    }

    public T getValue() {
        try {
            return clazz.cast(field.get(target));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
