package com.step.core.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/24/13
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepRequestMapper {
    private List<MapRequest> mapRequests = new ArrayList<MapRequest>();
    private List<MultiScopedStep> multiScopedSteps = new ArrayList<MultiScopedStep>();
    private List<GenericParameterConfiguration> genericParameterConfigurations = new ArrayList<GenericParameterConfiguration>();

    public void add(MapRequest mr){
        this.mapRequests.add(mr);
    }

    public List<MapRequest> getMapRequests(){
        return this.mapRequests;
    }

    public void addMultiScopedStep(MultiScopedStep step){
        this.multiScopedSteps.add(step);
    }

    public List<MultiScopedStep> getMultiScopedSteps(){
        return this.multiScopedSteps;
    }

    public void addGenericParameterConfiguration(GenericParameterConfiguration genericParameterConfiguration){
        this.genericParameterConfigurations.add(genericParameterConfiguration);
    }

    public List<GenericParameterConfiguration> getGenericParameterConfigurations(){
        return genericParameterConfigurations;
    }
}
