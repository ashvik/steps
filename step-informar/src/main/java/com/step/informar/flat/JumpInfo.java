package com.step.informar.flat;

import com.step.informar.flat.visitor.FlatInfoVisitor;

/**
 * Created by amishra on 6/21/14.
 */
public class JumpInfo implements FlatInfo{
    private String fromStep, onSuccess="", onFailure="";
    private ConditionInfo condition;

    public String getFromStep() {
        return fromStep;
    }

    public void setFromStep(String fromStep) {
        this.fromStep = fromStep;
    }

    public ConditionInfo getCondition() {
        return condition;
    }

    public void setCondition(ConditionInfo condition) {
        this.condition = condition;
    }

    public String getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(String onFailure) {
        this.onFailure = onFailure;
    }

    public String getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(String onSuccess) {
        this.onSuccess = onSuccess;
    }

    @Override
    public void accept(FlatInfoVisitor visitor) {
        visitor.visitJumpInfo(this);
    }
}