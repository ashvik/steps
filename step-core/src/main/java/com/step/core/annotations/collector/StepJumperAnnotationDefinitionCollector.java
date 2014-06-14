package com.step.core.annotations.collector;

import com.step.core.annotations.StepJumper;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.PackageScanner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class StepJumperAnnotationDefinitionCollector extends AbstractStepAnnotationDefinitionCollector {
    @Override
    public Set<AnnotatedDefinition> collect(String pack) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();
        try{
            if(pack != null && !pack.isEmpty()){
                Class[] bds = PackageScanner.getClassesInPackage(pack);

                for(Class cls : bds){
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
            }
        }catch(Exception e){
            //log.info("Error: "+e.getMessage());
        }

        return definitions;
    }
}
