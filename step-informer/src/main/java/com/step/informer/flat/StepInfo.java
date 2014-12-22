package com.step.informer.flat;

import com.step.informer.flat.visitor.FlatInfoVisitor;

/**
 * Created by amishra on 6/21/14.
 */
public class StepInfo implements FlatInfo{
    private String stepName;
    private String nextStep;
    private String interceptorType;
    private String description;

    public StepInfo(){}

    public StepInfo(String stepName){
        this.stepName = stepName;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void accept(FlatInfoVisitor visitor) {
        visitor.visitStepInfo(this);
    }

    public String getInterceptorType() {
        return interceptorType;
    }

    public void setInterceptorType(String interceptorType) {
        this.interceptorType = interceptorType;
    }
}
