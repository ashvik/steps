package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;

/**
 * Created by amishra on 6/13/14.
 */

@StepDefinition(next = "jumpTestStep7")
public class JumpTestStep6 extends AbstractResponseLessStep {
    @Override
    public void execute() throws Exception {
        System.out.println("IN STEP6");
    }
}
