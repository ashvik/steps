package com.step.core;

import com.step.core.interceptor.event.StepEventType;

import java.util.List;

/**
 * Created by amishra on 10/14/14.
 */
public class PluginRequest{
    private String step;
    private List<String> plugIns;
    private StepEventType stepEventType;

    public PluginRequest(String step, String type, List<String> plugIns){
        this.step = step;
        this.plugIns = plugIns;
        this.stepEventType = StepEventType.getEventTypeFromCode(type);
    }

    public String getStep() {
        return step;
    }

    public List<String> getPlugIns() {
        return plugIns;
    }

    public StepEventType getStepEventType(){
        return stepEventType;
    }
}
