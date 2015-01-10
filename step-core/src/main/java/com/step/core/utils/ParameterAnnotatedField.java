package com.step.core.utils;

import com.step.core.enums.ParameterValueType;

/**
 * Created by amishra on 1/9/15.
 */
public class ParameterAnnotatedField extends AnnotatedField{
    private String name;

    private ParameterValueType parameterValueType;

    public ParameterAnnotatedField(Class<?> fieldClass, String fieldName, String annotatedName) {
        super(fieldClass, fieldName, annotatedName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParameterValueType getParameterValueType() {
        return parameterValueType;
    }

    public void setParameterValueType(ParameterValueType parameterValueType) {
        this.parameterValueType = parameterValueType;
    }
}
