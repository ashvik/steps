package com.step.core.exceptions.handler.abstr;

import com.step.core.context.StepExecutionContext;
import com.step.core.exceptions.handler.StepExceptionHandler;

/**
 * Created by amishra on 10/3/14.
 */
public abstract class AbstractStepExceptionHandler implements StepExceptionHandler {
    private StepExecutionContext stepExecutionContext;

    public void setStepExecutionContext(StepExecutionContext context){
        this.stepExecutionContext = context;
    }

    public StepExecutionContext getStepExecutionContext(){
        return stepExecutionContext;
    }
}
