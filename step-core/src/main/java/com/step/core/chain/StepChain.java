package com.step.core.chain;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/18/13
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepChain {
    List<Class<?>> getPreSteps();
    List<Class<?>> getPostStep();
    List<Class<?>> getSteps();
    void addStep(Class<?> stepClass);
    void addInterceptorStep(Class<?> stepClass, boolean isPreStep);
}
