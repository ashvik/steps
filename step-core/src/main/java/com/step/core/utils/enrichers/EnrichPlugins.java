package com.step.core.utils.enrichers;

import com.step.core.PluginGateway;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.context.StepExecutionContext;
import com.step.core.io.ExecutionResult;
import com.step.core.utils.AnnotatedField;
import com.step.core.utils.PluginAnnotatedField;
import com.step.core.utils.StepEnricher;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by amishra on 1/9/15.
 */
public class EnrichPlugins implements StepEnricher {
    @Override
    public void enrichStep(Object step, StepDefinitionHolder definitionHolder, StepExecutionContext stepExecutionContext) {
        List<AnnotatedField> plugins = definitionHolder.getPlugins();

        for(AnnotatedField field : plugins){
            PluginAnnotatedField pluginAnnotatedField = (PluginAnnotatedField)field;
            String name = pluginAnnotatedField.getRequest();
            boolean applyGenericSteps = pluginAnnotatedField.isApplyGenericSteps();
            boolean passCurrentInputs = pluginAnnotatedField.isPassCurrentInputs();
            try{
                PluginGateway gateway = getPluginGateway(name, stepExecutionContext, applyGenericSteps, passCurrentInputs);
                Field f = step.getClass().getDeclaredField(field.getFieldName());
                f.setAccessible(true);
                f.set(step, gateway);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private static PluginGateway getPluginGateway(final String name, final StepExecutionContext context,
                                                  final boolean applyGenericSteps, final boolean passCurrentInputs){
        return new PluginGateway() {
            @Override
            public ExecutionResult runPlugin(Object... inputs) throws Exception {
                return context.applyPluginRequest(name, passCurrentInputs, applyGenericSteps, inputs);
            }
        };
    }
}
