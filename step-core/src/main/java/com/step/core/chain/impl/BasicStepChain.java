package com.step.core.chain.impl;

import com.step.core.chain.StepChain;
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
    private Map<Class<?>, String> stepNames = new HashMap<Class<?>, String>();
    private Map<String, StepNode> stepNodeMap = new HashMap<String, StepNode>();
    private Map<Class<?>, List<AnnotatedField>> dependenciesMap = new HashMap<Class<?>, List<AnnotatedField>>();
    private Map<Class<?>, List<AnnotatedField>> pluginDependenciesMap = new HashMap<Class<?>, List<AnnotatedField>>();
    private StepNode rootNode;
    private Set<String> visitedNodes = new HashSet<String>();
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
        this.pluginDependenciesMap.put(holder.getStepClass(), holder.getPlugins());
        this.stepNames.put(holder.getStepClass(), holder.getName());
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
    public StepNode getRootNode() {
        return rootNode;
    }

    @Override
    public StepNode getStepNodeByName(String name) {
        return stepNodeMap.get(name);
    }

    @Override
    public List<AnnotatedField> getAnnotatedPluginsForStep(Class<?> stepClass) {
        List<AnnotatedField> fields = this.pluginDependenciesMap.get(stepClass);
        return fields == null ? Collections.EMPTY_LIST : fields;
    }

    @Override
    public RequestParameterContainer getRequestParameterContainer() {
        return requestParameterContainer;
    }

    @Override
    public String getExpectedOutComeClass() {
        return classForExpectedOutCome;
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
    public List<String> getInputType() {
       return inputTypes;
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