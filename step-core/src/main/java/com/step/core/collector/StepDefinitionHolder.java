package com.step.core.collector;

import com.step.core.enums.GenericStepType;
import com.step.core.utils.AnnotatedField;
import com.step.core.utils.InputAsListAnnotatedField;
import com.step.core.utils.InputAsSetAnnotatedField;
import com.step.core.utils.ParameterAnnotatedField;

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
    private String executionMethod;
    private GenericStepType genericStepType;
    private List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
    private List<AnnotatedField> plugins = new ArrayList<AnnotatedField>();
    private List<AnnotatedField> annotatedInputs = new ArrayList<AnnotatedField>();
    private List<InputAsListAnnotatedField> inputAsListAnnotatedFields = new ArrayList<InputAsListAnnotatedField>();
    private List<InputAsSetAnnotatedField> inputAsSetAnnotatedFields = new ArrayList<InputAsSetAnnotatedField>();
    private List<ParameterAnnotatedField> parameterAnnotatedFields = new ArrayList<ParameterAnnotatedField>();
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

    public Set<String> getScopes(){
        return new HashSet<String>(this.scopes.keySet());
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

    public List<AnnotatedField> getAnnotatedInputs() {
        return annotatedInputs;
    }

    public void setAnnotatedInputs(List<AnnotatedField> annotatedInputs) {
        this.annotatedInputs = annotatedInputs;
    }

    public List<InputAsListAnnotatedField> getInputAsListAnnotatedFields() {
        return inputAsListAnnotatedFields;
    }

    public void setInputAsListAnnotatedFields(List<InputAsListAnnotatedField> inputAsListAnnotatedFields) {
        this.inputAsListAnnotatedFields = inputAsListAnnotatedFields;
    }

    public List<InputAsSetAnnotatedField> getInputAsSetAnnotatedFields() {
        return inputAsSetAnnotatedFields;
    }

    public void setInputAsSetAnnotatedFields(List<InputAsSetAnnotatedField> inputAsSetAnnotatedFields) {
        this.inputAsSetAnnotatedFields = inputAsSetAnnotatedFields;
    }

    public List<ParameterAnnotatedField> getParameterAnnotatedFields() {
        return parameterAnnotatedFields;
    }

    public void setParameterAnnotatedFields(List<ParameterAnnotatedField> parameterAnnotatedFields) {
        this.parameterAnnotatedFields = parameterAnnotatedFields;
    }

    public String getExecutionMethod() {
        return executionMethod;
    }

    public void setExecutionMethod(String executionMethod) {
        this.executionMethod = executionMethod;
    }

    public void merge(StepDefinitionHolder other){
        this.executionMethod = other.executionMethod == null ? this.executionMethod : other.executionMethod;
        this.genericStepType = other.genericStepType == null ? this.genericStepType : other.genericStepType;
        this.scopes = other.scopes.isEmpty() ? this.scopes : other.scopes;
        this.annotatedFields = other.annotatedFields.isEmpty() ? this.annotatedFields : other.annotatedFields;
        this.plugins = other.plugins.isEmpty() ? this.plugins : other.plugins;
        this.annotatedInputs = other.annotatedInputs.isEmpty() ? this.annotatedInputs : other.annotatedInputs;
        this.inputAsListAnnotatedFields = other.inputAsListAnnotatedFields.isEmpty() ? this.inputAsListAnnotatedFields : other.inputAsListAnnotatedFields;
        this.inputAsSetAnnotatedFields = other.inputAsSetAnnotatedFields.isEmpty() ? this.inputAsSetAnnotatedFields : other.inputAsSetAnnotatedFields;
        this.parameterAnnotatedFields = other.parameterAnnotatedFields.isEmpty() ? this.parameterAnnotatedFields : other.parameterAnnotatedFields;
    }
}
