package com.step.core.chain.repeater;

/**
 * Created by amishra on 6/14/14.
 */
public class RepeatDetails {
    private Class<?> conditionClass;
    private String repeatFromStep;

    public Class<?> getConditionClass() {
        return conditionClass;
    }

    public void setConditionClass(Class<?> conditionClass) {
        this.conditionClass = conditionClass;
    }

    public String getRepeatFromStep() {
        return repeatFromStep;
    }

    public void setRepeatFromStep(String repeatFromStep) {
        this.repeatFromStep = repeatFromStep;
    }
}
