package com.step.web;

import com.step.core.container.impl.DefaultStepExecutionContainer;
import com.step.core.exceptions.StepExecutionException;
import com.step.core.io.ExecutionResult;
import com.step.core.io.StepInput;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/23/13
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepWebExecutionContainer extends DefaultStepExecutionContainer {

    public WebExecutionResult submit(StepInput input) throws Exception {
        try{
            ExecutionResult result = super.submit(input);
            String onSuccess = getStepDefinition(input.getRequest()).getOnSuccess();
            return new WebExecutionResult(result.getResultObject(), onSuccess, result.getAttributes());
        }catch(StepExecutionException e){
            String onFailure = getStepDefinition(input.getRequest()).getOnFailure();
            return new WebExecutionResult(e.getMessage(), onFailure, null);
        }
    }
}
