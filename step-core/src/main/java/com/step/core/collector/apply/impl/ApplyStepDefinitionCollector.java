package com.step.core.collector.apply.impl;

import com.step.core.annotations.collector.StepDefinitionAnnotationDefinitionCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.apply.AnnotationDefinitionCollectorApplier;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.AnnotatedDefinitionCollector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class ApplyStepDefinitionCollector implements AnnotationDefinitionCollectorApplier {
    @Override
    public List<StepDefinitionHolder> apply(String[] stepPkgs) {
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