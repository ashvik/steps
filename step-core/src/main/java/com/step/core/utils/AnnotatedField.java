package com.step.core.utils;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/18/13
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnnotatedField {
    private Class<?> fieldClass;
    private String annotatedName;
    private String fieldName;

    public AnnotatedField(Class<?> fieldClass, String fieldName, String annotatedName){
        this.fieldClass = fieldClass;
        this.fieldName = fieldName;
        this.annotatedName = annotatedName;
    }

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public String getAnnotatedName() {
        return annotatedName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
