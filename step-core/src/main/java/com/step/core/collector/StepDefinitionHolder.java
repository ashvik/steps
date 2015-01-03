package com.step.core.collector;

import com.step.core.enums.GenericStepType;
import com.step.core.utils.AnnotatedField;

import java.util.*;

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
    private List<AnnotatedField> plugins = new ArrayList<AnnotatedField>();
    private Map<String, String> scopes = new HashMap<String, String>();

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

    public Set<String> getNextStepsForAllApplicableScopes(){
        return new HashSet<String>(this.scopes.values());
    }

    public List<AnnotatedField> getAnnotatedFields() {
        return annotatedFields;
    }

    public void setAnnotatedFields(List<AnnotatedField> annotatedFields) {
        this.annotatedFields = annotatedFields;
    }

    public List<AnnotatedField> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<AnnotatedField> plugins) {
        this.plugins = plugins;
    }

    public void merge(StepDefinitionHolder other){
        this.genericStepType = other.genericStepType == null ? this.genericStepType : other.genericStepType;
        this.scopes = other.scopes.isEmpty() ? this.scopes : other.scopes;
        this.annotatedFields = other.annotatedFields.isEmpty() ? this.annotatedFields : other.annotatedFields;
        this.plugins = other.plugins.isEmpty() ? this.plugins : other.plugins;
    }
}
