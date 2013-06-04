package com.step.core.context;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/19/13
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface StepContextAware {
    void setStepContext(StepContext context);
    StepContext getStepContext();
}
