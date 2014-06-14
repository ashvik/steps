package com.step.core;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/18/13
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractResponseLessStep extends AbstractStepExecutionContextAware implements ResponseLessStep {
    protected <I> I getInput(Class<I> clazz){
        return getStepExecutionContext().getInput(clazz);
    }
}
