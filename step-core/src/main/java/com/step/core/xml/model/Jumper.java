package com.step.core.xml.model;

/**
 * Created by amishra on 6/13/14.
 *
 */
public class Jumper {
    private String request;
    private String forStep;
    private String conditionClass;
    private String onSuccessJumpTo;
    private String onFailureJumpTo;

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

    public String getOnFailureJumpTo() {
        return onFailureJumpTo;
    }

    public void setOnFailureJumpTo(String onFailureJumpTo) {
        this.onFailureJumpTo = onFailureJumpTo;
    }

    public String getOnSuccessJumpTo() {
        return onSuccessJumpTo;
    }

    public void setOnSuccessJumpTo(String onSuccessJumpTo) {
        this.onSuccessJumpTo = onSuccessJumpTo;
    }

    public String getConditionClass() {
        return conditionClass;
    }

    public void setConditionClass(String conditionClass) {
        this.conditionClass = conditionClass;
    }
}
