package com.step.core.chain.impl;

import com.step.core.PluginRequest;
import com.step.core.chain.StepChain;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.exceptions.handler.StepExceptionHandler;
import com.step.core.parameter.RequestParameterContainer;
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
    private Map<Class<?>, List<Class<?>>> stepToJumpConditionMap = new HashMap<Class<?>, List<Class<?>>>();
    private Map<Class<?>, List<Class<?>>> stepToBreakConditionMap = new HashMap<Class<?>, List<Class<?>>>();
    private Map<Class<?>, Class<?>> stepToRepeatConditionMap = new HashMap<Class<?>, Class<?>>();
    private Map<Class<?>, String> stepNames = new HashMap<Class<?>, String>();
    private Map<Class<?>, List<JumpDetails>> stepJumpInfoMap = new HashMap<Class<?>, List<JumpDetails>>();
    private Map<Class<?>, RepeatDetails> stepRepeatInfoMap = new HashMap<Class<?>, RepeatDetails>();
    private Map<String, StepNode> stepNodeMap = new HashMap<String, StepNode>();
    private Map<Class<?>, List<AnnotatedField>> dependenciesMap = new HashMap<Class<?>, List<AnnotatedField>>();
    private StepNode rootNode;
    private Set<String> visitedNodes = new HashSet<String>();
    private List<PluginRequest> pluginRequestsNew = new ArrayList<PluginRequest>();
    private RequestParameterContainer requestParameterContainer;
    private String classForExpectedOutCome;
    private String stepExceptionHandler;
    private List<String> inputTypes;

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
        MappedRequestDetailsHolder requestDetailsHolder = holder.getMappedRequestDetailsHolder();
        if(requestDetailsHolder != null){
            List<JumpDetails> details = holder.getJumpDetails(request);
            if(details != null){
                for(JumpDetails jumpDetail : details){
                    List<Class<?>> condClass = this.stepToJumpConditionMap.get(holder.getStepClass());
                    if(condClass == null){
                        condClass = new ArrayList<Class<?>>();
                        this.stepToJumpConditionMap.put(holder.getStepClass(), condClass);
                    }

                    condClass.add(jumpDetail.getConditionClass());

                    List<JumpDetails> jumpDetailsList = this.stepJumpInfoMap.get(holder.getStepClass());
                    if(jumpDetailsList == null){
                        jumpDetailsList = new ArrayList<JumpDetails>();
                        this.stepJumpInfoMap.put(holder.getStepClass(), jumpDetailsList);
                    }

                    jumpDetailsList.add(jumpDetail);
                }
            }
            List<BreakDetails> breakDetails = holder.getBreakDetails(request);
            if(breakDetails != null){
                for(BreakDetails bds : breakDetails){
                    List<Class<?>> classes = this.stepToBreakConditionMap.get(holder.getStepClass());
                    if(classes == null){
                        classes = new ArrayList<Class<?>>();
                        this.stepToBreakConditionMap.put(holder.getStepClass(), classes);
                    }

                    classes.add(bds.getConditionClass());
                }
            }
            RepeatDetails repeatDetails = holder.getRepeatDetails(request);
            if(repeatDetails != null){
                this.stepToRepeatConditionMap.put(holder.getStepClass(), repeatDetails.getConditionClass());
                this.stepRepeatInfoMap.put(holder.getStepClass(), repeatDetails);
            }
        }
    }

    @Override
    public void addInterceptorStep(StepDefinitionHolder stepDefinitionHolder, String request, boolean isPreStep) {
        if(isPreStep){
            this.preSteps.add(stepDefinitionHolder.getStepClass());
            populateRequiredAssets(stepDefinitionHolder, request);
        }else{
            this.postSteps.add(stepDefinitionHolder.getStepClass());
            populateRequiredAssets(stepDefinitionHolder, request);
        }

        this.dependenciesMap.put(stepDefinitionHolder.getStepClass(), stepDefinitionHolder.getAnnotatedFields());
    }

    @Override
    public String getStepName(Class<?> stepClass) {
        return stepNames.get(stepClass);
    }

    @Override
    public List<Class<?>> getJumpConditionClassForStep(Class<?> step) {
        return this.stepToJumpConditionMap.get(step);
    }

    @Override
    public List<JumpDetails> getJumpDetailsForStep(Class<?> step) {
        return stepJumpInfoMap.get(step);
    }

    @Override
    public List<Class<?>> getBreakConditionClassForStep(Class<?> step) {
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

    @Override
    public void setPluginRequests(List<PluginRequest> pluginRequests){
        this.pluginRequestsNew = pluginRequests;
    }

    @Override
    public List<PluginRequest> getPluginRequests(){
        return this.pluginRequestsNew;
    }

    @Override
    public RequestParameterContainer getRequestParameterContainer() {
        return requestParameterContainer;
    }

    @Override
    public Class<?> getExpectedOutComeClass() {
        Class cls = null;

        if(classForExpectedOutCome != null){
            try {
                cls = Class.forName(classForExpectedOutCome);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return cls;
    }

    @Override
    public void setExpectedOutCome(String expectedOutCome) {
        this.classForExpectedOutCome = expectedOutCome;
    }

    @Override
    public Class<StepExceptionHandler> getStepExceptionHandler() {
        Class cls = null;

        if(stepExceptionHandler != null){
            try {
                cls = Class.forName(stepExceptionHandler);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return cls;
    }

    @Override
    public void setStepExceptionHandler(String stepExceptionHandler) {
        this.stepExceptionHandler = stepExceptionHandler;
    }

    @Override
    public void setInputTypes(List<String> inputTypes) {
        this.inputTypes = inputTypes;
    }

    @Override
    public List<Class> getInputType() {
        List<Class> classes = new ArrayList<Class>();

        for(String inputType : inputTypes){
            try {
                classes.add(Class.forName(inputType));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    public void setRequestParameterContainer(RequestParameterContainer requestParameterContainer) {
        this.requestParameterContainer = requestParameterContainer;
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