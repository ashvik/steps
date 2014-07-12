package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;

import java.util.List;

/**
 * Created by amishra on 7/7/14.
 */

@StepDefinition
public class NestedReqStep extends AbstractResponseLessStep {
    @Override
    public void execute() throws Exception {
        List in = getInput(List.class);
        System.out.println("IN NestedReqStep "+in);
    }
}
