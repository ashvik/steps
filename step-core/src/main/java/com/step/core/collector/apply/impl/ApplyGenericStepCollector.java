package com.step.core.collector.apply.impl;

import com.step.core.annotations.collector.GenericStepAnnotationDefinitionCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.apply.AnnotationDefinitionCollectorApplier;
import com.step.core.enums.GenericStepType;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.AnnotatedDefinitionCollector;

import java.util.*;

/**
 * Created by amishra on 6/14/14.
 */
public class ApplyGenericStepCollector implements AnnotationDefinitionCollectorApplier {
    @Override
    public List<StepDefinitionHolder> apply(String[] stepPkgs) {
        Set<AnnotatedDefinition> annoDefinitions = new HashSet<AnnotatedDefinition>();
        List<StepDefinitionHolder> defs = new ArrayList<StepDefinitionHolder>();
        List<PrioritizedStepDefinition> orderedSteps = new ArrayList<PrioritizedStepDefinition>();

        for(String pkg : stepPkgs){
            AnnotatedDefinitionCollector collector = new GenericStepAnnotationDefinitionCollector();
            annoDefinitions.addAll(collector.collect(pkg));
        }

        for(AnnotatedDefinition ad : annoDefinitions){
            String name = (String)ad.getDefinition("name");
            GenericStepType type = (GenericStepType)ad.getDefinition("type");
            int priority = (Integer)ad.getDefinition("priority");
            StepDefinitionHolder holder = new StepDefinitionHolder(name, ad.getAnnotatedClass());
            holder.setGenericStepType(type);
            holder.setAnnotatedFields((List)ad.getDefinition("dependencies"));
            orderedSteps.add(new PrioritizedStepDefinition(priority, holder));
            Collections.sort(orderedSteps, new PriorityComparator());
        }

        for(PrioritizedStepDefinition ordered : orderedSteps){
            defs.add(ordered.getStepDefinitionHolder());
        }

        return defs;
    }

    private static class PrioritizedStepDefinition {
        private int priority;
        private StepDefinitionHolder holder;

        PrioritizedStepDefinition(int priority, StepDefinitionHolder holder){
            this.priority = priority;
            this.holder = holder;
        }

        public StepDefinitionHolder getStepDefinitionHolder(){
            return this.holder;
        }
    }

    private static class PriorityComparator implements Comparator<PrioritizedStepDefinition>{
        @Override
        public int compare(PrioritizedStepDefinition o1, PrioritizedStepDefinition o2) {
            if(o1.holder.getGenericStepType() == o2.holder.getGenericStepType()){
                return o1.priority - o2.priority;
            }else if(o1.holder.getGenericStepType() == GenericStepType.PRE_STEP){
                return -1;
            }else{
                return 1;
            }
        }
    }
}
