package com.step.informar.flat.visitor.impl;

import com.step.core.chain.StepChain;
import com.step.core.chain.impl.BasicStepChain;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.chain.repeater.RepeatDetails;
import com.step.informar.flat.BreakInfo;
import com.step.informar.flat.ConditionInfo;
import com.step.informar.flat.JumpInfo;
import com.step.informar.flat.RepeatInfo;
import com.step.informar.flat.StepChainInfo;
import com.step.informar.flat.StepInfo;
import com.step.informar.flat.visitor.FlatInfoVisitor;

/**
 * Created by amishra on 6/21/14.
 */
public class BasicFlatInfoVisitor implements FlatInfoVisitor {
    private StepChainInfo stepChainInfo;
    private StepChain stepChain;
    private BasicStepChain.StepNode currentNode;

    public BasicFlatInfoVisitor(StepChainInfo stepChainInfo, StepChain stepChain){
        this.stepChainInfo = stepChainInfo;
        this.stepChain = stepChain;
    }

    @Override
    public void visitStepInfo(StepInfo stepInfo) {
        Class<?> stepClass = currentNode.getStepClass();
        String stepName = stepChain.getStepName(stepClass);
        String nextStepName = null;
        if(currentNode.getNextNode() != null){
            nextStepName = stepChain.getStepName(currentNode.getNextNode().getStepClass());
        }
        stepInfo.setStepName(stepName);
        stepInfo.setNextStep(nextStepName);
        stepChainInfo.addStepInfo(stepInfo);

        BreakInfo breakInfo = new BreakInfo();
        JumpInfo jumpInfo = new JumpInfo();
        RepeatInfo repeatInfo = new RepeatInfo();
        breakInfo.accept(this);
        jumpInfo.accept(this);
        repeatInfo.accept(this);
    }

    @Override
    public void visitJumpInfo(JumpInfo jumpInfo) {
        Class<?> stepClass = currentNode.getStepClass();
        String stepName = stepChain.getStepName(stepClass);
        JumpDetails details = stepChain.getJumpDetailsForStep(stepClass);

        if(details != null){
            String onSuccess = details.getOnSuccessJumpStep();
            String onFailure = details.getOnFailureJumpStep();
            String condition = details.getConditionClass().getSimpleName();
            jumpInfo.setFromStep(stepName);
            jumpInfo.setOnSuccess(onSuccess);
            jumpInfo.setOnFailure(onFailure);
            jumpInfo.setCondition(new ConditionInfo(condition));

            stepChainInfo.addJumpInfo(jumpInfo);
        }
    }

    @Override
    public void visitBreakInfo(BreakInfo breakInfo) {
        Class<?> stepClass = currentNode.getStepClass();
        String stepName = stepChain.getStepName(stepClass);
        Class conditionClass = stepChain.getBreakConditionClassForStep(stepClass);//.getSimpleName();

        if(conditionClass != null){
            breakInfo.setBreakStep(stepName);
            breakInfo.setCondition(new ConditionInfo(conditionClass.getSimpleName()));

            stepChainInfo.addBreakInfo(breakInfo);
        }
    }

    @Override
    public void visitRepeatInfo(RepeatInfo repeatInfo) {
        Class<?> stepClass = currentNode.getStepClass();
        String stepName = stepChain.getStepName(stepClass);
        RepeatDetails details = stepChain.getRepeatDetailsForStep(stepClass);

        if(details != null){
            String repeatFrom = details.getRepeatFromStep();
            String condition = details.getConditionClass().getSimpleName();

            repeatInfo.setStartRepeatFrom(repeatFrom);
            repeatInfo.setRepeatUpTo(stepName);
            repeatInfo.setCondition(new ConditionInfo(condition));

            stepChainInfo.addRepeatInfo(repeatInfo);
        }
    }

    public void setCurrentNode(BasicStepChain.StepNode currentNode){
        this.currentNode = currentNode;
    }
}
