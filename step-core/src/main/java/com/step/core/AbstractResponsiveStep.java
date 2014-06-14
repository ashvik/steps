package com.step.core;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/18/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractResponsiveStep<T> extends AbstractStepExecutionContextAware implements ResponsiveStep<T> {
    protected <I> I getInput(Class<I> clazz){
        return getStepExecutionContext().getInput(clazz);
    }
}
