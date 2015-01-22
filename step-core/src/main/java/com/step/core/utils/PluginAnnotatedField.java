package com.step.core.utils;

/**
 * Created by amishra on 1/12/15.
 */
public class PluginAnnotatedField extends AnnotatedField{
    private String request;

    private boolean applyGenericSteps;

    private boolean passCurrentInputs;

    public PluginAnnotatedField(Class<?> fieldClass, String fieldName, String annotatedName) {
        super(fieldClass, fieldName, annotatedName);
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public boolean isApplyGenericSteps() {
        return applyGenericSteps;
    }

    public void setApplyGenericSteps(boolean applyGenericSteps) {
        this.applyGenericSteps = applyGenericSteps;
    }

    public boolean isPassCurrentInputs() {
        return passCurrentInputs;
    }

    public void setPassCurrentInputs(boolean passCurrentInputs) {
        this.passCurrentInputs = passCurrentInputs;
    }
}
