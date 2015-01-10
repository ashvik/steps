package com.step.core.interceptor.impl;

import com.step.core.ResponseLessStep;
import com.step.core.chain.StepChain;
import com.step.core.context.StepExecutionContext;
import com.step.core.interceptor.ExecutionInterceptor;
import com.step.core.utils.StepExecutionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/30/13
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStepExecutionInterceptor implements ExecutionInterceptor {
    private final Log logger = LogFactory.getLog(getClass());

    protected void executeInterceptorSteps(List<Class<?>> steps, StepChain chain, StepExecutionContext context) {
        for(Class<?> stepClass : steps){
            try{
                Object step = StepExecutionUtil.loadClass(stepClass.getName(), context.getClassLoader()).newInstance();
                StepExecutionUtil.makeRichStepObject(step, chain.getInterceptorStepDefinition(stepClass), context);
                if(step instanceof ResponseLessStep){
                    ResponseLessStep rls = (ResponseLessStep)step;
                    rls.setStepExecutionContext(context);
                    rls.execute();
                }else{
                    Method method = stepClass.getDeclaredMethod("execute");
                    Object stepResult = method.invoke(step);
                    if(stepResult != null){
                        context.getStepInput().setInput(stepResult);
                    }
                }
            }catch(Exception e){
                logger.info("Exception Occurred during execution of step: "+stepClass.getName()+", cause: "+e.getMessage());
                throw new IllegalStateException(e.getMessage());
            }
        }
    }
}
