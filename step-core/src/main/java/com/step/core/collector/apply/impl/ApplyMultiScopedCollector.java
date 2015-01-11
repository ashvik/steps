package com.step.core.collector.apply.impl;

import com.step.core.annotations.builder.impl.MultiScopedAnnotationDefinitionBuilder;
import com.step.core.annotations.collector.BasicAnnotationDefinitionCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.apply.AnnotationDefinitionCollectorApplier;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.AnnotatedDefinitionCollector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by amishra on 7/22/14.
 */
public class ApplyMultiScopedCollector implements AnnotationDefinitionCollectorApplier {
    @Override
    public List<StepDefinitionHolder> apply(String[] stepPkgs) {
        Set<AnnotatedDefinition> annoDefinitions = new HashSet<AnnotatedDefinition>();
        List<StepDefinitionHolder> defs = new ArrayList<StepDefinitionHolder>();
        for(String pkg : stepPkgs){
            AnnotatedDefinitionCollector collector = new BasicAnnotationDefinitionCollector();
            annoDefinitions.addAll(collector.collect(pkg, new MultiScopedAnnotationDefinitionBuilder()));
        }

        for(AnnotatedDefinition ad : annoDefinitions){
            String names = (String)ad.getDefinition("name");
            String[] scopes = (String[])ad.getDefinition("scopes");
            String[] next = (String[])ad.getDefinition("next");
            StepDefinitionHolder holder = new StepDefinitionHolder(names, ad.getAnnotatedClass());
            holder.setAnnotatedFields((List)ad.getDefinition("dependencies"));
            holder.setPlugins((List)ad.getDefinition("plugins"));
            holder.setAnnotatedInputs((List)ad.getDefinition("inputs"));
            holder.setInputAsListAnnotatedFields((List)ad.getDefinition("inputsAsList"));
            holder.setInputAsSetAnnotatedFields((List)ad.getDefinition("inputsAsSet"));
            holder.setParameterAnnotatedFields((List)ad.getDefinition("parameters"));
            holder.setExecutionMethod((String)ad.getDefinition("executionMethod"));

            for(int i=0 ; i<scopes.length ; i++){
                holder.addScope(scopes[i], next[i]);
            }

            defs.add(holder);
        }

        return defs;
    }
}
