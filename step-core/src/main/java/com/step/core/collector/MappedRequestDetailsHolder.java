package com.step.core.collector;

import com.step.core.PluginRequest;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
import com.step.core.interceptor.event.ExecutionDecisionEvent;
import com.step.core.interceptor.event.PluginEvent;
import com.step.core.interceptor.event.impl.AutomatedRunnablePluginEvent;
import com.step.core.interceptor.event.impl.BreakExecutionDecisionEvent;
import com.step.core.interceptor.event.impl.JumpExecutionDecisionEvent;
import com.step.core.interceptor.event.impl.RepeatExecutionDecisionEvent;
import com.step.core.parameter.RequestParameterContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 7/4/14.
 */
public class MappedRequestDetailsHolder {
    private String mappedRequest;
    private String onSuccess;
    private String onFailure;
    private String expectedOutCome;
    private String stepExceptionHandler;
    private String rootStep;
    private boolean canApplyGenericSteps = true;
    private List<String> preSteps = new ArrayList<String>();
    private List<String> postSteps = new ArrayList<String>();
    private RequestParameterContainer requestParameterContainer;
    private List<String> genericParameters = new ArrayList<String>();
    private List<String> inputTypes = new ArrayList<String>();
    private List<ExecutionDecisionEvent<BreakDetails>> breakExecutionDecisionEvents = new ArrayList<ExecutionDecisionEvent<BreakDetails>>();
    private List<ExecutionDecisionEvent<JumpDetails>> jumpExecutionDecisionEvents = new ArrayList<ExecutionDecisionEvent<JumpDetails>>();
    private List<ExecutionDecisionEvent<RepeatDetails>> repeatExecutionDecisionEvents = new ArrayList<ExecutionDecisionEvent<RepeatDetails>>();
    private List<PluginEvent> autoPluginEvents = new ArrayList<PluginEvent>();

    public List<String> getPostSteps() {
        return postSteps;
    }

    public void setPostSteps(List<String> postSteps) {
        this.postSteps = postSteps;
    }

    public List<String> getPreSteps() {
        return preSteps;
    }

    public void setPreSteps(List<String> preSteps) {
        this.preSteps = preSteps;
    }

    public boolean isCanApplyGenericSteps() {
        return canApplyGenericSteps;
    }

    public void setCanApplyGenericSteps(boolean canApplyGenericSteps) {
        this.canApplyGenericSteps = canApplyGenericSteps;
    }

    public String getMappedRequest() {
        return mappedRequest;
    }

    public void setMappedRequest(String mappedRequest) {
        this.mappedRequest = mappedRequest;
    }

    public String getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(String onSuccess) {
        this.onSuccess = onSuccess;
    }

    public String getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(String onFailure) {
        this.onFailure = onFailure;
    }

    public RequestParameterContainer getRequestParameterContainer() {
        return requestParameterContainer;
    }

    public void setRequestParameterContainer(RequestParameterContainer requestParameterContainer) {
        this.requestParameterContainer = requestParameterContainer;
    }

    public void addGenericParameter(List<String> param){
        this.genericParameters = param;
    }

    public List<String> getGenericParameters(){
        return this.genericParameters;
    }

    public String getExpectedOutCome() {
        return expectedOutCome;
    }

    public void setExpectedOutCome(String expectedOutCome) {
        this.expectedOutCome = expectedOutCome;
    }

    public String getStepExceptionHandler() {
        return stepExceptionHandler;
    }

    public void setStepExceptionHandler(String stepExceptionHandler) {
        this.stepExceptionHandler = stepExceptionHandler;
    }

    public List<String> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(List<String> inputTypes) {
        this.inputTypes = inputTypes;
    }

    public void addBreakExecutionDecisionEvent(BreakDetails breakDetails, String step){
        BreakExecutionDecisionEvent breakExecutionDecisionEvent = new BreakExecutionDecisionEvent(breakDetails, step);
        this.breakExecutionDecisionEvents.add(breakExecutionDecisionEvent);
    }

    public void addJumpExecutionDecisionEvent(JumpDetails jumpDetails, String step){
        JumpExecutionDecisionEvent jumpExecutionDecisionEvent = new JumpExecutionDecisionEvent(jumpDetails, step);
        this.jumpExecutionDecisionEvents.add(jumpExecutionDecisionEvent);
    }

    public void addRepeatExecutionDecisionEvent(RepeatDetails repeatDetails, String step){
        RepeatExecutionDecisionEvent repeatExecutionDecisionEvent = new RepeatExecutionDecisionEvent(repeatDetails, step);
        this.repeatExecutionDecisionEvents.add(repeatExecutionDecisionEvent);
    }

    public List<ExecutionDecisionEvent<BreakDetails>> getBreakExecutionDecisionEvents(){
        return this.breakExecutionDecisionEvents;
    }

    public List<ExecutionDecisionEvent<JumpDetails>> getJumpExecutionDecisionEvents(){
        return this.jumpExecutionDecisionEvents;
    }

    public List<ExecutionDecisionEvent<RepeatDetails>> getRepeatExecutionDecisionEvents(){
        return this.repeatExecutionDecisionEvents;
    }

    public void addPluginEvent(PluginRequest request){
        AutomatedRunnablePluginEvent automatedRunnablePluginEvent = new AutomatedRunnablePluginEvent(request);
        this.autoPluginEvents.add(automatedRunnablePluginEvent);
    }

    public List<PluginEvent> getAutoPluginEvents(){
        return this.autoPluginEvents;
    }

    public String getRootStep() {
        return rootStep;
    }

    public void setRootStep(String rootStep) {
        this.rootStep = rootStep;
    }
}
