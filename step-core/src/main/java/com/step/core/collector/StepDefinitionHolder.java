package com.step.core.collector;

import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
import com.step.core.enums.GenericStepType;
import com.step.core.utils.AnnotatedField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class StepDefinitionHolder {
    private Class<?> stepClass;
    private String nextStep;
    private String name;
    private String mappedRequest;
    private String onSuccess;
    private String onFailure;
    private boolean canApplyGenericSteps = true;
    private GenericStepType genericStepType;
    private List<String> preSteps = new ArrayList<String>();
    private List<String> postSteps = new ArrayList<String>();
    private List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
    private Map<String, String> scopes = new HashMap<String, String>();
    private Map<String, JumpDetails> jumpDetailsMap = new HashMap<String, JumpDetails>();
    private Map<String, BreakDetails> breakDetailsMap = new HashMap<String, BreakDetails>();
    private Map<String, RepeatDetails> repeatDetailsMap = new HashMap<String, RepeatDetails>();

    public StepDefinitionHolder(String name){
        this.name = name;
    }

    public StepDefinitionHolder(String name, Class<?> stepClass){
        this.name = name;
        this.stepClass = stepClass;
    }

    public StepDefinitionHolder(String name, String nextStep, Class<?> stepClass){
        this.name = name;
        this.stepClass = stepClass;
        this.nextStep = nextStep;
    }

    public Class getStepClass(){
        return this.stepClass;
    }

    public String getNextStep(){
        return this.nextStep;
    }

    public String getName(){
        return this.name;
    }

    public void setGenericStepType(GenericStepType genericStepType){
        this.genericStepType = genericStepType;
    }

    public GenericStepType getGenericStepType(){
        return this.genericStepType;
    }

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

    public void addScope(String scope, String nextStep){
        this.scopes.put(scope, nextStep);
    }

    public String getNextStepForScope(String scope){
        return this.scopes.get(scope);
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

    public List<AnnotatedField> getAnnotatedFields() {
        return annotatedFields;
    }

    public void setAnnotatedFields(List<AnnotatedField> annotatedFields) {
        this.annotatedFields = annotatedFields;
    }

    public void addJumpDetails(String request, JumpDetails jumpDetails){
        this.jumpDetailsMap.put(request, jumpDetails);
    }

    public JumpDetails getJumpDetails(String request){
        return this.jumpDetailsMap.get(request);
    }

    public void addBreakDetails(String request, BreakDetails breakDetails){
        this.breakDetailsMap.put(request, breakDetails);
    }

    public BreakDetails getBreakDetails(String request){
        return this.breakDetailsMap.get(request);
    }

    public void addRepeatDetails(String request, RepeatDetails repeatDetails){
        this.repeatDetailsMap.put(request, repeatDetails);
    }

    public RepeatDetails getRepeatDetails(String request){
        return this.repeatDetailsMap.get(request);
    }

    public void merge(StepDefinitionHolder other){
        this.mappedRequest = other.mappedRequest == null ? this.mappedRequest : other.mappedRequest ;
        this.genericStepType = other.genericStepType == null ? this.genericStepType : other.genericStepType;
        this.canApplyGenericSteps = !this.canApplyGenericSteps ? this.canApplyGenericSteps : other.canApplyGenericSteps;
        this.preSteps = other.preSteps.isEmpty() ? this.preSteps : other.preSteps;
        this.postSteps = other.postSteps.isEmpty() ? this.postSteps : other.postSteps;
        this.scopes = other.scopes.isEmpty() ? this.scopes : other.scopes;
        this.jumpDetailsMap.putAll(other.jumpDetailsMap);
        this.breakDetailsMap.putAll(other.breakDetailsMap);
        this.repeatDetailsMap.putAll(other.repeatDetailsMap);
    }

    public StepDefinitionHolder cloneWithDifferentMappedRequest(String mappedRequest){
        StepDefinitionHolder cloned = new StepDefinitionHolder(this.name, this.getStepClass());
        cloned.mappedRequest = mappedRequest;
        cloned.genericStepType = this.genericStepType;
        cloned.canApplyGenericSteps = this.canApplyGenericSteps;
        cloned.preSteps = this.preSteps;
        cloned.postSteps = this.postSteps;
        cloned.scopes = this.scopes;
        cloned.jumpDetailsMap = this.jumpDetailsMap;
        cloned.breakDetailsMap = this.breakDetailsMap;
        cloned.repeatDetailsMap = this.repeatDetailsMap;
        cloned.nextStep = this.nextStep;

        return cloned;
    }
}
