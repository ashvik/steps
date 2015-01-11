package com.step.core.executor.impl;

import com.step.core.ResponseLessStep;
import com.step.core.ResponsiveStep;
import com.step.core.chain.StepChain;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
import com.step.core.context.StepExecutionContext;
import com.step.core.exceptions.StepExecutionException;
import com.step.core.exceptions.handler.StepExceptionHandler;
import com.step.core.executor.StepExecutor;
import com.step.core.interceptor.event.ExecutionDecisionEvent;
import com.step.core.interceptor.event.PluginEvent;
import com.step.core.interceptor.event.StepEventType;
import com.step.core.io.ExecutionResult;
import com.step.core.utils.StepExecutionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

            //Run plugin request automatically if configured, before execution of particular step.
            try{
                List<PluginEvent> pluginEvents = context.getAutomatedPluginEvent();
                runPluginsAutomatically(pluginEvents, chain.getStepName(originalStepClass), StepEventType.PRE_EVENT);
                StepExecutionUtil.makeRichStepObject(step,
                        currentNode.getStepDefinitionHolder(), context);

                if (step instanceof ResponsiveStep) {
                    ResponsiveStep rs = (ResponsiveStep) step;
                    rs.setStepExecutionContext(context);
                    stepResult = rs.execute();
                    context.getStepInput().setInput(stepResult);
                    moveToStep = breakOrMoveStepExecutionIfApplicable(currentNode, originalStepClass, context, chain,
                            jumpTo, repeatToStep);
                } else if (step instanceof ResponseLessStep) {
                    ResponseLessStep rls = (ResponseLessStep) step;
                    rls.setStepExecutionContext(context);

                    rls.execute();

                    stepResult = null;
                    moveToStep = breakOrMoveStepExecutionIfApplicable(currentNode, originalStepClass, context, chain,
                            jumpTo, repeatToStep);
                } else {
                    try {
                        Method method = stepClass.getDeclaredMethod(currentNode.getStepDefinitionHolder().getExecutionMethod());
                        stepResult = method.invoke(step);
                    }catch (NoSuchMethodException e){
                        throw new StepExecutionException(stepClass,
                                "Step can only be the instanceof ResponsiveStep or ResponseLessStep or should have execute method implemented.");
                    }catch(InvocationTargetException e){
                        logger.error("Exception Occurred during execution of step: "+stepClass.getName()+", cause: "+e.getTargetException().getMessage());
                        if(stepExceptionHandlerClass != null){
                            handleException(context, stepExceptionHandlerClass, (Exception)e.getTargetException());
                            break;
                        }else{
                            throw (Exception)e.getTargetException();
                        }
                    }

                    if(stepResult != null){
                        context.getStepInput().setInput(stepResult);
                    }
                    moveToStep = breakOrMoveStepExecutionIfApplicable(currentNode, originalStepClass, context, chain,
                            jumpTo, repeatToStep);
                }

                //Run plugin request automatically if configured, after execution of particular step.
                runPluginsAutomatically(pluginEvents, chain.getStepName(originalStepClass), StepEventType.POST_EVENT);
            }catch(Exception e){
                logger.error("Exception Occurred during execution of step: "+stepClass.getName()+", cause: "+e.getMessage());
                if(stepExceptionHandlerClass != null){
                    handleException(context, stepExceptionHandlerClass, e);
                    break;
                }else{
                    throw e;
                }
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

    private void handleException(StepExecutionContext context, Class<StepExceptionHandler> stepExceptionHandlerClass, Exception e) throws InstantiationException, IllegalAccessException {
        StepExceptionHandler handler = stepExceptionHandlerClass.newInstance();
        handler.setStepExecutionContext(context);
        handler.handleException(e);
        context.breakStepChainExecution();
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
        String jumpTo = null;

        List<ExecutionDecisionEvent<JumpDetails>> jumpExecutionDecisionEvents = context.getJumpExecutionDecisionEvents();
        for(ExecutionDecisionEvent<JumpDetails> jumpExecutionDecisionEvent : jumpExecutionDecisionEvents){
            if(jumpExecutionDecisionEvent.getStep().equals(chain.getStepName(stepClass))){
                boolean res = jumpExecutionDecisionEvent.runEvent(context.getClassLoader());

                JumpDetails info = jumpExecutionDecisionEvent.getDecisionDetails();
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

        List<ExecutionDecisionEvent<BreakDetails>> breakExecutionDecisionEvents = context.getBreakExecutionDecisionEvents();
        for(ExecutionDecisionEvent<BreakDetails> breakExecutionDecisionEvent : breakExecutionDecisionEvents){
            if(breakExecutionDecisionEvent.getStep().equals(chain.getStepName(stepClass))){
                boolean res = breakExecutionDecisionEvent.runEvent(context.getClassLoader());

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

        List<ExecutionDecisionEvent<RepeatDetails>> repeatExecutionDecisionEvents = context.getRepeatExecutionDecisionEvents();
        for(ExecutionDecisionEvent<RepeatDetails> repeatDetailsExecutionDecisionEvent : repeatExecutionDecisionEvents){
            if(repeatDetailsExecutionDecisionEvent.getStep().equals(chain.getStepName(stepClass))){
                boolean res = repeatDetailsExecutionDecisionEvent.runEvent(context.getClassLoader());
                RepeatDetails repeatDetails = repeatDetailsExecutionDecisionEvent.getDecisionDetails();

                if (!res) {
                    return repeatDetails.getRepeatFromStep();
                }
            }
        }

        return null;

    }

    private void  runPluginsAutomatically(List<PluginEvent> pluginEvents, String currentStep, StepEventType stepEventType) throws Exception{
        for(PluginEvent pluginEvent : pluginEvents){
            if(pluginEvent.getStep().equals(currentStep) && pluginEvent.getEventType()==stepEventType){
                pluginEvent.runPlugins();
                break;
            }
        }
    }
}