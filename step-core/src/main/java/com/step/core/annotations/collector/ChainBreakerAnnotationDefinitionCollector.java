package com.step.core.annotations.collector;

import com.step.core.annotations.ChainBreaker;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.PackageScanner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class ChainBreakerAnnotationDefinitionCollector extends AbstractStepAnnotationDefinitionCollector{
    @Override
    public Set<AnnotatedDefinition> collect(String pack) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();
        try{
            if(pack != null && !pack.isEmpty()){
                Class[] bds = PackageScanner.getClassesInPackage(pack);

                for(Class cls : bds){
                    try{
                        ChainBreaker ac = (ChainBreaker) cls.getAnnotation(ChainBreaker.class);
                        if(ac != null){
                            AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                            String conditionClass = ac.conditionClass();
                            String request = ac.forRequest();

                            definition.addDefinition("name", makeDefaultStepName(cls));
                            definition.addDefinition("request", request);
                            definition.addDefinition("conditionClass", conditionClass);
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
