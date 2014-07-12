package com.step.external.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;

/**
 * Created by ashish on 12-07-2014.
 */

@StepDefinition(next = "externalStep2")
public class ExternalStep1 extends AbstractResponseLessStep {

    @Override
    public void execute() throws Exception {
        System.out.println("ExternalStep1...");
    }
}
