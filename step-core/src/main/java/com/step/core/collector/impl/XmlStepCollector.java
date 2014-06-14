package com.step.core.collector.impl;

import com.step.core.Configuration;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.collector.StepCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.xml.model.*;
import com.step.core.xml.parse.StepConfigurationParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class XmlStepCollector implements StepCollector {

    @Override
    public List<StepDefinitionHolder> collect(Configuration conf) {
        List<StepDefinitionHolder> definitions = new ArrayList<StepDefinitionHolder>();
        String[] xmls = conf.getStepConfigurationFiles();

        for(String xml : xmls){
            definitions.addAll(makeFromConfigurationFile(xml));
        }
        return definitions;
    }

    private List<StepDefinitionHolder> makeFromConfigurationFile(String xmfFile){
        List<StepDefinitionHolder> definitions = new ArrayList<StepDefinitionHolder>();
        List<Jumper> allJumpers = new ArrayList<Jumper>();
        List<Breaker> allBreakers = new ArrayList<Breaker>();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmfFile);
        StepConfigurationParser parser = new StepConfigurationParser();

        try{
            StepRequestMapper mapper = parser.parse(in);
            for(MultiScopedStep mss : mapper.getMultiScopedSteps()){
                StepDefinitionHolder holder = new StepDefinitionHolder(mss.getName());
                for(Scope scope : mss.getScopes()){
                    holder.addScope(scope.getRequest(), scope.getNextStep());
                }
                definitions.add(holder);
            }
            for(MapRequest mr : mapper.getMapRequests()){
                StepDefinitionHolder holder = new StepDefinitionHolder(mr.getRootStep());
                holder.setMappedRequest(mr.getRequest());
                holder.setCanApplyGenericSteps(mr.isApplyGenericSteps());
                holder.setPreSteps(mr.getPreSteps());
                holder.setPostSteps(mr.getPostSteps());
                holder.setOnSuccess(mr.getOnSuccess());
                holder.setOnFailure(mr.getOnFailure());
                definitions.add(holder);

                allJumpers.addAll(mr.getJumpers());
                allBreakers.addAll(mr.getBreaker());
            }

            processJumpers(allJumpers, definitions);
            processBreakers(allBreakers, definitions);
        }catch(Exception e){
            e.printStackTrace();
        }

        return definitions;
    }

    private void processJumpers(List<Jumper> allJumpers, List<StepDefinitionHolder> definitions) throws ClassNotFoundException {
        for(Jumper jumper : allJumpers){
            StepDefinitionHolder h = new StepDefinitionHolder(jumper.getForStep());
            JumpDetails details = new JumpDetails();
            details.setConditionClass(Class.forName(jumper.getConditionClass()));
            details.setOnSuccessJumpStep(jumper.getOnSuccessJumpTo());
            details.setOnFailureJumpStep(jumper.getOnFailureJumpTo());
            h.addJumpDetails(jumper.getRequest(), details);
            definitions.add(h);
        }
    }

    private void processBreakers(List<Breaker> allBreakers, List<StepDefinitionHolder> definitions) throws ClassNotFoundException {
        for(Breaker breaker : allBreakers){
            StepDefinitionHolder h = new StepDefinitionHolder(breaker.getForStep());
            BreakDetails details = new BreakDetails();
            details.setConditionClass(Class.forName(breaker.getConditionClass()));
            h.addBreakDetails(breaker.getRequest(), details);
            definitions.add(h);
        }
    }
}
