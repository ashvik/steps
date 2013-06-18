package com.step.core.annotations.collector;

import com.step.core.annotations.GenericStepDefinition;
import com.step.core.enums.GenericStepType;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.PackageScanner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/25/13
 * Time: 1:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class GenericStepAnnotationDefinitionCollector extends AbstractStepAnnotationDefinitionCollector {
    @Override
    public Set<AnnotatedDefinition> collect(String pack) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();
        try{
            if(pack != null && !pack.isEmpty()){
                Class[] bds = PackageScanner.getClassesInPackage(pack);

                for(Class cls : bds){
                    try{
                        GenericStepDefinition ac = (GenericStepDefinition) cls.getAnnotation(GenericStepDefinition.class);
                        if(ac != null){
                            AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                            String name = ac.name();
                            GenericStepType type = ac.stepType();
                            int priority = ac.stepPriority();

                            definition.addDefinition("name", name);
                            definition.addDefinition("type", type);
                            definition.addDefinition("priority", priority);
                            definition.addDefinition("dependencies", collectAnnotatedFields(cls));

                            definitions.add(definition);
                        }
                    }catch(Exception e){
                    }
                }
            }
        }catch(Exception e){
            //log.info("Error: "+e.getMessage());
        }

        return definitions;
    }
}
