package com.step.core.provider.impl;

import com.step.core.Configuration;
import com.step.core.PluginRequest;
import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.collector.StepCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.enums.GenericStepType;
import com.step.core.exceptions.PluginRequestNotFoundException;
import com.step.core.exceptions.StepClassNotFoundException;
import com.step.core.provider.StepDefinitionProvider;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicStepDefinitionProvider implements StepDefinitionProvider {
    private Map<String, StepDefinitionHolder> steps = new HashMap<String, StepDefinitionHolder>();
    private Map<String, String> stepsRequestMapper = new HashMap<String, String>();
    private List<StepDefinitionHolder> genericPreSteps = new ArrayList<StepDefinitionHolder>();
    private List<StepDefinitionHolder> genericPostSteps = new ArrayList<StepDefinitionHolder>();
    private Set<String> registeredSteps = new HashSet<String>();
    private StepCollector[] stepCollectors;

    public BasicStepDefinitionProvider(StepCollector... stepCollectors){
        this.stepCollectors = stepCollectors;
    }

    @Override
    public void prepare(Configuration conf) {
        List<StepDefinitionHolder> definitions = new ArrayList<StepDefinitionHolder>();

        for(StepCollector collector : stepCollectors){
            definitions.addAll(collector.collect(conf));
        }

        for(StepDefinitionHolder definition : definitions){
            String name = definition.getName();
            MappedRequestDetailsHolder mappedRequestDetailsHolder = definition.getMappedRequestDetailsHolder();
            String request = null;
            if(mappedRequestDetailsHolder != null){
                request = mappedRequestDetailsHolder.getMappedRequest();
            }
            GenericStepType type = definition.getGenericStepType();

            StepDefinitionHolder def = steps.get(name);
            if(def != null){
                if(def.getMappedRequestDetailsHolder() != null &&
                        def.getMappedRequestDetailsHolder().getMappedRequest() != null &&
                        !def.getMappedRequestDetailsHolder().getMappedRequest().isEmpty() &&
                        request != null){
                    StepDefinitionHolder cloned = def.cloneWithDifferentMappedRequest(request);
                    cloned.getMappedRequestDetailsHolder().setPreSteps(definition.getMappedRequestDetailsHolder().getPreSteps());
                    cloned.getMappedRequestDetailsHolder().setPostSteps(definition.getMappedRequestDetailsHolder().getPostSteps());
                    cloned.getMappedRequestDetailsHolder().setCanApplyGenericSteps(definition.getMappedRequestDetailsHolder().isCanApplyGenericSteps());
                    cloned.getMappedRequestDetailsHolder().addPluginRequests(request, definition.getMappedRequestDetailsHolder().getPluginsForRequest(request));
                    cloned.getMappedRequestDetailsHolder().addGenericParameter(definition.getMappedRequestDetailsHolder().getGenericParameters());
                    cloned.getMappedRequestDetailsHolder().setInputTypes(definition.getMappedRequestDetailsHolder().getInputTypes());
                    cloned.getMappedRequestDetailsHolder().setRequestParameterContainer(definition.getMappedRequestDetailsHolder().getRequestParameterContainer());
                    cloned.getMappedRequestDetailsHolder().setExpectedOutCome(definition.getMappedRequestDetailsHolder().getExpectedOutCome());
                    cloned.getMappedRequestDetailsHolder().setStepExceptionHandler(definition.getMappedRequestDetailsHolder().getStepExceptionHandler());
                    String key = request+"@"+name;
                    steps.put(key, cloned);
                    stepsRequestMapper.put(request, key);
                    registeredSteps.add(name);
                    continue;
                }
                def.merge(definition);
            }else{
                steps.put(definition.getName(), definition);
                registeredSteps.add(name);
            }

            if(type != null){
                if(type == GenericStepType.PRE_STEP){
                    genericPreSteps.add(definition);
                }else{
                    genericPostSteps.add(definition);
                }

                continue;
            }

            if(request != null){
                stepsRequestMapper.put(request, name);
            }
        }

        validateSteps();
    }

    @Override
    public StepDefinitionHolder getStepDefinitionByStepName(String name) {
        return steps.get(name);
    }

    @Override
    public StepDefinitionHolder getStepDefinitionByRequest(String name) {
        String stepName = stepsRequestMapper.get(name);
        if(stepName != null){
            return steps.get(stepName);
        }
        return null;
    }

    @Override
    public List<StepDefinitionHolder> getGenericPreSteps() {
        return this.genericPreSteps;
    }

    @Override
    public List<StepDefinitionHolder> getGenericPostSteps() {
        return this.genericPostSteps;
    }

    @Override
    public Set<String> allRequests() {
        return this.stepsRequestMapper.keySet();
    }

    @Override
    public Set<String> allSteps() {
        return this.steps.keySet();
    }

    private void validateSteps(){
        Set<String> allRequests = allRequests();

        for(String step : steps.keySet()){
            StepDefinitionHolder stepDefinitionHolder = steps.get(step);
            String next = stepDefinitionHolder.getNextStep();
            Set<String> nextScopeSteps = stepDefinitionHolder.getNextStepsForAllApplicableScopes();

            if(stepDefinitionHolder.getMappedRequestDetailsHolder() != null){
                String request = stepDefinitionHolder.getMappedRequestDetailsHolder().getMappedRequest();
                if(request != null && !request.isEmpty()){
                    List<PluginRequest> plugins = stepDefinitionHolder.getMappedRequestDetailsHolder().getPluginsForRequest(request);
                    if(plugins != null && !plugins.isEmpty()){
                        for(PluginRequest pluginRequest : plugins){
                            if(!allRequests.contains(pluginRequest.getRequest())){
                                throw new PluginRequestNotFoundException("Plugin request '"+pluginRequest.getRequest()+"' not found, configured in request '"+request+"'.");
                            }
                        }
                    }
                }
            }
            if(stepDefinitionHolder.getStepClass() == null){
                throw new StepClassNotFoundException("No Step Class found for step having name '"+stepDefinitionHolder.getName()+"'.");
            }if(next != null && !next.isEmpty()){
                if(!registeredSteps.contains(next)){
                    throw new StepClassNotFoundException("No Step Class found for step having name '"+next+"'.");
                }
            }if(!nextScopeSteps.isEmpty()){
                for(String nextScopeStep : nextScopeSteps){
                    if(!registeredSteps.contains(nextScopeStep)){
                        throw new StepClassNotFoundException("No Step Class found for step having name '"+nextScopeStep+"'.");
                    }
                }
            }
        }
    }
}
