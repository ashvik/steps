package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.StepDefinition;
import com.step.core.io.ExecutionResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amishra on 6/13/14.
 */

@StepDefinition
//@StepJumper(forRequest = "jumpTestNew", conditionClass = "com.step.core.example.steps.TestJumpConditionNew", onSuccessJumpTo = "jumpTestStep6")
public class JumpTestStep1 extends AbstractResponseLessStep {
    @Override
    public void execute() throws Exception {
        Map<String, String> data = new HashMap();
        data.put("id","1");
        data.put("name","name_1");
        data.put("value", "value_1");
        data.put("description", "description_1");
        ExecutionResult result = getStepExecutionContext().applyPluginRequest("add", data);
        System.out.println("IN STEP1: "+result.getResultObject());
    }
}


