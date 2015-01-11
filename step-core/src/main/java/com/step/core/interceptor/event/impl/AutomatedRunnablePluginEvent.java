package com.step.core.interceptor.event.impl;

import com.step.core.PluginRequest;
import com.step.core.context.abstr.AbstractStepExecutionContextAware;
import com.step.core.interceptor.event.PluginEvent;
import com.step.core.interceptor.event.StepEventType;
import com.step.core.io.ExecutionResult;

/**
 * Created by amishra on 1/2/15.
 */
public class AutomatedRunnablePluginEvent extends AbstractStepExecutionContextAware implements PluginEvent<PluginRequest>{
    private PluginRequest pluginRequest;

    public AutomatedRunnablePluginEvent(PluginRequest pluginRequest){
        this.pluginRequest = pluginRequest;
    }

    @Override
    public void runPlugins() throws Exception{
        for(String plugin : pluginRequest.getPlugIns()){
            ExecutionResult result = getStepExecutionContext().applyPluginRequest(plugin);
            getStepExecutionContext().getStepInput().setInput(result.getResultObject());
        }
    }

    @Override
    public PluginRequest getPluginDetails() {
        return pluginRequest;
    }

    @Override
    public String getStep() {
        return pluginRequest.getStep();
    }

    @Override
    public StepEventType getEventType() {
        return pluginRequest.getStepEventType();
    }
}
