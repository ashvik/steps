package com.step.core.context.abstr;

import com.step.core.context.StepContext;
import com.step.core.context.StepContextAware;

/**
 * Created by amishra on 6/14/14.
 */
public abstract class AbstractStepContextAware implements StepContextAware{
    private StepContext context;

    public void setStepContext(StepContext context){
        this.context = context;
    }

    public StepContext getStepContext(){
        return this.context;
    }
}
