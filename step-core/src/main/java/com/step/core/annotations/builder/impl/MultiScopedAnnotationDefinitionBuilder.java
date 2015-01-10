package com.step.core.annotations.builder.impl;

import com.step.core.annotations.MultiScoped;
import com.step.core.utils.AnnotatedDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by amishra on 7/22/14.
 */
public class MultiScopedAnnotationDefinitionBuilder extends AbstractAnnotationDefinitionBuilder{
    @Override
    public Set<AnnotatedDefinition> build(Class... classes) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();

        for(Class cls : classes){
            try{
                MultiScoped ac = (MultiScoped) cls.getAnnotation(MultiScoped.class);
                if(ac != null){
                    AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                    String[] scopes = ac.scopes();
                    String[] next = ac.nextStepsPerScopes();


                    definition.addDefinition("name", makeDefaultStepName(cls));
                    definition.addDefinition("scopes", scopes);
                    definition.addDefinition("next", next);
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
