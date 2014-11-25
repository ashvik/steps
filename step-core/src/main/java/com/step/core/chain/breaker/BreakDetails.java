package com.step.core.chain.breaker;

/**
 * Created by amishra on 6/14/14.
 */
public class BreakDetails{
    private Class<?> conditionClass;

    public Class<?> getConditionClass() {
        return conditionClass;
    }

    public void setConditionClass(Class<?> conditionClass) {
        this.conditionClass = conditionClass;
    }
}
