package com.step.core.repository;

import com.step.core.Configuration;
import com.step.core.PluginRequest;
import com.step.core.alias.RequestAliasProvider;
import com.step.core.alias.impl.BasicRequestAliasProvider;
import com.step.core.chain.StepChain;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.impl.AnnotatedGenericStepCollector;
import com.step.core.collector.impl.AnnotatedStepCollector;
import com.step.core.collector.impl.XmlStepCollector;
import com.step.core.exceptions.PluginRequestNotFoundException;
import com.step.core.exceptions.RequestParameterNotFoundException;
import com.step.core.exceptions.StepChainException;
import com.step.core.exceptions.StepClassNotFoundException;
import com.step.core.interceptor.event.PluginEvent;
import com.step.core.parameter.GenericRequestParameterProvider;
import com.step.core.parameter.ParameterNameValueHolder;
import com.step.core.parameter.RequestParameterContainer;
import com.step.core.parameter.impl.BasicGenericRequestParameterProvider;
import com.step.core.parameter.impl.BasicRequestParameterContainer;
import com.step.core.provider.MappedRequestDetailsProvider;
import com.step.core.provider.StepDefinitionProvider;
import com.step.core.provider.impl.BasicMappedRequestDetailsProvider;
import com.step.core.provider.impl.BasicStepDefinitionProvider;
import com.step.core.utils.AnnotatedField;
import com.step.core.utils.PluginAnnotatedField;

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
    private RequestAliasProvider requestAliasProvider;
    private MappedRequestDetailsProvider mappedRequestDetailsProvider;

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
        MappedRequestDetailsHolder requestDetailsHolder = mappedRequestDetailsProvider.getMappedRequestDetails(req);
        StepDefinitionHolder rootHolder = stepDefinitionProvider.getStepDefinitionByStepName(requestDetailsHolder.getRootStep());
        StepDefinitionHolder holder = rootHolder;
        StepChain chain = new BasicStepChain();
        String request = requestDetailsHolder.getMappedRequest();
        boolean canApplyGenSteps = canUseGenericSteps && requestDetailsHolder.isCanApplyGenericSteps();

        addCommonStepsInChainIfApplicable(chain, canApplyGenSteps, true, request, requestDetailsHolder.getPreSteps());
        chain.addStep(holder, request);
        RequestParameterContainer requestParameterContainer = requestDetailsHolder.getRequestParameterContainer();
        List<String> genericPrams = requestDetailsHolder.getGenericParameters();
        String expectedOutCome = requestDetailsHolder.getExpectedOutCome();
        List<String> inputTypes = requestDetailsHolder.getInputTypes();
        String stepExceptionHandler = requestDetailsHolder.getStepExceptionHandler();

        if(inputTypes != null && !inputTypes.isEmpty()){
            chain.setInputTypes(inputTypes);
        }
        if(stepExceptionHandler != null){
            chain.setStepExceptionHandler(stepExceptionHandler);
        }
        if(expectedOutCome != null){
            chain.setExpectedOutCome(expectedOutCome);
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
            String scope = requestAliasProvider.getAliasForRequest(request);
            String next;
            if(scope != null){
                next = holder.getNextStepForScope(scope);
                if(next == null){
                    next = holder.getNextStepForScope(request);
                }
            }else{
                next = holder.getNextStepForScope(request);
            }

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
        return Collections.unmodifiableSet(this.mappedRequestDetailsProvider.getAllRequestNames());
    }

    @Override
    public Set<String> getAllStepsByName() {
        return Collections.unmodifiableSet(this.stepDefinitionProvider.allSteps());
    }

    @Override
    public String getAliasForRequest(String request) {
        return requestAliasProvider.getAliasForRequest(request);
    }

    @Override
    public String getRequestForAlias(String alias) {
        return requestAliasProvider.getRequestForAlias(alias);
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void buildRepository() {
        genericRequestParameterProvider = new BasicGenericRequestParameterProvider();
        requestAliasProvider = new BasicRequestAliasProvider();
        mappedRequestDetailsProvider = new BasicMappedRequestDetailsProvider();
        stepDefinitionProvider = new BasicStepDefinitionProvider(new AnnotatedStepCollector(),
                new AnnotatedGenericStepCollector(), new XmlStepCollector(genericRequestParameterProvider, requestAliasProvider,mappedRequestDetailsProvider));
        stepDefinitionProvider.prepare(configuration);
        validate();
    }

    @Override
    public MappedRequestDetailsHolder getMappedRequestDetails(String req) {
        return mappedRequestDetailsProvider.getMappedRequestDetails(req);
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

    private void validate(){
        Set<String> allRegisteredSteps = stepDefinitionProvider.allRegisteredSteps();
        Set<String> allRequests = mappedRequestDetailsProvider.getAllRequestNames();
        Set<String> allSteps = stepDefinitionProvider.allSteps();

        for(String step : allSteps){
            StepDefinitionHolder stepDefinitionHolder = stepDefinitionProvider.getStepDefinitionByStepName(step);
            String next = stepDefinitionHolder.getNextStep();
            Set<String> scopes = stepDefinitionHolder.getScopes();
            List<AnnotatedField> annotatedPlugins = stepDefinitionHolder.getPlugins();

            //check steps definition
            if(stepDefinitionHolder.getStepClass() == null){
                throw new StepClassNotFoundException("No Step Class found for step having name '"+stepDefinitionHolder.getName()+"'.");
            }if(next != null && !next.isEmpty()){
                if(!allRegisteredSteps.contains(next)){
                    throw new StepClassNotFoundException("No Step Class found for step having name '"+next+"' configured next step for step '"+stepDefinitionHolder.getName()+"'.");
                }
            }

            //check scopes
            for(String scope : scopes){
                if(!allRequests.contains(scope) && requestAliasProvider.getRequestForAlias(scope) == null){
                    throw new PluginRequestNotFoundException("Request '"+scope+"' not found, configured in multi scoped step '"+stepDefinitionHolder.getName()+"'.");
                }

                String nextStepForScope = stepDefinitionHolder.getNextStepForScope(scope);
                if(!allRegisteredSteps.contains(nextStepForScope)){
                    throw new StepClassNotFoundException("No Step Class found for step having name '"+nextStepForScope+"' configured for step '"+step+"'.");
                }
            }

            //check annotated plugins
            for(AnnotatedField annotatedField : annotatedPlugins){
                PluginAnnotatedField pluginAnnotatedField = (PluginAnnotatedField) annotatedField;
                String pluginReq = pluginAnnotatedField.getRequest();
                if(!allRequests.contains(pluginReq) && requestAliasProvider.getRequestForAlias(pluginReq) == null){
                    throw new PluginRequestNotFoundException("Plugin request '"+pluginReq+"' not found, configured in step '"+stepDefinitionHolder.getName()+"'.");
                }
            }


        }
        for(String request : allRequests){
            if(request != null && !request.isEmpty()){
                MappedRequestDetailsHolder mappedRequestDetailsHolder = mappedRequestDetailsProvider.getMappedRequestDetails(request);
                String rootStep = mappedRequestDetailsHolder.getRootStep();

                //check root step
                if(!allRegisteredSteps.contains(rootStep)){
                    throw new StepClassNotFoundException("No Step Class found for step having name '"+rootStep+"'"+(request == null ? "." : "configured in request '"+request+"'."));
                }

                //check automated plugins
                List<PluginEvent> plugins = mappedRequestDetailsHolder.getAutoPluginEvents();
                if(plugins != null && !plugins.isEmpty()){
                    for(PluginEvent<PluginRequest> pluginEvent : plugins){
                        PluginRequest pluginRequest = pluginEvent.getPluginDetails();
                        for(String plugin : pluginRequest.getPlugIns()){
                            if(!allRequests.contains(plugin) && requestAliasProvider.getRequestForAlias(plugin) == null){
                                throw new PluginRequestNotFoundException("Plugin request '"+plugin+"' not found, configured in request '"+request+"'.");
                            }
                        }
                    }
                }
            }
        }
    }
}
