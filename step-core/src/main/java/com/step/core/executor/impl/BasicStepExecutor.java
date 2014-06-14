package com.step.core.executor.impl;

import com.step.core.ResponseLessStep;
import com.step.core.ResponsiveStep;
import com.step.core.chain.StepChain;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.conditions.BreakCondition;
import com.step.core.conditions.JumpCondition;
import com.step.core.conditions.RepeatBreakCondition;
import com.step.core.context.StepExecutionContext;
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
    public ExecutionResult execute(StepChain chain, StepExecutionContext context) {
        ExecutionResult result = null;
        Object stepResult = null;
        String jumpTo = null;
        String repeatToStep = null;
        BasicStepChain.StepNode currentNode = chain.getRootNode();

        while(true){
            Object step = null;
            Class<?> stepClass = currentNode.getStepClass();

            if(context.hasStepChainExecutionBroken()){
                break;
            }

            try {
                step = stepClass.newInstance();
                StepExecutionUtil.makeRichStepObject(step, chain.getDependenciesForStep(stepClass), context);

                if(step instanceof ResponsiveStep){
                    ResponsiveStep rs = (ResponsiveStep)step;
                    rs.setStepExecutionContext(context);
                    stepResult = rs.execute();
                    context.getStepInput().setInput(stepResult);
                    jumpTo = checkJumpCondition(chain, stepClass, context);
                    if(jumpTo == null || jumpTo.isEmpty()){
                        repeatToStep = checkRepeatBreakCondition(chain, stepClass, context);
                        if(repeatToStep == null || repeatToStep.isEmpty()){
                            checkBreakCondition(chain, stepClass, context);
                        }
                    }
                }else if(step instanceof ResponseLessStep){
                    ResponseLessStep rls = (ResponseLessStep)step;
                    rls.setStepExecutionContext(context);
                    rls.execute();
                    stepResult = null;
                    jumpTo = checkJumpCondition(chain, stepClass, context);
                    if(jumpTo == null || jumpTo.isEmpty()){
                        repeatToStep = checkRepeatBreakCondition(chain, stepClass, context);
                        if(repeatToStep == null || repeatToStep.isEmpty()){
                            checkBreakCondition(chain, stepClass, context);
                        }
                    }
                }else{
                    throw new IllegalStateException("Step can only be the instanceof ResponsiveStep or ResponseLessStep.");
                }
            } catch(Exception e){
                throw new StepExecutionException(stepClass, e);
            }

            if(jumpTo != null && !jumpTo.isEmpty()){
                int currentSequence = currentNode.getStepSequence();
                currentNode = chain.getStepNodeByName(jumpTo);
                if(currentNode.getStepSequence() < currentSequence){
                    throw new StepExecutionException(stepClass, "Jumpers can move only in forward direction.");
                }
            }else if(repeatToStep != null && !repeatToStep.isEmpty()){
                int currentSequence = currentNode.getStepSequence();
                currentNode = chain.getStepNodeByName(repeatToStep);
                if(currentNode.getStepSequence() > currentSequence){
                    throw new StepExecutionException(stepClass, "Repeaters can move only in reverse direction.");
                }
            }else if(currentNode.getNextNode() != null){
                currentNode = currentNode.getNextNode();
            }else{
                break;
            }
        }

        result = new ExecutionResult(stepResult, context.getAttributes());

        return result;
    }

    private String checkJumpCondition(StepChain chain, Class<?> stepClass, StepExecutionContext context) throws IllegalAccessException, InstantiationException {
        Class condition = chain.getJumpConditionClassForStep(stepClass);
        String jumpTo = null;

        if(condition != null){
            JumpCondition jumpCondition = (JumpCondition)condition.newInstance();
            jumpCondition.setStepExecutionContext(context);
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

    private void checkBreakCondition(StepChain chain, Class<?> stepClass, StepExecutionContext context) throws IllegalAccessException, InstantiationException {
        Class condition = chain.getBreakConditionClassForStep(stepClass);

        if(condition != null){
            BreakCondition breakCondition = (BreakCondition)condition.newInstance();
            breakCondition.setStepExecutionContext(context);
            boolean res = breakCondition.check();

            if(res){
                context.breakStepChainExecution();
            }
        }
    }

    private String checkRepeatBreakCondition(StepChain chain, Class<?> stepClass, StepExecutionContext context) throws IllegalAccessException, InstantiationException {
        Class condition = chain.getRepeatBreakConditionClassForStep(stepClass);

        if(condition != null){
            RepeatBreakCondition breakCondition = (RepeatBreakCondition)condition.newInstance();
            breakCondition.setStepExecutionContext(context);
            boolean res = breakCondition.check();

            if(!res){
                return chain.getRepeatDetailsForStep(stepClass).getRepeatFromStep();
            }
        }

        return null;

    }
}