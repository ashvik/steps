package com.step.core.repository;

import com.step.core.Configuration;
import com.step.core.chain.StepChain;
import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.collector.StepDefinitionHolder;

import java.util.Set;

/**
 * Created by amishra on 6/20/14.
 */
public interface StepRepository {
    StepDefinitionHolder getRootStepForRequest(String request);
    StepDefinitionHolder getStepByName(String name);
    StepChain getStepExecutionChainForRequest(String request);
    StepChain getStepExecutionChainForRequestUsingGenericStepsFlag(String request, boolean canUseGenericSteps);
    Set<String> getAllRequestsByName();
    Set<String> getAllStepsByName();
    String getAliasForRequest(String request);
    String getRequestForAlias(String alias);
    void setConfiguration(Configuration configuration);
    void buildRepository();
    MappedRequestDetailsHolder getMappedRequestDetails(String req);
}
