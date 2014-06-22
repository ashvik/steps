package com.step.informar.flat;

import com.step.informar.flat.visitor.FlatInfoVisitor;

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

    /*public BreakInfo(StepInfo breakStep, ConditionInfo condition){
        this.breakStep = breakStep;
        this.condition = condition;
    }

    public StepInfo getBreakStep() {
        return breakStep;
    }

    public ConditionInfo getCondition() {
        return condition;
    }*/
}
