package com.step.core.repository;

import com.step.core.Configuration;
import com.step.core.chain.StepChain;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.impl.AnnotatedGenericStepCollector;
import com.step.core.collector.impl.AnnotatedStepCollector;
import com.step.core.collector.impl.XmlStepCollector;
import com.step.core.exceptions.StepChainException;
import com.step.core.provider.StepDefinitionProvider;
import com.step.core.provider.impl.BasicStepDefinitionProvider;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by amishra on 6/20/14.
 */
public class BasicStepRepository implements StepRepository{
    private Configuration configuration;
    private StepDefinitionProvider stepDefinitionProvider;

    @Override
    public StepDefinitionHolder getRootStepForRequest(String request) {
        return stepDefinitionProvider.getStepDefinitionByRequest(request);
    }

    @Override
    public StepDefinitionHolder getStepByName(String name) {
        return stepDefinitionProvider.getStepDefinitionByStepName(name);
    }

    @Override
    public StepChain getStepExecutionChainForRequest(String req) {
        StepDefinitionHolder rootHolder = stepDefinitionProvider.getStepDefinitionByRequest(req);
        StepDefinitionHolder holder = rootHolder;
        StepChain chain = new BasicStepChain();
        String request = holder.getMappedRequest();

        addCommonStepsInChainIfApplicable(chain, rootHolder.isCanApplyGenericSteps(), true, request, rootHolder.getPreSteps());

        chain.addStep(holder, request);
        boolean isFinished = false;

        while(!isFinished){
            String next = holder.getNextStepForScope(request);
            if(next != null && !next.isEmpty()){
                holder = this.stepDefinitionProvider.getStepDefinitionByStepName(next);
                if(holder == null){
                    throw new StepChainException("No step found with name '"+next+"' check if step is annotated.");
                }
                chain.addStep(holder, request);
            }else{
                String nextStep = holder.getNextStep();
                if(nextStep != null && !nextStep.isEmpty()){
                    holder = this.stepDefinitionProvider.getStepDefinitionByStepName(nextStep);
                    if(holder == null){
                        throw new StepChainException("No step found with name '"+nextStep+"' check if step is annotated.");
                    }
                }else{
                    isFinished = true;
                }

                chain.addStep(holder, request);
            }
        }

        addCommonStepsInChainIfApplicable(chain, rootHolder.isCanApplyGenericSteps(), false, request, rootHolder.getPostSteps());

        return chain;
    }

    @Override
    public Set<String> getAllRequestsByName() {
        return Collections.unmodifiableSet(this.stepDefinitionProvider.allRequests());
    }

    @Override
    public Set<String> getAllStepsByName() {
        return Collections.unmodifiableSet(this.stepDefinitionProvider.allSteps());
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void buildRepository() {
        stepDefinitionProvider = new BasicStepDefinitionProvider(new AnnotatedStepCollector(),
                new AnnotatedGenericStepCollector(), new XmlStepCollector());
        stepDefinitionProvider.prepare(configuration);
    }

    private void addCommonStepsInChainIfApplicable(StepChain chain, boolean canApply, boolean isPreStep, String request, List<String> steps){
        List<StepDefinitionHolder> genStep = isPreStep ? this.stepDefinitionProvider.getGenericPreSteps() :
                this.stepDefinitionProvider.getGenericPostSteps();

        if(canApply){
            for(StepDefinitionHolder def : genStep){
                chain.addInterceptorStep(def, request, isPreStep);
            }
        }if(!steps.isEmpty()){
            for(String step : steps){
                chain.addInterceptorStep(this.stepDefinitionProvider.getStepDefinitionByStepName(step), request, isPreStep);
            }
        }
    }
}
