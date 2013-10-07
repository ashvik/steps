package com.step.core.chain;

import com.step.core.annotations.collector.StepDefinitionAnnotationDefinitionCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.utils.AnnotatedField;

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
    List<AnnotatedField> getDependenciesForStep(Class<?> stepClass);
    void addStep(StepDefinitionHolder stepDefinitionHolder);
    void addInterceptorStep(StepDefinitionHolder stepDefinitionHolder, boolean isPreStep);
}
