package com.step.core.interceptor.event;

import com.step.core.context.StepExecutionContextAware;

/**
 * Created by amishra on 1/2/15.
 */
public interface StepEvent extends StepExecutionContextAware{
    String getStep();
    StepEventType getEventType();
}
