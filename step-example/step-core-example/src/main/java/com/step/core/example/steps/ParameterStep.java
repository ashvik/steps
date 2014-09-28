package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;

/**
 * Created by amishra on 9/27/14.
 */

@StepDefinition
public class ParameterStep extends AbstractResponseLessStep{

    @Override
    public void execute() throws Exception {
        IParameter parameters = getParameterAsSingleObject(IParameter.class);
        System.out.println(parameters);
        System.out.println(getParameterAsString("g1"));
        System.out.println(getParameterAsString("gnew1"));
    }
}
