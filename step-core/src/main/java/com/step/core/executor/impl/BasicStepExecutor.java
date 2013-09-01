package com.step.core.executor.impl;

import com.step.core.ResponseLessStep;
import com.step.core.ResponsiveStep;
import com.step.core.chain.StepChain;
import com.step.core.context.StepContext;
import com.step.core.exceptions.StepExecutionException;
import com.step.core.executor.StepExecutor;
import com.step.core.io.ExecutionResult;
import com.step.core.utils.StepExecutionUtil;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/25/13
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicStepExecutor implements StepExecutor {
    @Override
    public ExecutionResult execute(StepChain chain, StepContext context) {
        ExecutionResult result = null;
        Object stepResult = null;

        for(Class<?> stepClass : chain.getSteps()){
            Object step = null;
            try {
                step = stepClass.newInstance();

                StepExecutionUtil.makeRichStepObject(step, chain.getDependenciesForStep(stepClass), context);

                if(step instanceof ResponsiveStep){
                    ResponsiveStep rs = (ResponsiveStep)step;
                    rs.setStepContext(context);
                    stepResult = rs.execute();
                    context.getStepInput().setInput(stepResult);
                }else if(step instanceof ResponseLessStep){
                    ResponseLessStep rls = (ResponseLessStep)step;
                    rls.setStepContext(context);
                    rls.execute();
                    stepResult = null;
                }else{
                    throw new IllegalStateException("Step can only be the instanceof ResponsiveStep or ResponseLessStep.");
                }
            } catch(Exception e){
                throw new StepExecutionException(stepClass, e);
            }
        }
        result = new ExecutionResult(stepResult, context.getAttributes());

        return result;
    }
}