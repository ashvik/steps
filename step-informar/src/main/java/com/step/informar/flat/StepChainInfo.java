package com.step.informar.flat;

import com.step.core.chain.StepChain;
import com.step.core.chain.impl.BasicStepChain;
import com.step.informar.flat.visitor.impl.BasicFlatInfoVisitor;

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
            currentNode = currentNode.getNextNode();
        }

        if(!postSteps.isEmpty()){
            handleInterceptorSteps(postSteps, chain, visitor,"POST");
        }
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

    private void handleInterceptorSteps(List<Class<?>> steps, StepChain chain, BasicFlatInfoVisitor visitor, String type){
        for(Class<?> stepClass : steps){
            BasicStepChain.StepNode node = new BasicStepChain.StepNode(stepClass, null, -1);
            visitor.setCurrentNode(node);
            StepInfo info = new StepInfo();
            info.setInterceptorType(type);
            info.accept(visitor);
        }
    }
}
