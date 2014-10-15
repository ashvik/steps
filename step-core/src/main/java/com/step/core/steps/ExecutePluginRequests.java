package com.step.core.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.PluginRequest;
import com.step.core.annotations.StepDefinition;
import com.step.core.io.ExecutionResult;

import java.util.List;

/**
 * Created by amishra on 10/14/14.
 */

@StepDefinition
public class ExecutePluginRequests extends AbstractResponseLessStep{
    @Override
    public void execute() throws Exception {
        List<PluginRequest> pluginRequests = getStepExecutionContext().getPluginRequests();
        Object out;

        for(PluginRequest pluginRequest : pluginRequests){
            if(pluginRequest.isRunAutomatically()){
                ExecutionResult result = runPluginRequest(pluginRequest.getRequest());
                out = result.getResultObject();

                if(out != null){
                    getStepExecutionContext().getStepInput().setInput(out);
                }
            }
        }
    }
}
