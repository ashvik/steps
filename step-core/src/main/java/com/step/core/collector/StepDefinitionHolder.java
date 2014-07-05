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
    private GenericStepType genericStepType;
    private List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
    private Map<String, String> scopes = new HashMap<String, String>();
    private MappedRequestDetailsHolder mappedRequestDetailsHolder;
    private Map<String, List<JumpDetails>> jumpDetailsMap = new HashMap<String, List<JumpDetails>>();
    private Map<String, List<BreakDetails>> breakDetailsMap = new HashMap<String, List<BreakDetails>>();
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

    public void addScope(String scope, String nextStep){
        this.scopes.put(scope, nextStep);
    }

    public String getNextStepForScope(String scope){
        return this.scopes.get(scope);
    }

    public List<AnnotatedField> getAnnotatedFields() {
        return annotatedFields;
    }

    public void setAnnotatedFields(List<AnnotatedField> annotatedFields) {
        this.annotatedFields = annotatedFields;
    }

    public MappedRequestDetailsHolder getMappedRequestDetailsHolder() {
        return mappedRequestDetailsHolder;
    }

    public void setMappedRequestDetailsHolder(MappedRequestDetailsHolder mappedRequestDetailsHolder) {
        this.mappedRequestDetailsHolder = mappedRequestDetailsHolder;
    }

    public void addJumpDetails(String request, JumpDetails jumpDetails){
        List<JumpDetails> details = this.jumpDetailsMap.get(request);
        if(details == null){
            details = new ArrayList<JumpDetails>();
            this.jumpDetailsMap.put(request, details);
        }
        details.add(jumpDetails);
    }

    public List<JumpDetails> getJumpDetails(String request){
        return this.jumpDetailsMap.get(request);
    }

    public void addBreakDetails(String request, BreakDetails breakDetails){
        List<BreakDetails> details = this.breakDetailsMap.get(request);
        if(details == null){
            details = new ArrayList<BreakDetails>();
            this.breakDetailsMap.put(request, details);
        }
        details.add(breakDetails);
    }

    public List<BreakDetails> getBreakDetails(String request){
        return this.breakDetailsMap.get(request);
    }

    public void addRepeatDetails(String request, RepeatDetails repeatDetails){
        this.repeatDetailsMap.put(request, repeatDetails);
    }

    public RepeatDetails getRepeatDetails(String request){
        return this.repeatDetailsMap.get(request);
    }

    public void merge(StepDefinitionHolder other){
        this.genericStepType = other.genericStepType == null ? this.genericStepType : other.genericStepType;
        this.scopes = other.scopes.isEmpty() ? this.scopes : other.scopes;
        this.jumpDetailsMap.putAll(other.jumpDetailsMap);
        this.breakDetailsMap.putAll(other.breakDetailsMap);
        this.repeatDetailsMap.putAll(other.repeatDetailsMap);
        this.mappedRequestDetailsHolder.merge(other.getMappedRequestDetailsHolder());
    }

    public StepDefinitionHolder cloneWithDifferentMappedRequest(String mappedRequest){
        StepDefinitionHolder cloned = new StepDefinitionHolder(this.name, this.getStepClass());
        cloned.genericStepType = this.genericStepType;
        cloned.scopes = this.scopes;
        cloned.nextStep = this.nextStep;
        cloned.jumpDetailsMap = this.jumpDetailsMap;
        cloned.breakDetailsMap = this.breakDetailsMap;
        cloned.repeatDetailsMap = this.repeatDetailsMap;
        cloned.mappedRequestDetailsHolder = this.mappedRequestDetailsHolder.cloneWithDifferentMappedRequest(mappedRequest);

        return cloned;
    }
}
