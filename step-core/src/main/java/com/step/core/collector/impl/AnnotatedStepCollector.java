package com.step.core.collector.impl;

import com.step.core.Configuration;
import com.step.core.annotations.collector.StepDefinitionAnnotationDefinitionCollector;
import com.step.core.collector.StepCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.AnnotatedDefinitionCollector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class AnnotatedStepCollector implements StepCollector{
    @Override
    public List<StepDefinitionHolder> collect(Configuration conf) {
        List<StepDefinitionHolder> definitions = new ArrayList<StepDefinitionHolder>();
        String[] stepPackages = conf.getStepPackages();
        definitions.addAll(scanAndCollectStepDefinition(stepPackages));

        return definitions;
    }

    private List<StepDefinitionHolder> scanAndCollectStepDefinition(String[] stepPkgs) {
        Set<AnnotatedDefinition> annoDefinitions = new HashSet<AnnotatedDefinition>();
        List<StepDefinitionHolder> defs = new ArrayList<StepDefinitionHolder>();
        for(String pkg : stepPkgs){
            AnnotatedDefinitionCollector collector = new StepDefinitionAnnotationDefinitionCollector();
            annoDefinitions.addAll(collector.collect(pkg));
        }

        for(AnnotatedDefinition ad : annoDefinitions){
            String names = (String)ad.getDefinition("name");
            String next = (String)ad.getDefinition("next");
            StepDefinitionHolder holder = new StepDefinitionHolder(names, next, ad.getAnnotatedClass());
            holder.setAnnotatedFields((List)ad.getDefinition("dependencies"));

            defs.add(holder);
        }

        return defs;
    }
}
