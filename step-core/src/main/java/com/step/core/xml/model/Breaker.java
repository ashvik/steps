package com.step.core.xml.model;

/**
 * Created by amishra on 6/14/14.
 */
public class Breaker {
    private String request;
    private String forStep;
    private String conditionClass;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getForStep() {
        return forStep;
    }

    public void setForStep(String forStep) {
        this.forStep = forStep;
    }

    public String getConditionClass() {
        return conditionClass;
    }

    public void setConditionClass(String conditionClass) {
        this.conditionClass = conditionClass;
    }
}
