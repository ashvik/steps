package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;

/**
 * Created by amishra on 7/4/14.
 */

@StepDefinition
public class TestPreStepAgain  extends AbstractResponseLessStep {
    @Override
    public void execute() throws Exception {
        System.out.println("IN TestPreStepAgain");
    }
}
