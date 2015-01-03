package com.step.core.interceptor.event.impl;

import com.step.core.chain.breaker.BreakDetails;
import com.step.core.conditions.BreakCondition;
import com.step.core.context.abstr.AbstractStepExecutionContextAware;
import com.step.core.interceptor.event.ExecutionDecisionEvent;
import com.step.core.interceptor.event.StepEventType;
import com.step.core.utils.StepExecutionUtil;

/**
 * Created by amishra on 1/2/15.
 */
public class BreakExecutionDecisionEvent extends AbstractStepExecutionContextAware implements ExecutionDecisionEvent<BreakDetails>{

    private String step;

    private BreakDetails breakDetails;

    public BreakExecutionDecisionEvent(BreakDetails breakDetails, String step){
        this.breakDetails = breakDetails;
        this.step = step;
    }

    @Override
    public boolean runEvent(ClassLoader classLoader) {
        BreakCondition condition = null;
        try {
            try {
                condition = (BreakCondition)StepExecutionUtil.loadClass(breakDetails.getConditionClass().getName(), classLoader).newInstance();
                condition.setStepExecutionContext(getStepExecutionContext());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return condition.check();
    }

    @Override
    public String getStep() {
        return this.step;
    }

    @Override
    public StepEventType getEventType() {
        return StepEventType.POST_EVENT;
    }

    public BreakDetails getDecisionDetails(){
        return breakDetails;
    }
}
