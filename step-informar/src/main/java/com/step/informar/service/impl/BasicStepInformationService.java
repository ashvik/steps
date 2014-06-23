package com.step.informar.service.impl;

import com.step.core.chain.StepChain;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.repository.StepRepository;
import com.step.informar.flat.*;
import com.step.informar.service.StepInformationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by amishra on 6/21/14.
 */
public class BasicStepInformationService implements StepInformationService {
    private StepRepository stepRepository;
    private Map<String, StepChainInfo> stepChainInfoMap = new HashMap<String, StepChainInfo>();

    @Override
    public void setStepRepository(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public Set<String> fetchAllConfiguredRequests() {
        TreeSet<String> requests = new TreeSet<String>();
        requests.addAll(stepRepository.getAllRequestsByName());
        return Collections.unmodifiableSet(requests);
    }

    @Override
    public Set<String> fetchAllConfiguredSteps() {
        TreeSet<String> steps = new TreeSet<String>();
        steps.addAll(stepRepository.getAllStepsByName());
        return Collections.unmodifiableSet(steps);
    }

    @Override
    public StepChainInfo getStepChainInfoForRequest(String request) {
        StepChainInfo stepChainInfo = stepChainInfoMap.get(request);

        if(stepChainInfo == null){
            StepChain chain = stepRepository.getStepExecutionChainForRequest(request);
            stepChainInfo = new StepChainInfo();
            stepChainInfo.prepareStepChainInfo(chain);
            stepChainInfoMap.put(request, stepChainInfo);
        }
        return stepChainInfo;
    }

    @Override
    public List<String> getStepChainInfoDiagramForRequest(String request) {
        StepChainInfo stepChainInfo = getStepChainInfoForRequest(request);
        List<StepInfo> steps = stepChainInfo.getDefaultExecutionChain();
        List<String> print = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        int stepNo = 1;

        for(int i=0 ; i<steps.size() ; i++){
            StepInfo stepInfo = steps.get(i);
            String name = stepInfo.getStepName()+(stepInfo.getInterceptorType() != null ? " ("+stepInfo.getInterceptorType()+")" : "");
            if(i != steps.size()-1){
                builder.append("=>("+stepNo+") "+name+"\n");
                stepNo++;
            }else{
                builder.append("=>("+stepNo+") "+name);
            }
        }
        print.add("EXECUTION CHAIN:- ");
        print.add(builder.toString()+"\n");

        if(!stepChainInfo.getJumpers().isEmpty()){
            print.add("JUMPERS:- ");
            print.add(" ********************************************************************************************************************************************************************************************************************************************************");
            print.add(" *                          FROM STEP                           |                           ON SUCCESS                           |                          ON FAILURE                         |                          CONDITION                     *");
            print.add(" ********************************************************************************************************************************************************************************************************************************************************");

            List<JumpInfo> jumpInfos = stepChainInfo.getJumpers();
            if(!jumpInfos.isEmpty()){
                for(JumpInfo jumpInfo : jumpInfos){
                    String info = makeJumpInfoValueString(jumpInfo);
                    print.add(info);
                    print.add(" --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                }
            }

            print.add("\n");
        }

        if(!stepChainInfo.getBreakers().isEmpty()){
            print.add("BREAKERS:- ");
            print.add(" ********************************************************************************************************************************");
            print.add(" *                          BREAK STEP                          |                         CONDITION                             *");
            print.add(" ********************************************************************************************************************************");
            List<BreakInfo> breakInfos = stepChainInfo.getBreakers();
            if(!breakInfos.isEmpty()){
                for(BreakInfo breakInfo : breakInfos){
                    String info = makeBreakInfoValueString(breakInfo);
                    print.add(info);
                    print.add(" --------------------------------------------------------------------------------------------------------------------------------");
                }
            }

            print.add("\n");
        }

        if(!stepChainInfo.getRepeaters().isEmpty()){
            print.add("REPEATERS:- ");
            print.add(" ***********************************************************************************************************************************************************************************************");
            print.add(" *                          FROM STEP                          |                           UP TO STEP                            |                         CONDITION                           *");
            print.add(" ***********************************************************************************************************************************************************************************************");
            List<RepeatInfo> repeatInfos = stepChainInfo.getRepeaters();
            if(!repeatInfos.isEmpty()){
                for(RepeatInfo repeatInfo : repeatInfos){
                    String info = makeRepeatInfoValueString(repeatInfo);
                    print.add(info);
                    print.add(" -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                }
            }
        }

        return print;
    }

    @Override
    public StepInfo getStepDetails(String name) {
        StepDefinitionHolder holder = stepRepository.getStepByName(name);
        StepInfo info = new StepInfo();
        info.setStepName(holder.getName());
        info.setDescription("-DESCRIPTION-");
        return info;
    }

    private String makeJumpInfoValueString(JumpInfo info){
        StringBuffer buffer = new StringBuffer("| -F-                                                           " +
                "| -S-                                                            " +
                "| -X-                                                         " +
                "| -C-");
        int fromIndex = buffer.indexOf("| -F-");
        int successIndex = buffer.indexOf("| -S-");
        int failureIndex = buffer.indexOf("| -X-");
        int conditionIndex = buffer.indexOf("| -C-");

        buffer = buffer.replace(fromIndex, fromIndex+5, " | "+info.getFromStep());

        buffer = buffer.replace(successIndex, successIndex+5, "| "+info.getOnSuccess());
        successIndex = buffer.indexOf("| -S-");
        if(successIndex > -1)
            buffer = buffer.replace(successIndex, successIndex+5, "");

        buffer = buffer.replace(failureIndex, failureIndex+5, "| "+info.getOnFailure());
        failureIndex = buffer.indexOf("| -X-");
        if(failureIndex > -1)
            buffer = buffer.replace(failureIndex, failureIndex+5, "");

        String cond = info.getCondition().getCondition().trim();
        buffer = buffer.replace(conditionIndex, conditionIndex + 5,"| "+cond);
        conditionIndex = buffer.indexOf("| -C-");
        if(conditionIndex > -1)
            buffer = buffer.replace(conditionIndex, conditionIndex+5, "");
        String finalString = buffer.toString();

        return finalString;

    }

    private String makeBreakInfoValueString(BreakInfo info){
        StringBuffer buffer = new StringBuffer("| -B-                                                           " +
                "| -C- ");
        int fromIndex = buffer.indexOf("| -B-");
        int conditionIndex = buffer.indexOf("| -C-");

        buffer = buffer.replace(fromIndex, fromIndex+5, " | "+info.getBreakStep());

        buffer = buffer.replace(conditionIndex, conditionIndex+5, "| "+info.getCondition().getCondition());
        conditionIndex = buffer.indexOf("| -C-");
        if(conditionIndex > -1)
            buffer = buffer.replace(conditionIndex, conditionIndex+5, "");

        String finalString = buffer.toString();

        return finalString;

    }

    private String makeRepeatInfoValueString(RepeatInfo info){
        StringBuffer buffer = new StringBuffer("| -F-                                                          " +
                "| -U-                                                             " +
                "| -C-                         ");
        int fromIndex = buffer.indexOf("| -F-");
        int upToIndex = buffer.indexOf("| -U-");
        int conditionIndex = buffer.indexOf("| -C-");

        buffer = buffer.replace(fromIndex, fromIndex+5, " | "+info.getStartRepeatFrom());

        buffer = buffer.replace(upToIndex, upToIndex+5, "| "+info.getRepeatUpTo());
        upToIndex = buffer.indexOf("| -U-");
        if(upToIndex > -1)
            buffer = buffer.replace(upToIndex, upToIndex+5, "");

        buffer = buffer.replace(conditionIndex, conditionIndex + 5, "| "+info.getCondition().getCondition());
        conditionIndex = buffer.indexOf("| -C-");
        if(conditionIndex > -1)
            buffer = buffer.replace(conditionIndex, conditionIndex+5, "");

        String finalString = buffer.toString();

        return finalString;

    }
}
