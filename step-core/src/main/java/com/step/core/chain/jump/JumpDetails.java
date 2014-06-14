package com.step.core.chain.jump;

/**
 * Created by amishra on 6/13/14.
 */
public class JumpDetails {
    private Class<?> conditionClass;
    private String onSuccessJumpStep;
    private String onFailureJumpStep;

    public Class<?> getConditionClass() {
        return conditionClass;
    }

    public void setConditionClass(Class<?> conditionClass) {
        this.conditionClass = conditionClass;
    }

    public String getOnSuccessJumpStep() {
        return onSuccessJumpStep;
    }

    public void setOnSuccessJumpStep(String onSuccessJumpStep) {
        this.onSuccessJumpStep = onSuccessJumpStep;
    }

    public String getOnFailureJumpStep() {
        return onFailureJumpStep;
    }

    public void setOnFailureJumpStep(String onFailureJumpStep) {
        this.onFailureJumpStep = onFailureJumpStep;
    }
}
