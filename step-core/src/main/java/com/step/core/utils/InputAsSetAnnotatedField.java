package com.step.core.utils;

/**
 * Created by amishra on 1/9/15.
 */
public class InputAsSetAnnotatedField extends AnnotatedField{
    private Class<?> setOf;

    public InputAsSetAnnotatedField(Class<?> fieldClass, String fieldName, String annotatedName) {
        super(fieldClass, fieldName, annotatedName);
    }

    public Class<?> getSetOf() {
        return setOf;
    }

    public void setSetOf(Class<?> setOf) {
        this.setOf = setOf;
    }
}
