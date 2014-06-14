package com.step.core.context.abstr;

import com.step.core.context.StepExecutionContext;
import com.step.core.context.StepExecutionContextAware;

/**
 * Created by amishra on 6/14/14.
 */
public abstract class AbstractStepExecutionContextAware implements StepExecutionContextAware {
    private StepExecutionContext context;

    public void setStepExecutionContext(StepExecutionContext context){
        this.context = context;
    }

    public StepExecutionContext getStepExecutionContext(){
        return this.context;
    }
}
