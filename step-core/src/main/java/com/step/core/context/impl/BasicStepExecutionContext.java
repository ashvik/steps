package com.step.core.context.impl;

import com.step.core.Attributes;
import com.step.core.PluginRequest;
import com.step.core.chain.StepChain;
import com.step.core.container.StepExecutionContainer;
import com.step.core.container.impl.DefaultStepExecutionContainer;
import com.step.core.context.StepExecutionContext;
import com.step.core.executor.StepExecutorProvider;
import com.step.core.factory.ObjectFactory;
import com.step.core.io.ExecutionResult;
import com.step.core.io.StepInput;
import com.step.core.parameter.RequestParameterContainer;
import com.step.core.repository.StepRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/25/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicStepExecutionContext implements StepExecutionContext {
    private Map<String, Object> cache = new HashMap<String, Object>();
    private StepInput input;
    private ObjectFactory objectFactory;
    private Attributes attributes = new Attributes();
    private boolean breakStepChain;
    private StepExecutorProvider stepExecutorProvider;
    private StepRepository stepRepository;
    private List<PluginRequest> applicablePluginRequestNew = new ArrayList<PluginRequest>();
    private StepExecutionContainer stepExecutionContainer = new LocalStepExecutionContainer();
    private RequestParameterContainer requestParameterContainer;
    private ClassLoader classLoader;

    @Override
    public void put(String name, Object obj) {
        this.cache.put(name, obj);
    }

    @Override
    public Object get(String name) {
        return this.cache.get(name);
    }

    @Override
    public void setStepInput(StepInput input) {
        this.input = input;
    }

    @Override
    public StepInput getStepInput() {
        return this.input;
    }

    public <T> T getInput(Class<T> inputClass){
        return (T)this.input.getInput(inputClass);
    }

    @Override
    public <T> T getDependency(Class<T> dependency) {
        return this.objectFactory.fetch(dependency);
    }

    @Override
    public Object getDependency(String dependency) {
        return this.objectFactory.fetch(dependency);
    }

    @Override
    public void setObjectFactory(ObjectFactory factory) {
        this.objectFactory = factory;
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.getAttribute(name);
    }

    @Override
    public Attributes getAttributes() {
        return this.attributes;
    }

    @Override
    public void putAttribute(String name, Object value) {
        this.attributes.addAttribute(name, value);
    }

    @Override
    public void breakStepChainExecution() {
        breakStepChain = true;
    }

    @Override
    public boolean hasStepChainExecutionBroken() {
        return breakStepChain;
    }

    @Override
    public void setApplicablePluginRequest(List<PluginRequest> applicablePluginRequest) {
        this.applicablePluginRequestNew = applicablePluginRequest;
    }

    @Override
    public ExecutionResult applyPluginRequest(String request, Object... input) throws Exception{
        for(PluginRequest pluginRequest : applicablePluginRequestNew){
            if(pluginRequest.getRequest().equals(request)){
                return stepExecutionContainer.submit(request, input);
            }
        }

        throw new IllegalStateException("Request can not apply plugin request '"+request+"', make sure it is configured.");
    }

    @Override
    public ExecutionResult applyPluginRequest(String request, boolean allowInputTransfer, boolean applyGenericSteps, Object... input) throws Exception{
        LocalStepInput localStepInput = new LocalStepInput(request);
        localStepInput.setAllowInputTransfer(allowInputTransfer);
        localStepInput.setApplyGenericSteps(applyGenericSteps);

        for(Object in : input){
            localStepInput.setInput(in);
        }

        for(PluginRequest pluginRequest : applicablePluginRequestNew){
            if(pluginRequest.getRequest().equals(request)){
                return stepExecutionContainer.submit(localStepInput);
            }
        }

        throw new IllegalStateException("Request can not apply plugin request '"+request+"', make sure it is configured.");
    }

    @Override
    public void setStepExecutorProvider(StepExecutorProvider stepExecutorProvider) {
        this.stepExecutorProvider = stepExecutorProvider;
    }

    @Override
    public void setStepRepository(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public RequestParameterContainer getRequestParameterContainer() {
        return requestParameterContainer;
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public List<PluginRequest> getPluginRequests() {
        return applicablePluginRequestNew;
    }

    @Override
    public void setRequestParameterContainer(RequestParameterContainer requestParameterContainer) {
        this.requestParameterContainer = requestParameterContainer;
    }

    private class LocalStepExecutionContainer extends DefaultStepExecutionContainer {
        @Override
        public ExecutionResult submit(StepInput input) throws Exception {
            StepExecutionContext context = new BasicStepExecutionContext();
            context.setClassLoader(input.getClassLoader());
            if(input instanceof LocalStepInput){
                return submitLocalStepInput((LocalStepInput)input, context);
            }else{
                input.fromExternalInput(BasicStepExecutionContext.this.input);
                context.setStepInput(input);
                return submitInternal(input, context, false);
            }
        }

        private ExecutionResult submitLocalStepInput(LocalStepInput input, StepExecutionContext context) throws Exception{
            if(input.isAllowInputTransfer()){
                input.fromExternalInput(BasicStepExecutionContext.this.input);
            }

            context.setStepInput(input);
            if(input.isApplyGenericSteps()){
                return submitInternal(input, context, true);
            }else{
                return submitInternal(input, context, false);
            }
        }

        private ExecutionResult submitInternal(StepInput input, StepExecutionContext context, boolean allowGenericSteps) throws Exception {
            context.setObjectFactory(objectFactory);
            context.setStepRepository(stepRepository);
            context.setStepExecutorProvider(stepExecutorProvider);

            StepChain chain = stepRepository.getStepExecutionChainForRequestUsingGenericStepsFlag(input.getRequest(), allowGenericSteps);
            context.setApplicablePluginRequest(chain.getPluginRequests());
            context.setRequestParameterContainer(chain.getRequestParameterContainer());

            return submit(context, chain, stepExecutorProvider);
        }
    }

    private class LocalStepInput extends StepInput{
        private boolean allowInputTransfer;

        private boolean applyGenericSteps;

        public LocalStepInput(String request) {
            super(request);
        }

        public boolean isAllowInputTransfer() {
            return allowInputTransfer;
        }

        public void setAllowInputTransfer(boolean allowInputTransfer) {
            this.allowInputTransfer = allowInputTransfer;
        }

        public boolean isApplyGenericSteps() {
            return applyGenericSteps;
        }

        public void setApplyGenericSteps(boolean applyGenericSteps) {
            this.applyGenericSteps = applyGenericSteps;
        }
    }
}
