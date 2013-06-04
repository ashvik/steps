package com.step.core.executor.proxy;

import com.step.core.chain.StepChain;
import com.step.core.context.StepContext;
import com.step.core.enums.InterceptorType;
import com.step.core.executor.StepExecutor;
import com.step.core.interceptor.ExecutionInterceptor;
import com.step.core.interceptor.ExecutionInterceptorHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/30/13
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class StepExecutorHandler implements InvocationHandler{
    private StepExecutor executor;
    private List<ExecutionInterceptorHolder> interceptors;

    public StepExecutorHandler(StepExecutor executor, List<ExecutionInterceptorHolder> interceptors){
        this.executor = executor;
        this.interceptors = interceptors;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<ExecutionInterceptor> post = new ArrayList<ExecutionInterceptor>();
        StepChain chain = (StepChain)args[0];
        StepContext context = (StepContext)args[1];

        for(ExecutionInterceptorHolder interceptor : interceptors){
            if(interceptor.getType() == InterceptorType.BEFORE){
                interceptor.getInterceptor().intercept(chain, context);
            }else{
                post.add(interceptor.getInterceptor());
            }
        }

        Object result = method.invoke(executor, args);

        for(ExecutionInterceptor interceptor : post){
            interceptor.intercept(chain, context);
        }

        return result;
    }
}
