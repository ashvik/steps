package com.step.core.container.impl;

import com.step.core.Configuration;
import com.step.core.chain.StepChain;
import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.container.StepExecutionContainer;
import com.step.core.context.StepExecutionContext;
import com.step.core.context.impl.BasicStepExecutionContext;
import com.step.core.executor.StepExecutor;
import com.step.core.executor.StepExecutorProvider;
import com.step.core.executor.impl.BasicStepExecutorProvider;
import com.step.core.factory.ObjectFactory;
import com.step.core.io.ExecutionResult;
import com.step.core.io.StepInput;
import com.step.core.repository.StepRepository;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/22/13
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultStepExecutionContainer implements StepExecutionContainer {
    private Configuration configuration;
    private ObjectFactory objectFactory;
    private StepExecutorProvider stepExecutorProvider;
    private StepRepository stepRepository;

    @Override
    public ExecutionResult submit(StepInput input) throws Exception {
        String req = input.getRequest();
        StepChain chain = stepRepository.getStepExecutionChainForRequest(req);
        MappedRequestDetailsHolder mappedRequestDetailsHolder = stepRepository.getMappedRequestDetails(req);
        StepExecutionContext context = new BasicStepExecutionContext();

        context.setStepChain(chain);
        context.setStepInput(input);
        context.setObjectFactory(this.objectFactory);
        context.setStepRepository(stepRepository);
        context.setStepExecutorProvider(stepExecutorProvider);
        context.setRequestParameterContainer(chain.getRequestParameterContainer());
        context.setClassLoader(input.getClassLoader());
        context.setBreakExecutionDecisionEvents(mappedRequestDetailsHolder.getBreakExecutionDecisionEvents());
        context.setJumpExecutionDecisionEvents(mappedRequestDetailsHolder.getJumpExecutionDecisionEvents());
        context.setRepeatExecutionDecisionEvents(mappedRequestDetailsHolder.getRepeatExecutionDecisionEvents());
        context.setAutomatedPluginEvent(mappedRequestDetailsHolder.getAutoPluginEvents());
        StepExecutor executor = stepExecutorProvider.provide();
        return executor.execute(context);
    }

    @Override
    public ExecutionResult submit(String req, Object... ins) throws Exception {
        StepInput input = new StepInput(req);
        for(Object in : ins){
            input.setInput(in);
        }
        return submit(input);
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public void setStepRepository(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public void init() {
        stepExecutorProvider = new BasicStepExecutorProvider();
        stepExecutorProvider.initInterceptors(configuration);
    }
}