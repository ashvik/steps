package com.step.core.annotations.builder.impl;

import com.step.core.annotations.StepDefinition;
import com.step.core.utils.AnnotatedDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class StepDefinitionAnnotationDefinitionBuilder extends AbstractAnnotationDefinitionBuilder{
    @Override
    public Set<AnnotatedDefinition> build(Class... classes) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();

        for(Class cls : classes){
            try{
                StepDefinition ac = (StepDefinition) cls.getAnnotation(StepDefinition.class);
                if(ac != null){
                    AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                    String name = ac.name();
                    String next = ac.next();
                    String executionMethod = ac.executionMethod();

                    if(name == null || name.isEmpty()){
                        name = makeDefaultStepName(cls);
                    }

                    definition.addDefinition("name", name);
                    definition.addDefinition("next", next);
                    definition.addDefinition("executionMethod", executionMethod);
                    definition.addDefinition("dependencies", collectAnnotatedFields(cls));
                    definition.addDefinition("plugins", collectPluginAnnotatedFields(cls));
                    definition.addDefinition("inputs", collectInputAnnotatedFields(cls));
                    definition.addDefinition("inputsAsList", collectInputAsListAnnotatedFields(cls));
                    definition.addDefinition("inputsAsSet", collectInputAsSetAnnotatedFields(cls));
                    definition.addDefinition("parameters", collectParameterAnnotatedFields(cls));

                    definitions.add(definition);
                }
            }catch(Exception e){

            }
        }
        return definitions;
    }
}
