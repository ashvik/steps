package com.step.core.chain.impl;

import com.step.core.chain.StepChain;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.utils.AnnotatedField;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/26/13
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class BasicStepChain implements StepChain {
    private List<Class<?>> steps = new ArrayList<Class<?>>();
    private List<Class<?>> preSteps = new ArrayList<Class<?>>();
    private List<Class<?>> postSteps = new ArrayList<Class<?>>();
    private Map<Class<?>, Class<?>> stepToJumpConditionMap = new HashMap<Class<?>, Class<?>>();
    private Map<Class<?>, Class<?>> stepToBreakConditionMap = new HashMap<Class<?>, Class<?>>();
    private Map<Class<?>, Class<?>> stepToRepeatConditionMap = new HashMap<Class<?>, Class<?>>();
    private Map<Class<?>, String> stepNames = new HashMap<Class<?>, String>();
    private Map<Class<?>, JumpDetails> stepJumpInfoMap = new HashMap<Class<?>, JumpDetails>();
    private Map<Class<?>, RepeatDetails> stepRepeatInfoMap = new HashMap<Class<?>, RepeatDetails>();
    private Map<String, StepNode> stepNodeMap = new HashMap<String, StepNode>();
    private Map<Class<?>, List<AnnotatedField>> dependenciesMap = new HashMap<Class<?>, List<AnnotatedField>>();
    private StepNode rootNode;
    private Set<String> visitedNodes = new HashSet<String>();

    @Override
    public List<Class<?>> getPreSteps() {
        return this.preSteps;
    }

    @Override
    public List<Class<?>> getPostStep() {
        return this.postSteps;
    }

    @Override
    public List<Class<?>> getSteps() {
        return this.steps;
    }

    @Override
    public List<AnnotatedField> getDependenciesForStep(Class<?> stepClass) {
        List<AnnotatedField> fields = this.dependenciesMap.get(stepClass);
        return fields == null ? Collections.EMPTY_LIST : fields;
    }

    @Override
    public void addStep(StepDefinitionHolder holder, String request) {
        if(rootNode == null){
            rootNode = new StepNode(holder.getStepClass(),null,1);
            stepNodeMap.put(holder.getName(), rootNode);
            populateRequiredAssets(holder, request);
            visitedNodes.add(holder.getName());
        }else{
            if(!visitedNodes.contains(holder.getName())){
                StepNode currentNode = rootNode;
                StepNode next = rootNode.nextNode;
                int seq = currentNode.getStepSequence();
                while(true){
                    if(next == null){
                        seq++;
                        next = new StepNode(holder.getStepClass(), null, seq);
                        stepNodeMap.put(holder.getName(), next);
                        currentNode.setNextNode(next);
                        populateRequiredAssets(holder, request);
                        visitedNodes.add(holder.getName());
                        break;
                    }else{
                        currentNode = next;
                        next = currentNode.getNextNode();
                        seq = currentNode.getStepSequence();
                    }
                }
            }
        }
    }

    private void populateRequiredAssets(StepDefinitionHolder holder, String request){
        this.dependenciesMap.put(holder.getStepClass(), holder.getAnnotatedFields());
        this.stepNames.put(holder.getStepClass(), holder.getName());
        JumpDetails details = holder.getJumpDetails(request);
        if(details != null){
            this.stepToJumpConditionMap.put(holder.getStepClass(), details.getConditionClass());
            this.stepJumpInfoMap.put(holder.getStepClass(), details);
        }
        BreakDetails breakDetails = holder.getBreakDetails(request);
        if(breakDetails != null){
            this.stepToBreakConditionMap.put(holder.getStepClass(), breakDetails.getConditionClass());
        }
        RepeatDetails repeatDetails = holder.getRepeatDetails(request);
        if(repeatDetails != null){
            this.stepToRepeatConditionMap.put(holder.getStepClass(), repeatDetails.getConditionClass());
            this.stepRepeatInfoMap.put(holder.getStepClass(), repeatDetails);
        }
    }

    @Override
    public void addInterceptorStep(StepDefinitionHolder stepDefinitionHolder, boolean isPreStep) {
        if(isPreStep){
            this.preSteps.add(stepDefinitionHolder.getStepClass());
        }else{
            this.postSteps.add(stepDefinitionHolder.getStepClass());
        }

        this.dependenciesMap.put(stepDefinitionHolder.getStepClass(), stepDefinitionHolder.getAnnotatedFields());
    }

    @Override
    public String getStepName(Class<?> stepClass) {
        return stepNames.get(stepClass);
    }

    @Override
    public Class<?> getJumpConditionClassForStep(Class<?> step) {
        return this.stepToJumpConditionMap.get(step);
    }

    @Override
    public JumpDetails getJumpDetailsForStep(Class<?> step) {
        return stepJumpInfoMap.get(step);
    }

    @Override
    public Class<?> getBreakConditionClassForStep(Class<?> step) {
        return this.stepToBreakConditionMap.get(step);
    }

    @Override
    public Class<?> getRepeatBreakConditionClassForStep(Class<?> step) {
        return this.stepToRepeatConditionMap.get(step);
    }

    @Override
    public RepeatDetails getRepeatDetailsForStep(Class<?> step) {
        return stepRepeatInfoMap.get(step);
    }

    @Override
    public StepNode getRootNode() {
        return rootNode;
    }

    @Override
    public StepNode getStepNodeByName(String name) {
        return stepNodeMap.get(name);
    }

    public static final class StepNode{
        private int stepSequence;
        private Class<?> stepClass;
        private StepNode nextNode;

        public StepNode(Class stepClass, StepNode nextNode, int stepSequence){
            this.stepClass = stepClass;
            this.nextNode = nextNode;
            this.stepSequence = stepSequence;
        }

        public Class<?> getStepClass() {
            return stepClass;
        }

        public StepNode getNextNode() {
            return nextNode;
        }

        public int getStepSequence() {
            return stepSequence;
        }

        public void setNextNode(StepNode nextNode){
            this.nextNode = nextNode;
        }
    }
}
