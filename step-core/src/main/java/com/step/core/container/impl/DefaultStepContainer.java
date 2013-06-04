package com.step.core.container.impl;

import com.step.core.Configuration;
import com.step.core.chain.StepChain;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.impl.AnnotatedGenericStepCollector;
import com.step.core.collector.impl.AnnotatedStepCollector;
import com.step.core.collector.impl.XmlStepCollector;
import com.step.core.container.StepContainer;
import com.step.core.context.StepContext;
import com.step.core.context.impl.BasicStepContext;
import com.step.core.executor.StepExecutor;
import com.step.core.executor.StepExecutorProvider;
import com.step.core.executor.impl.BasicStepExecutorProvider;
import com.step.core.factory.ObjectFactory;
import com.step.core.io.ExecutionResult;
import com.step.core.io.StepInput;
import com.step.core.provider.StepDefinitionProvider;
import com.step.core.provider.impl.BasicStepDefinitionProvider;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/22/13
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultStepContainer implements StepContainer {
    private Configuration configuration;
    private ObjectFactory objectFactory;
    private StepDefinitionProvider stepDefinitionProvider;
    private StepExecutorProvider stepExecutorProvider;

    @Override
    public ExecutionResult submit(StepInput input) {
        String req = input.getRequest();
        StepDefinitionHolder sdh = stepDefinitionProvider.getStepDefinitionByRequest(req);

        if(sdh == null){
            //TODO throw exception
        }

        StepChain chain = createStepChainToExecute(sdh);
        StepContext context = new BasicStepContext();
        context.setStepInput(input);
        context.setObjectFactory(this.objectFactory);
        StepExecutor executor = stepExecutorProvider.provide(chain, context);
        return executor.execute(chain, context);
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
    public void init() {
        stepDefinitionProvider = new BasicStepDefinitionProvider(new AnnotatedStepCollector(),
                new AnnotatedGenericStepCollector(), new XmlStepCollector());
        stepDefinitionProvider.prepare(configuration);
        stepExecutorProvider = new BasicStepExecutorProvider();
        stepExecutorProvider.initInterceptors(configuration);
    }

    private StepChain createStepChainToExecute(StepDefinitionHolder holder){
        StepChain chain = new BasicStepChain();
        String request = holder.getMappedRequest();
        addCommonStepsInChainIfApplicable(chain, holder.isCanApplyGenericSteps(), true, holder.getPreSteps());

        chain.addStep(holder.getStepClass());
        boolean isFinished = false;

        while(!isFinished){
            String next = holder.getNextStepForScope(request);//holder.getNextStep();
            if(next != null && !next.isEmpty()){
                holder = this.stepDefinitionProvider.getStepDefinitionByStepName(next);
                chain.addStep(holder.getStepClass());
            }else{
                String nextStep = holder.getNextStep();
                if(nextStep != null && !nextStep.isEmpty()){
                    holder = this.stepDefinitionProvider.getStepDefinitionByStepName(nextStep);
                }else{
                    isFinished = true;
                }

                chain.addStep(holder.getStepClass());
            }
        }

        addCommonStepsInChainIfApplicable(chain, holder.isCanApplyGenericSteps(), false, holder.getPostSteps());

        return chain;
    }

    private void addCommonStepsInChainIfApplicable(StepChain chain, boolean canApply, boolean isPreStep, List<String> steps){
        List<StepDefinitionHolder> genStep = isPreStep ? this.stepDefinitionProvider.getGenericPreSteps() :
                this.stepDefinitionProvider.getGenericPostSteps();

        if(canApply){
            for(StepDefinitionHolder def : genStep){
                chain.addInterceptorStep(def.getStepClass(), isPreStep);
            }
        }else if(!steps.isEmpty()){
            for(String step : steps){
                chain.addInterceptorStep(this.stepDefinitionProvider.getStepDefinitionByStepName(step).getStepClass(), isPreStep);
            }
        }
    }
}