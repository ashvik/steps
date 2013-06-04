package com.step.core;

import com.step.core.context.StepContext;
import com.step.core.context.StepContextAware;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/26/13
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStepContextAware implements StepContextAware {
    private StepContext stepContext;

    public void setStepContext(StepContext context){
        this.stepContext = context;
    }

    public StepContext getStepContext(){
        return this.stepContext;
    }
}
