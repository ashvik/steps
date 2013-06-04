package com.step.core.annotations.collector;

import com.step.core.annotations.GenericStepDefinition;
import com.step.core.annotations.InterceptorQualifier;
import com.step.core.enums.GenericStepType;
import com.step.core.enums.InterceptorType;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.AnnotatedDefinitionCollector;
import com.step.core.utils.PackageScanner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/30/13
 * Time: 12:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class InterceptorQualifierAnnotationDefinitionCollector implements AnnotatedDefinitionCollector {
    @Override
    public Set<AnnotatedDefinition> collect(String pack) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();
        try{
            if(pack != null && !pack.isEmpty()){
                Class[] bds = PackageScanner.getClassesInPackage(pack);

                for(Class cls : bds){
                    try{
                        InterceptorQualifier ac = (InterceptorQualifier) cls.getAnnotation(InterceptorQualifier.class);
                        if(ac != null){
                            AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                            InterceptorType type = ac.type();

                            definition.addDefinition("type", type);
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
