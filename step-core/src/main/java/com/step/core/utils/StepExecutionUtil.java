package com.step.core.utils;

import com.step.core.collector.StepDefinitionHolder;
import com.step.core.context.StepExecutionContext;
import com.step.core.utils.enrichers.EnrichListTypeInputs;
import com.step.core.utils.enrichers.EnrichParameters;
import com.step.core.utils.enrichers.EnrichPlugins;
import com.step.core.utils.enrichers.EnrichSetTypeInputs;
import com.step.core.utils.enrichers.EnrichStepDependencies;
import com.step.core.utils.enrichers.EnrichStepInputs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/18/13
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class StepExecutionUtil {
    private static List<StepEnricher> stepEnrichers = new ArrayList<StepEnricher>();

    static {
        stepEnrichers.add(new EnrichStepDependencies());
        stepEnrichers.add(new EnrichPlugins());
        stepEnrichers.add(new EnrichStepInputs());
        stepEnrichers.add(new EnrichListTypeInputs());
        stepEnrichers.add(new EnrichSetTypeInputs());
        stepEnrichers.add(new EnrichParameters());
    }

    public static void makeRichStepObject(Object stepObject, StepDefinitionHolder definitionHolder, StepExecutionContext context){
        for(StepEnricher stepEnricher : stepEnrichers){
            stepEnricher.enrichStep(stepObject, definitionHolder, context);
        }
    }

    public static Class loadClass(String classToLoad, ClassLoader classLoader) throws ClassNotFoundException {
        if(classLoader != null){
            return classLoader.loadClass(classToLoad);
        }

        return Class.forName(classToLoad);
    }
}
