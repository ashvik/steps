package com.step.test.base.step;

import com.step.core.AbstractStep;
import com.step.core.ResponseLessStep;
import com.step.core.ResponsiveStep;
import com.step.core.io.StepInput;
import com.step.test.base.context.StepTestingExecutionContext;

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
        stepTestingExecutionContext.setStepInput(input);
        step.setStepExecutionContext(stepTestingExecutionContext);
    }
}
