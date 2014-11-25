package com.step.core.repository;

import com.step.core.Configuration;
import com.step.core.PluginRequest;
import com.step.core.chain.StepChain;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.impl.AnnotatedGenericStepCollector;
import com.step.core.collector.impl.AnnotatedStepCollector;
import com.step.core.collector.impl.XmlStepCollector;
import com.step.core.exceptions.RequestParameterNotFoundException;
import com.step.core.exceptions.StepChainException;
import com.step.core.parameter.GenericRequestParameterProvider;
import com.step.core.parameter.ParameterNameValueHolder;
import com.step.core.parameter.RequestParameterContainer;
import com.step.core.parameter.impl.BasicGenericRequestParameterProvider;
import com.step.core.parameter.impl.BasicRequestParameterContainer;
import com.step.core.provider.StepDefinitionProvider;
import com.step.core.provider.impl.BasicStepDefinitionProvider;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by amishra on 6/20/14.
 */
public class BasicStepRepository implements StepRepository {
    private Configuration configuration;
    private StepDefinitionProvider stepDefinitionProvider;
    private GenericRequestParameterProvider genericRequestParameterProvider;

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
        return getStepExecutionChainForRequestUsingGenericStepsFlag(req, true);
    }

    @Override
    public StepChain getStepExecutionChainForRequestUsingGenericStepsFlag(String req, boolean canUseGenericSteps) {
        StepDefinitionHolder rootHolder = stepDefinitionProvider.getStepDefinitionByRequest(req);
        MappedRequestDetailsHolder requestDetailsHolder = rootHolder.getMappedRequestDetailsHolder();
        StepDefinitionHolder holder = rootHolder;
        StepChain chain = new BasicStepChain();
        String request = requestDetailsHolder.getMappedRequest();
        boolean canApplyGenSteps = canUseGenericSteps && requestDetailsHolder.isCanApplyGenericSteps();

        addCommonStepsInChainIfApplicable(chain, canApplyGenSteps, true, request, requestDetailsHolder.getPreSteps());

        chain.addStep(holder, request);
        RequestParameterContainer requestParameterContainer = rootHolder.getMappedRequestDetailsHolder().getRequestParameterContainer();
        List<String> genericPrams = rootHolder.getMappedRequestDetailsHolder().getGenericParameters();
        String expectedOutCome = rootHolder.getMappedRequestDetailsHolder().getExpectedOutCome();
        List<String> inputTypes = rootHolder.getMappedRequestDetailsHolder().getInputTypes();
        String stepExceptionHandler = rootHolder.getMappedRequestDetailsHolder().getStepExceptionHandler();
        List<PluginRequest> pluginRequests = rootHolder.getMappedRequestDetailsHolder().getPluginsForRequest(req);

        if(inputTypes != null && !inputTypes.isEmpty()){
            chain.setInputTypes(inputTypes);
        }
        if(stepExceptionHandler != null){
            chain.setStepExceptionHandler(stepExceptionHandler);
        }
        if(expectedOutCome != null){
            chain.setExpectedOutCome(expectedOutCome);
        }
        if(pluginRequests != null){
            chain.setPluginRequests(pluginRequests);
        }
        if(requestParameterContainer != null){
            populateGenericRequestParams(requestParameterContainer, genericPrams);
            chain.setRequestParameterContainer(requestParameterContainer);
        }else if(!genericPrams.isEmpty()){
            requestParameterContainer = new BasicRequestParameterContainer();
            populateGenericRequestParams(requestParameterContainer, genericPrams);
            chain.setRequestParameterContainer(requestParameterContainer);
        }

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

        addCommonStepsInChainIfApplicable(chain, canApplyGenSteps, false, request, requestDetailsHolder.getPostSteps());

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
        genericRequestParameterProvider = new BasicGenericRequestParameterProvider();
        stepDefinitionProvider = new BasicStepDefinitionProvider(new AnnotatedStepCollector(),
                new AnnotatedGenericStepCollector(), new XmlStepCollector(genericRequestParameterProvider));
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

    private void populateGenericRequestParams(RequestParameterContainer requestParameterContainer, List<String> genericPrams){
        for(String genericParam : genericPrams){
            List<ParameterNameValueHolder> params = genericRequestParameterProvider.getRequestParameterNameValuePairs(genericParam);
            if(params == null){
                throw new RequestParameterNotFoundException(
                        String.format("Generic parameters for ref '%s' is not found.", genericParam));
            }
            for(ParameterNameValueHolder param : params){
                requestParameterContainer.addRequestParameter(param.getName(), param.getValues());
            }
        }
    }
}
