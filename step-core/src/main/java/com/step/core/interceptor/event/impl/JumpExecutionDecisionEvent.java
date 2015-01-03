package com.step.core.interceptor.event.impl;

import com.step.core.chain.jump.JumpDetails;
import com.step.core.conditions.JumpCondition;
import com.step.core.context.abstr.AbstractStepExecutionContextAware;
import com.step.core.interceptor.event.ExecutionDecisionEvent;
import com.step.core.interceptor.event.StepEventType;
import com.step.core.utils.StepExecutionUtil;

/**
 * Created by amishra on 1/2/15.
 */
public class JumpExecutionDecisionEvent extends AbstractStepExecutionContextAware implements ExecutionDecisionEvent<JumpDetails> {
    private String step;

    private JumpDetails jumpDetails;

    public JumpExecutionDecisionEvent(JumpDetails jumpDetails, String step){
        this.jumpDetails = jumpDetails;
        this.step = step;
    }

    @Override
    public boolean runEvent(ClassLoader classLoader) {
        JumpCondition condition = null;
        try {
            try {
                condition = (JumpCondition) StepExecutionUtil.loadClass(jumpDetails.getConditionClass().getName(), classLoader).newInstance();
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

    public JumpDetails getDecisionDetails(){
        return jumpDetails;
    }
}
