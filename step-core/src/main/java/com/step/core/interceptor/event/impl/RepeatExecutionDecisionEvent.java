package com.step.core.interceptor.event.impl;

import com.step.core.chain.repeater.RepeatDetails;
import com.step.core.conditions.RepeatBreakCondition;
import com.step.core.context.abstr.AbstractStepExecutionContextAware;
import com.step.core.interceptor.event.ExecutionDecisionEvent;
import com.step.core.interceptor.event.StepEventType;
import com.step.core.utils.StepExecutionUtil;

/**
 * Created by amishra on 1/2/15.
 */
public class RepeatExecutionDecisionEvent extends AbstractStepExecutionContextAware implements ExecutionDecisionEvent<RepeatDetails> {
    private String step;

    private RepeatDetails repeatDetails;

    public RepeatExecutionDecisionEvent(RepeatDetails repeatDetails, String step){
        this.repeatDetails = repeatDetails;
        this.step = step;
    }

    @Override
    public boolean runEvent(ClassLoader classLoader) {
        RepeatBreakCondition condition = null;
        try {
            try {
                condition = (RepeatBreakCondition) StepExecutionUtil.loadClass(repeatDetails.getConditionClass().getName(), classLoader).newInstance();
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
        return step;
    }

    @Override
    public StepEventType getEventType() {
        return StepEventType.POST_EVENT;
    }

    public RepeatDetails getDecisionDetails(){
        return repeatDetails;
    }
}
