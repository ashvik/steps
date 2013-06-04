package com.step.core;

import com.step.core.context.StepContextAware;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/19/13
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ResponseLessStep extends StepContextAware {
    void execute();
}
