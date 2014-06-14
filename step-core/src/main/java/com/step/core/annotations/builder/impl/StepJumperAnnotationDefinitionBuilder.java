package com.step.core.annotations.builder.impl;

import com.step.core.annotations.StepJumper;
import com.step.core.utils.AnnotatedDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class StepJumperAnnotationDefinitionBuilder extends AbstractAnnotationDefinitionBuilder{
    @Override
    public Set<AnnotatedDefinition> build(Class... classes) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();

        for(Class cls : classes){
            try{
                StepJumper ac = (StepJumper) cls.getAnnotation(StepJumper.class);
                if(ac != null){
                    AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                    String conditionClass = ac.conditionClass();
                    String onSuccess = ac.onSuccessJumpTo();
                    String onFailure = ac.onFailureJumpTo();
                    String request = ac.forRequest();

                    definition.addDefinition("name", makeDefaultStepName(cls));
                    definition.addDefinition("request", request);
                    definition.addDefinition("conditionClass", conditionClass);
                    definition.addDefinition("onSuccess", onSuccess);
                    definition.addDefinition("onFailure", onFailure);
                    definitions.add(definition);
                }
            }catch(Exception e){
            }
        }

        return definitions;
    }
}
