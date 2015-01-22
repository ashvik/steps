package com.step.core.chain;

import com.step.core.chain.impl.BasicStepChain;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.exceptions.handler.StepExceptionHandler;
import com.step.core.parameter.RequestParameterContainer;
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
    void addStep(StepDefinitionHolder stepDefinitionHolder, String request);
    void addInterceptorStep(StepDefinitionHolder stepDefinitionHolder, String request, boolean isPreStep);
    String getStepName(Class<?> stepClass);
    BasicStepChain.StepNode getRootNode();
    BasicStepChain.StepNode getStepNodeByName(String name);
    void setRequestParameterContainer(RequestParameterContainer requestParameterContainer);
    RequestParameterContainer getRequestParameterContainer();
    String getExpectedOutComeClass();
    void setExpectedOutCome(String expectedOutCome);
    Class<StepExceptionHandler> getStepExceptionHandler();
    void setStepExceptionHandler(String stepExceptionHandler);
    void setInputTypes(List<String> inputTypes);
    List<String> getInputType();
    List<AnnotatedField> getAnnotatedPluginsForStep(Class<?> stepClass);
    StepDefinitionHolder getInterceptorStepDefinition(Class<?> preStep);
}
