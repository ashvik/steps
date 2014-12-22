package com.step.informer.service;

import com.step.core.Configuration;
import com.step.core.repository.StepRepository;
import com.step.informer.flat.StepChainInfo;
import com.step.informer.flat.StepInfo;

import java.util.List;
import java.util.Set;

/**
 * Created by amishra on 6/21/14.
 */
public interface StepInformationService {
    void setStepRepository(StepRepository stepRepository);
    Set<String> fetchAllConfiguredRequests();
    Set<String> fetchAllConfiguredSteps();
    StepChainInfo getStepChainInfoForRequest(String request);
    List<String> getStepChainInfoDiagramForRequest(String request);
    StepInfo getStepDetails(String name);
    void generateDocumentation(String versionString, Configuration configuration);
}
