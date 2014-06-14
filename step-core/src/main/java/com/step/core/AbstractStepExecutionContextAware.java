package com.step.core;

import com.step.core.context.StepExecutionContext;
import com.step.core.context.StepExecutionContextAware;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/26/13
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStepExecutionContextAware implements StepExecutionContextAware {
    private StepExecutionContext stepExecutionContext;

    public void setStepExecutionContext(StepExecutionContext context){
        this.stepExecutionContext = context;
    }

    public StepExecutionContext getStepExecutionContext(){
        return this.stepExecutionContext;
    }
}
