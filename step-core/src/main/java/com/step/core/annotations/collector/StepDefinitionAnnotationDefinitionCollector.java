package com.step.core.annotations.collector;

import com.step.core.annotations.StepDefinition;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.PackageScanner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/22/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepDefinitionAnnotationDefinitionCollector extends AbstractStepAnnotationDefinitionCollector {
    @Override
    public Set<AnnotatedDefinition> collect(String pack) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();
        List<Class<?>> dependencies = new ArrayList<Class<?>>();

        try{
            if(pack != null && !pack.isEmpty()){
                Class[] bds = PackageScanner.getClassesInPackage(pack);

                for(Class cls : bds){
                    try{
                        StepDefinition ac = (StepDefinition) cls.getAnnotation(StepDefinition.class);
                        if(ac != null){
                            AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                            String name = ac.name();
                            String next = ac.next();

                            if(name == null || name.isEmpty()){
                                name = makeDefaultStepName(cls);
                            }

                            definition.addDefinition("name", name);
                            definition.addDefinition("next", next);
                            definition.addDefinition("dependencies", collectAnnotatedFields(cls));

                            definitions.add(definition);
                        }
                    }catch(Exception e){
                        //log.info("Error: "+e.getMessage());
                    }
                }
            }
        }catch(Exception e){
            //log.info("Error: "+e.getMessage());
        }

        return definitions;
    }
}
