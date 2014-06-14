package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;
import com.step.core.annotations.StepJumper;

/**
 * Created by amishra on 6/13/14.
 */

@StepDefinition
@StepJumper(forRequest = "jumpTestNew", conditionClass = "com.step.core.example.steps.TestJumpConditionNew", onSuccessJumpTo = "jumpTestStep6")
public class JumpTestStep1 extends AbstractResponseLessStep {
    @Override
    public void execute() throws Exception {
        System.out.println("IN STEP1");
    }
}


