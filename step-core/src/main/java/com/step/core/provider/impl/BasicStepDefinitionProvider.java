package com.step.core.provider.impl;

import com.step.core.Configuration;
import com.step.core.collector.StepCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.enums.GenericStepType;
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
            GenericStepType type = definition.getGenericStepType();

            StepDefinitionHolder def = steps.get(name);
            if(def != null){
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
        }
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

    @Override
    public Set<String> allRegisteredSteps() {
        return registeredSteps;
    }
}
