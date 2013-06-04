package com.step.core.annotations.collector;

import com.step.core.annotations.StepDefinition;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.AnnotatedDefinitionCollector;
import com.step.core.utils.PackageScanner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/22/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepDefinitionAnnotationDefinitionCollector implements AnnotatedDefinitionCollector {
    @Override
    public Set<AnnotatedDefinition> collect(String pack) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();
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

    private String makeDefaultStepName(Class stepClass){
        String clsName = stepClass.getClass().getName();
        String defaultName = clsName.substring(clsName.lastIndexOf('.')+1, clsName.length());
        char[] chars = defaultName.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);

        return new String(chars, 0, chars.length);
    }
}
