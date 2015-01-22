package com.step.core.context.impl;

import com.step.core.Attributes;
import com.step.core.chain.StepChain;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.context.StepExecutionContext;
import com.step.core.executor.StepExecutorProvider;
import com.step.core.factory.ObjectFactory;
import com.step.core.interceptor.event.ExecutionDecisionEvent;
import com.step.core.interceptor.event.PluginEvent;
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
    private LocalStepExecutionContainer stepExecutionContainer = new LocalStepExecutionContainer();
    private RequestParameterContainer requestParameterContainer;
    private ClassLoader classLoader;
    private StepChain stepChain;
    private List<ExecutionDecisionEvent<BreakDetails>> breakExecutionDecisionEvents = new ArrayList<ExecutionDecisionEvent<BreakDetails>>();
    private List<ExecutionDecisionEvent<JumpDetails>> jumpExecutionDecisionEvents = new ArrayList<ExecutionDecisionEvent<JumpDetails>>();
    private List<ExecutionDecisionEvent<RepeatDetails>> repeatExecutionDecisionEvents = new ArrayList<ExecutionDecisionEvent<RepeatDetails>>();
    private List<PluginEvent> autoPluginEvents = new ArrayList<PluginEvent>();

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
    public ExecutionResult applyPluginRequest(String request, Object... input) throws Exception{
        String aliasRequest = stepRepository.getRequestForAlias(request);
        request = aliasRequest != null ? aliasRequest : request;
        return applyPluginRequest(request, true, false, input);
    }

    @Override
    public ExecutionResult applyPluginRequest(String request, boolean allowInputTransfer, boolean applyGenericSteps, Object... input) throws Exception{
        LocalStepInput localStepInput = createLocalStepInput(request, allowInputTransfer, applyGenericSteps, input);
        String aliasRequest = stepRepository.getRequestForAlias(request);
        localStepInput = aliasRequest != null ? createLocalStepInput(aliasRequest, allowInputTransfer, applyGenericSteps, input) : localStepInput;
        return stepExecutionContainer.submit(localStepInput);
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
    public List<ExecutionDecisionEvent<BreakDetails>> getBreakExecutionDecisionEvents() {
        return breakExecutionDecisionEvents;
    }

    @Override
    public List<ExecutionDecisionEvent<JumpDetails>> getJumpExecutionDecisionEvents() {
        return jumpExecutionDecisionEvents;
    }

    @Override
    public List<ExecutionDecisionEvent<RepeatDetails>> getRepeatExecutionDecisionEvents() {
        return repeatExecutionDecisionEvents;
    }

    @Override
    public void setBreakExecutionDecisionEvents(List<ExecutionDecisionEvent<BreakDetails>> breakExecutionDecisionEvents) {
        for(ExecutionDecisionEvent<BreakDetails> breakExecutionDecisionEvent : breakExecutionDecisionEvents){
            breakExecutionDecisionEvent.setStepExecutionContext(this);
            this.breakExecutionDecisionEvents.add(breakExecutionDecisionEvent);
        }
    }

    @Override
    public void setJumpExecutionDecisionEvents(List<ExecutionDecisionEvent<JumpDetails>> jumpExecutionDecisionEvents) {
        for(ExecutionDecisionEvent<JumpDetails> jumpExecutionDecisionEvent : jumpExecutionDecisionEvents){
            jumpExecutionDecisionEvent.setStepExecutionContext(this);
            this.jumpExecutionDecisionEvents.add(jumpExecutionDecisionEvent);
        }
    }

    @Override
    public void setRepeatExecutionDecisionEvents(List<ExecutionDecisionEvent<RepeatDetails>> repeatExecutionDecisionEvents) {
        for(ExecutionDecisionEvent<RepeatDetails> repeatExecutionDecisionEvent : repeatExecutionDecisionEvents){
            repeatExecutionDecisionEvent.setStepExecutionContext(this);
            this.repeatExecutionDecisionEvents.add(repeatExecutionDecisionEvent);
        }
    }

    @Override
    public List<PluginEvent> getAutomatedPluginEvent() {
        return autoPluginEvents;
    }

    @Override
    public void setAutomatedPluginEvent(List<PluginEvent> automatedPluginEvent) {
        for(PluginEvent pluginEvent : automatedPluginEvent){
            pluginEvent.setStepExecutionContext(this);
            this.autoPluginEvents.add(pluginEvent);
        }
    }

    @Override
    public void setStepChain(StepChain stepChain) {
      this.stepChain = stepChain;
    }

    @Override
    public StepChain getStepChain() {
        return this.stepChain;
    }

    @Override
    public void setRequestParameterContainer(RequestParameterContainer requestParameterContainer) {
        this.requestParameterContainer = requestParameterContainer;
    }

    private LocalStepInput createLocalStepInput(String request, boolean allowInputTransfer, boolean applyGenericSteps, Object... input){
        LocalStepInput localStepInput = new LocalStepInput(request);
        localStepInput.setAllowInputTransfer(allowInputTransfer);
        localStepInput.setApplyGenericSteps(applyGenericSteps);

        for(Object in : input){
            localStepInput.setInput(in);
        }

        return localStepInput;
    }

    private class LocalStepExecutionContainer {
        public ExecutionResult submit(StepInput input) throws Exception {
            StepExecutionContext context = new BasicStepExecutionContext();
            context.setClassLoader(classLoader);
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
            MappedRequestDetailsHolder mappedRequestDetailsHolder = stepRepository.getMappedRequestDetails(input.getRequest());
            context.setStepChain(chain);
            context.setRequestParameterContainer(chain.getRequestParameterContainer());
            context.setBreakExecutionDecisionEvents(mappedRequestDetailsHolder.getBreakExecutionDecisionEvents());
            context.setJumpExecutionDecisionEvents(mappedRequestDetailsHolder.getJumpExecutionDecisionEvents());
            context.setRepeatExecutionDecisionEvents(mappedRequestDetailsHolder.getRepeatExecutionDecisionEvents());
            context.setAutomatedPluginEvent(mappedRequestDetailsHolder.getAutoPluginEvents());
            return stepExecutorProvider.provide().execute(context);
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
