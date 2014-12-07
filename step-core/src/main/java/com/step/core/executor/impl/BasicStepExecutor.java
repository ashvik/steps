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
import com.step.core.exceptions.handler.StepExceptionHandler;
import com.step.core.executor.StepExecutor;
import com.step.core.io.ExecutionResult;
import com.step.core.utils.StepExecutionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: amishra Date: 5/25/13 Time: 8:46 PM To
 * change this template use File | Settings | File Templates.
 */
public class BasicStepExecutor implements StepExecutor {
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public ExecutionResult execute(StepChain chain, StepExecutionContext context)
            throws Exception {
        ExecutionResult result = null;
        Object stepResult = null;
        String jumpTo = null;
        String repeatToStep = null;
        BasicStepChain.StepNode currentNode = chain.getRootNode();
        BasicStepChain.StepNode moveToStep = null;
        String expectedOutCome = chain.getExpectedOutComeClass();
        ClassLoader classLoader = context.getClassLoader();
        Class<StepExceptionHandler> stepExceptionHandlerClass = null;
        Class<?> originalStepClass = null;
        if(chain.getStepExceptionHandler() != null){
            stepExceptionHandlerClass = StepExecutionUtil.loadClass(chain.getStepExceptionHandler().getName(), classLoader);
        }


        while (true) {
            Object step = null;
            originalStepClass = currentNode.getStepClass();
            Class<?> stepClass = StepExecutionUtil.loadClass(currentNode.getStepClass().getName(), classLoader);

            if (context.hasStepChainExecutionBroken()) {
                break;
            }
            step = stepClass.newInstance();
            StepExecutionUtil.makeRichStepObject(step,
                    chain.getDependenciesForStep(stepClass), context);

            if (step instanceof ResponsiveStep) {
                ResponsiveStep rs = (ResponsiveStep) step;
                rs.setStepExecutionContext(context);
                try{
                    stepResult = rs.execute();
                }catch(Exception e){
                    logger.error("Exception Occurred during execution of step: "+stepClass.getName()+", cause: "+e.getMessage());
                    if(stepExceptionHandlerClass != null){
                        StepExceptionHandler handler = stepExceptionHandlerClass.newInstance();
                        handler.setStepExecutionContext(context);
                        handler.handleException(e);
                        context.breakStepChainExecution();
                        break;
                    }else{
                        throw e;
                    }
                }
                context.getStepInput().setInput(stepResult);
                moveToStep = breakOrMoveStepExecutionIfApplicable(currentNode, originalStepClass, context, chain,
                        jumpTo, repeatToStep);
            } else if (step instanceof ResponseLessStep) {
                ResponseLessStep rls = (ResponseLessStep) step;
                rls.setStepExecutionContext(context);
                try{
                    rls.execute();
                }catch(Exception e){
                    logger.error("Exception Occurred during execution of step: "+stepClass.getName()+", cause: "+e.getMessage());
                    if(stepExceptionHandlerClass != null){
                        StepExceptionHandler handler = stepExceptionHandlerClass.newInstance();
                        handler.setStepExecutionContext(context);
                        handler.handleException(e);
                        context.breakStepChainExecution();
                        break;
                    }else{
                        throw e;
                    }
                }
                stepResult = null;
                moveToStep = breakOrMoveStepExecutionIfApplicable(currentNode, originalStepClass, context, chain,
                        jumpTo, repeatToStep);
            } else {
                throw new StepExecutionException(stepClass,
                        "Step can only be the instanceof ResponsiveStep or ResponseLessStep.");
            }

            if(moveToStep != null){
                currentNode = moveToStep;
            }else if (currentNode.getNextNode() != null) {
                currentNode = currentNode.getNextNode();
            } else {
                break;
            }
        }

        if(expectedOutCome != null){
            String[] tokens = expectedOutCome.split(" of ");
            if(tokens.length == 2){
                Class type = StepExecutionUtil.loadClass(tokens[1], classLoader);
                if(tokens[0].contains("List")){
                     result = new ExecutionResult(context.getStepInput().getListTypeInput(type), context.getAttributes());
                }else{
                     result = new ExecutionResult(context.getStepInput().getSetTypeInput(type), context.getAttributes());
                }
            }else{
                result = new ExecutionResult(context.getInput(StepExecutionUtil.loadClass(expectedOutCome, classLoader)), context.getAttributes());
            }
        }else{
            result = new ExecutionResult(stepResult, context.getAttributes());
        }

        return result;
    }

