package com.step.test.base.step;

import com.step.core.AbstractStep;
import com.step.core.PluginGateway;
import com.step.core.ResponseLessStep;
import com.step.core.ResponsiveStep;
import com.step.core.annotations.Plugin;
import com.step.core.context.StepExecutionContext;
import com.step.core.io.ExecutionResult;
import com.step.core.io.StepInput;
import com.step.test.base.context.StepTestingExecutionContext;

import java.lang.reflect.Field;

/**
 * Created by amishra on 11/21/14.
 */
public abstract class StepProvider {
    public static <T extends ResponsiveStep> T createResponsiveStep(Class<T> stepClass, Object... inputs) throws IllegalAccessException, InstantiationException {
        ResponsiveStep step = stepClass.newInstance();
        prepareStep((AbstractStep)step, null, inputs);
        return (T)step;
    }

    public static <T extends ResponsiveStep> T createResponsiveStep(Class<T> stepClass, StepPrerequisitesHolder stepPrerequisitesHolder, Object... inputs) throws IllegalAccessException, InstantiationException {
        ResponsiveStep step = stepClass.newInstance();
        prepareStep((AbstractStep)step, stepPrerequisitesHolder, inputs);
        return (T)step;
    }

    public static <T extends ResponseLessStep> T createResponseLessStep(Class<T> stepClass, Object... inputs) throws IllegalAccessException, InstantiationException {
        ResponseLessStep step = stepClass.newInstance();
        prepareStep((AbstractStep)step, null, inputs);
        return (T)step;
    }

    public static <T extends ResponseLessStep> T createResponseLessStep(Class<T> stepClass, StepPrerequisitesHolder stepPrerequisitesHolder, Object... inputs) throws IllegalAccessException, InstantiationException {
        ResponseLessStep step = stepClass.newInstance();
        prepareStep((AbstractStep)step, stepPrerequisitesHolder, inputs);
        return (T)step;
    }

    private static void prepareStep(AbstractStep step, StepPrerequisitesHolder stepPrerequisitesHolder, Object... inputs){
        StepTestingExecutionContext stepTestingExecutionContext = new StepTestingExecutionContext();
        StepInput input = new StepInput("");

        for(Object in : inputs){
            input.setInput(in);
        }

        if(stepPrerequisitesHolder != null){
            stepTestingExecutionContext.setPluginRequest(stepPrerequisitesHolder.getPluginRequests());
            stepTestingExecutionContext.setRequestParameterContainer(stepPrerequisitesHolder.getRequestParameterContainer());
        }

        injectPlugins(step, stepTestingExecutionContext);
        stepTestingExecutionContext.setStepInput(input);
        step.setStepExecutionContext(stepTestingExecutionContext);
    }


    private static void injectPlugins(final AbstractStep step, final StepExecutionContext context){
        Field[] fields =  step.getClass().getFields();

        for(Field field : fields){
            final Plugin plugin = field.getAnnotation(Plugin.class);
            if(plugin != null){
                Field f = null;
                try {
                    f = step.getClass().getDeclaredField(field.getName());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                f.setAccessible(true);
                PluginGateway gateway = new PluginGateway(){
                    @Override
                    public ExecutionResult runPlugin(Object... inputs) throws Exception {
                        return context.applyPluginRequest(plugin.request(), inputs);
                    }
                };

                try {
                    f.set(step, gateway);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
