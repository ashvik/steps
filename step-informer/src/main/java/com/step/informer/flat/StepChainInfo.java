package com.step.informer.flat;

import com.step.core.chain.StepChain;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.utils.AnnotatedField;
import com.step.core.utils.PluginAnnotatedField;
import com.step.informer.flat.visitor.impl.BasicFlatInfoVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 6/21/14.
 */
public class StepChainInfo {
    private List<StepInfo> defaultExecutionChain = new ArrayList<StepInfo>();
    private List<JumpInfo> jumpers = new ArrayList<JumpInfo>();
    private List<BreakInfo> breakers = new ArrayList<BreakInfo>();
    private List<RepeatInfo> repeaters = new ArrayList<RepeatInfo>();
    private List<String> inputs = new ArrayList<String>();
    private List<String> plugIns = new ArrayList<String>();
    private String expectedOutCome;
    private String alias;

    public StepChainInfo(){}

    public void prepareStepChainInfo(StepChain chain){
        List<Class<?>> preSteps = chain.getPreSteps();
        List<Class<?>> postSteps = chain.getPostStep();
        BasicStepChain.StepNode currentNode = chain.getRootNode();
        BasicFlatInfoVisitor visitor = new BasicFlatInfoVisitor(this, chain);

        if(!preSteps.isEmpty()){
            handleInterceptorSteps(preSteps, chain, visitor,"PRE");
        }

        while(currentNode != null){
            visitor.setCurrentNode(currentNode);
            StepInfo info = new StepInfo();
            info.accept(visitor);
            List<AnnotatedField> plg = chain.getAnnotatedPluginsForStep(currentNode.getStepClass());

            for(AnnotatedField plugin : plg){
                PluginAnnotatedField pluginAnnotatedField = (PluginAnnotatedField)plugin;
                plugIns.add(pluginAnnotatedField.getRequest());
            }
            currentNode = currentNode.getNextNode();

        }

        if(!postSteps.isEmpty()){
            handleInterceptorSteps(postSteps, chain, visitor,"POST");
        }

        if(chain.getInputType() !=  null){
            for(String cls : chain.getInputType()){
                inputs.add(cls);
            }
        }

        expectedOutCome = chain.getExpectedOutComeClass();
    }

    public void addStepInfo(StepInfo stepInfo){
        this.defaultExecutionChain.add(stepInfo);
    }

    public void addJumpInfo(JumpInfo jumpInfo){
        this.jumpers.add(jumpInfo);
    }

    public void addBreakInfo(BreakInfo breakInfo){
        this.breakers.add(breakInfo);
    }

    public void addRepeatInfo(RepeatInfo repeatInfo){
        this.repeaters.add(repeatInfo);
    }

    public List<StepInfo> getDefaultExecutionChain() {
        return defaultExecutionChain;
    }

    public List<JumpInfo> getJumpers() {
        return jumpers;
    }

    public List<BreakInfo> getBreakers() {
        return breakers;
    }

    public List<RepeatInfo> getRepeaters() {
        return repeaters;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public List<String> getPlugIns() {
        return plugIns;
    }

    public String getExpectedOutCome() {
        return expectedOutCome;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    private void handleInterceptorSteps(List<Class<?>> steps, StepChain chain, BasicFlatInfoVisitor visitor, String type){
        for(Class<?> stepClass : steps){

            BasicStepChain.StepNode node = new BasicStepChain.StepNode(chain.getInterceptorStepDefinition(stepClass), null, -1);
            visitor.setCurrentNode(node);
            StepInfo info = new StepInfo();
            info.setInterceptorType(type);
            info.accept(visitor);
        }
    }
}
