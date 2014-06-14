package com.step.core.xml.model;

/**
 * Created by amishra on 6/14/14.
 */
public class Repeater {
    private String request;
    private String forStep;
    private String conditionClass;
    private String repeatFromStep;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRepeatFromStep() {
        return repeatFromStep;
    }

    public void setRepeatFromStep(String repeatFromStep) {
        this.repeatFromStep = repeatFromStep;
    }

    public String getConditionClass() {
        return conditionClass;
    }

    public void setConditionClass(String conditionClass) {
        this.conditionClass = conditionClass;
    }

    public String getForStep() {
        return forStep;
    }

    public void setForStep(String forStep) {
        this.forStep = forStep;
    }
}
