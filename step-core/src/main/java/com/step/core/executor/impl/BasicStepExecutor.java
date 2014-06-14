package com.step.core.executor.impl;

import com.step.core.ResponseLessStep;
import com.step.core.ResponsiveStep;
import com.step.core.chain.StepChain;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.conditions.BreakCondition;
import com.step.core.conditions.JumpCondition;
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
        String jumpTo = null;

        for(Class<?> stepClass : chain.getSteps()){
            Object step = null;
            if(!canApplyStep(chain, stepClass, jumpTo)){
                continue;
            }

            if(context.hasStepChainExecutionBroken()){
                break;
            }

            try {
                step = stepClass.newInstance();

                StepExecutionUtil.makeRichStepObject(step, chain.getDependenciesForStep(stepClass), context);

                if(step instanceof ResponsiveStep){
                    ResponsiveStep rs = (ResponsiveStep)step;
                    rs.setStepContext(context);
                    stepResult = rs.execute();
                    context.getStepInput().setInput(stepResult);
                    jumpTo = checkJumpCondition(chain, stepClass, context);
                    if(jumpTo == null || jumpTo.isEmpty()){
                        checkBreakCondition(chain, stepClass, context);
                    }
                }else if(step instanceof ResponseLessStep){
                    ResponseLessStep rls = (ResponseLessStep)step;
                    rls.setStepContext(context);
                    rls.execute();
                    stepResult = null;
                    jumpTo = checkJumpCondition(chain, stepClass, context);
                    if(jumpTo == null || jumpTo.isEmpty()){
                        checkBreakCondition(chain, stepClass, context);
                    }
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

    private String checkJumpCondition(StepChain chain, Class<?> stepClass, StepContext context) throws IllegalAccessException, InstantiationException {
        Class condition = chain.getJumpConditionClassForStep(stepClass);
        String jumpTo = null;

        if(condition != null){
            JumpCondition jumpCondition = (JumpCondition)condition.newInstance();
            jumpCondition.setStepContext(context);
            boolean res = jumpCondition.check();

            JumpDetails info = chain.getJumpDetailsForStep(stepClass);
            if(info == null){
                throw new IllegalStateException("Jump info should be configured if condition is present.");
            }

            if(res){
                jumpTo = info.getOnSuccessJumpStep();
            }else{
                jumpTo = info.getOnFailureJumpStep();
            }
        }

        return jumpTo;
    }

    private void checkBreakCondition(StepChain chain, Class<?> stepClass, StepContext context) throws IllegalAccessException, InstantiationException {
        Class condition = chain.getBreakConditionClassForStep(stepClass);

        if(condition != null){
            BreakCondition breakCondition = (BreakCondition)condition.newInstance();
            breakCondition.setStepContext(context);
            boolean res = breakCondition.check();

            if(res){
                context.breakStepChainExecution();
            }
        }
    }

    private boolean canApplyStep(StepChain chain, Class<?> step, String name){
        if(name != null && !name.isEmpty()){
            if(!chain.getStepName(step).equals(name)){
                return false;
            }
        }

        return true;
    }
}