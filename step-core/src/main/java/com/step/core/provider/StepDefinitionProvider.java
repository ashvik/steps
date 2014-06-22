package com.step.core.provider;

import com.step.core.Configuration;
import com.step.core.collector.StepDefinitionHolder;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepDefinitionProvider {
    void prepare(Configuration conf);
    StepDefinitionHolder getStepDefinitionByStepName(String name);
    StepDefinitionHolder getStepDefinitionByRequest(String name);
    List<StepDefinitionHolder> getGenericPreSteps();
    List<StepDefinitionHolder> getGenericPostSteps();
    Set<String> allRequests();
    Set<String> allSteps();
}
