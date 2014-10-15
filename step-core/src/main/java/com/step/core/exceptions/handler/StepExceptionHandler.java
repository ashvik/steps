package com.step.core.exceptions.handler;

import com.step.core.context.StepExecutionContextAware;

/**
 * Created by amishra on 10/3/14.
 */
public interface StepExceptionHandler extends StepExecutionContextAware{
    void handleException(Exception e);
}
