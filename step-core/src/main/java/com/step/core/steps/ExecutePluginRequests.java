package com.step.core.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.PluginRequest;
import com.step.core.annotations.StepDefinition;
import com.step.core.interceptor.event.PluginEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Created by amishra on 10/14/14.
 */

@StepDefinition
public class ExecutePluginRequests extends AbstractResponseLessStep{
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void execute() throws Exception {
        List<PluginEvent> pluginEvents = getStepExecutionContext().getAutomatedPluginEvent();

        for(PluginEvent<PluginRequest> pluginEvent : pluginEvents){
            PluginRequest pluginRequest = pluginEvent.getPluginDetails();
            logger.info("Successfully ran following plugins: "+pluginRequest.getPlugIns());
        }
    }
}
