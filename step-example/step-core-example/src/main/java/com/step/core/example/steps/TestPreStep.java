package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;

/**
 * Created by amishra on 6/22/14.
 */

@StepDefinition
public class TestPreStep extends AbstractResponseLessStep{
    @Override
    public void execute() throws Exception {
        System.out.println("IN TestPreStep");
    }
}
