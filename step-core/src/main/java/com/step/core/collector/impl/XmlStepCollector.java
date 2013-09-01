package com.step.core.collector.impl;

import com.step.core.Configuration;
import com.step.core.collector.StepCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.xml.model.MapRequest;
import com.step.core.xml.model.MultiScopedStep;
import com.step.core.xml.model.Scope;
import com.step.core.xml.model.StepRequestMapper;
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
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return definitions;
    }
}
