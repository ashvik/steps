package com.step.core.chain;

import com.step.core.PluginRequest;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
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
    List<AnnotatedField> getDependenciesForStep(Class<?> stepClass);
    void addStep(StepDefinitionHolder stepDefinitionHolder, String request);
    void addInterceptorStep(StepDefinitionHolder stepDefinitionHolder, String request, boolean isPreStep);
    String getStepName(Class<?> stepClass);
    List<Class<?>> getJumpConditionClassForStep(Class<?> step);
    List<JumpDetails> getJumpDetailsForStep(Class<?> step);
    List<Class<?>> getBreakConditionClassForStep(Class<?> step);
    BasicStepChain.StepNode getRootNode();
    BasicStepChain.StepNode getStepNodeByName(String name);
    Class<?> getRepeatBreakConditionClassForStep(Class<?> step);
    RepeatDetails getRepeatDetailsForStep(Class<?> step);
    void setRequestParameterContainer(RequestParameterContainer requestParameterContainer);
    RequestParameterContainer getRequestParameterContainer();
    Class<?> getExpectedOutComeClass();
    void setExpectedOutCome(String expectedOutCome);
    Class<StepExceptionHandler> getStepExceptionHandler();
    void setStepExceptionHandler(String stepExceptionHandler);
    void setInputTypes(List<String> inputTypes);
    List<Class> getInputType();
    void setPluginRequests(List<PluginRequest> pluginRequests);
    List<PluginRequest> getPluginRequests();
}
