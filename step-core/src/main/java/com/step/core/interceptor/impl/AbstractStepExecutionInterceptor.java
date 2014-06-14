package com.step.core.interceptor.impl;

import com.step.core.ResponseLessStep;
import com.step.core.chain.StepChain;
import com.step.core.context.StepExecutionContext;
import com.step.core.interceptor.ExecutionInterceptor;
import com.step.core.utils.StepExecutionUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/30/13
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStepExecutionInterceptor implements ExecutionInterceptor {
    protected void executeInterceptorSteps(List<Class<?>> steps, StepChain chain, StepExecutionContext context) {
        for(Class<?> stepClass : steps){
            try{
                Object step = stepClass.newInstance();
                StepExecutionUtil.makeRichStepObject(step, chain.getDependenciesForStep(stepClass), context);
                ResponseLessStep rls = (ResponseLessStep)step;
                rls.setStepExecutionContext(context);
                rls.execute();
            }catch(Exception e){
                e.printStackTrace();
                throw new IllegalStateException(e.getMessage());
            }
        }
    }
}
