package com.step.informer.flat;

import com.step.informer.flat.visitor.FlatInfoVisitor;

/**
 * Created by amishra on 6/21/14.
 */
public class BreakInfo implements FlatInfo{
    private String breakStep;
    private ConditionInfo condition;

    public String getBreakStep() {
        return breakStep;
    }

    public void setBreakStep(String breakStep) {
        this.breakStep = breakStep;
    }

    public ConditionInfo getCondition() {
        return condition;
    }

    public void setCondition(ConditionInfo condition) {
        this.condition = condition;
    }

    @Override
    public void accept(FlatInfoVisitor visitor) {
        visitor.visitBreakInfo(this);
    }
}
