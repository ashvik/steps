package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;

import java.util.List;

/**
 * Created by amishra on 9/27/14.
 */

@StepDefinition
public class ParameterStep extends AbstractResponseLessStep{

    @Override
    public void execute() throws Exception {
        List<IParameter> parameters = getParameterAsObjects(IParameter.class);
        System.out.println(parameters.get(0));
    }
}
