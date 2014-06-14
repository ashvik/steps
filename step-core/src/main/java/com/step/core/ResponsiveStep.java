package com.step.core;

import com.step.core.context.StepContextAware;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/18/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ResponsiveStep<T> extends StepContextAware {
    T execute() throws Exception;
}
