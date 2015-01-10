package com.step.core.utils;

/**
 * Created by amishra on 1/9/15.
 */
public class InputAsListAnnotatedField extends AnnotatedField{
    private Class<?> listOf;

    public InputAsListAnnotatedField(Class<?> fieldClass, String fieldName, String annotatedName) {
        super(fieldClass, fieldName, annotatedName);
    }

    public Class<?> getListOf() {
        return listOf;
    }

    public void setListOf(Class<?> listOf) {
        this.listOf = listOf;
    }
}
