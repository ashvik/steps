package com.step.core.context;

import com.step.core.Attributes;
import com.step.core.PluginRequest;
import com.step.core.executor.StepExecutorProvider;
import com.step.core.factory.ObjectFactory;
import com.step.core.io.ExecutionResult;
import com.step.core.io.StepInput;
import com.step.core.parameter.RequestParameterContainer;
import com.step.core.repository.StepRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/18/13
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepExecutionContext {
    void put(String name, Object obj);
    Object get(String name);
    void setStepInput(StepInput input);
    StepInput getStepInput();
    <T> T getInput(Class<T> inputClass);
    <T> T getDependency(Class<T> dependency);
    Object getDependency(String dependency);
    void setObjectFactory(ObjectFactory factory);
    Object getAttribute(String name);
    Attributes getAttributes();
    void putAttribute(String name, Object value);
    void breakStepChainExecution();
    boolean hasStepChainExecutionBroken();
    void setApplicablePluginRequest(List<PluginRequest> applicablePluginRequest);
    List<PluginRequest> getPluginRequests();
    ExecutionResult applyPluginRequest(String request, Object... input) throws Exception;
    ExecutionResult applyPluginRequest(String request, boolean allowInputTransfer, boolean applyGenericSteps, Object... input) throws Exception;
    void setStepExecutorProvider(StepExecutorProvider stepExecutorProvider);
    void setStepRepository(StepRepository stepRepository);
    void setRequestParameterContainer(RequestParameterContainer requestParameterContainer);
    RequestParameterContainer getRequestParameterContainer();
    void setClassLoader(ClassLoader classLoader);
    ClassLoader getClassLoader();
}