    private BasicStepChain.StepNode breakOrMoveStepExecutionIfApplicable(BasicStepChain.StepNode currentNode,
                                                                         Class<?> stepClass,
                                                                         StepExecutionContext context, StepChain chain, String jumpTo,
                                                                         String repeatToStep) throws IllegalAccessException,
            InstantiationException, ClassNotFoundException {
        jumpTo = checkJumpCondition(chain, stepClass, context);
        if (jumpTo == null || jumpTo.isEmpty()) {
            repeatToStep = checkRepeatBreakCondition(chain, stepClass, context);
            if (repeatToStep == null || repeatToStep.isEmpty()) {
                checkBreakCondition(chain, stepClass, context);
            }
        }

        currentNode = moveToStepIfApplicable(currentNode, stepClass, chain, jumpTo,
                repeatToStep);

        return currentNode;
    }

    private BasicStepChain.StepNode moveToStepIfApplicable(BasicStepChain.StepNode currentNode,
                                                           Class<?> stepClass, StepChain chain, String jumpTo,
                                                           String repeatToStep) {
        BasicStepChain.StepNode moveToStep = null;
        if (jumpTo != null && !jumpTo.isEmpty()) {
            int currentSequence = currentNode.getStepSequence();
            moveToStep = chain.getStepNodeByName(jumpTo);
            if (moveToStep.getStepSequence() < currentSequence) {
                throw new StepExecutionException(stepClass,
                        "Jumpers can move only in forward direction.");
            }
        } else if (repeatToStep != null && !repeatToStep.isEmpty()) {
            int currentSequence = currentNode.getStepSequence();
            moveToStep = chain.getStepNodeByName(repeatToStep);
            if (moveToStep.getStepSequence() > currentSequence) {
                throw new StepExecutionException(stepClass,
                        "Repeaters can move only in reverse direction.");
            }
        }

        return moveToStep;
    }

    private String checkJumpCondition(StepChain chain, Class<?> stepClass,
                                      StepExecutionContext context) throws IllegalAccessException,
            InstantiationException {
        List<Class<?>> condition = chain.getJumpConditionClassForStep(stepClass);
        String jumpTo = null;

        if (condition != null) {
            for(int i=0 ; i<condition.size() ; i++){
                JumpCondition jumpCondition = (JumpCondition) condition.get(i)
                        .newInstance();
                jumpCondition.setStepExecutionContext(context);
                boolean res = jumpCondition.check();

                JumpDetails info = chain.getJumpDetailsForStep(stepClass).get(i);
                if (info == null) {
                    throw new IllegalStateException(
                            "Jump info should be configured if condition is present.");
                }

                if (res) {
                    jumpTo = info.getOnSuccessJumpStep();
                } else {
                    jumpTo = info.getOnFailureJumpStep();
                }

                if(jumpTo != null && !jumpTo.isEmpty()){
                    break;
                }
            }
        }

        return jumpTo;
    }

    private void checkBreakCondition(StepChain chain, Class<?> stepClass,
                                     StepExecutionContext context) throws IllegalAccessException,
            InstantiationException, ClassNotFoundException {
        List<Class<?>> condition = chain.getBreakConditionClassForStep(stepClass);
        ClassLoader classLoader = context.getClassLoader();

        if (condition != null) {
            for(int i=0 ; i<condition.size() ; i++){
                BreakCondition breakCondition = classLoader != null ? (BreakCondition)StepExecutionUtil.loadClass(condition.get(i).getName(),classLoader).newInstance():
                        (BreakCondition) condition.get(i)
                        .newInstance();
                breakCondition.setStepExecutionContext(context);
                boolean res = breakCondition.check();

                if (res) {
                    context.breakStepChainExecution();
                    break;
                }
            }
        }
    }

    private String checkRepeatBreakCondition(StepChain chain,
                                             Class<?> stepClass, StepExecutionContext context)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class condition = chain.getRepeatBreakConditionClassForStep(stepClass);
        ClassLoader classLoader = context.getClassLoader();

        if (condition != null) {
            RepeatBreakCondition breakCondition = classLoader != null ? (RepeatBreakCondition)StepExecutionUtil.loadClass(condition.getName(),classLoader).newInstance():
                    (RepeatBreakCondition) condition.newInstance();
            breakCondition.setStepExecutionContext(context);
            boolean res = breakCondition.check();

            if (!res) {
                return chain.getRepeatDetailsForStep(stepClass)
                        .getRepeatFromStep();
            }
        }

        return null;

    }
}