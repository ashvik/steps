package com.step.core.interceptor;

import com.step.core.enums.InterceptorType;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/30/13
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionInterceptorHolder {
    private ExecutionInterceptor interceptor;
    private InterceptorType type;

    public ExecutionInterceptorHolder(ExecutionInterceptor interceptor, InterceptorType type){
         this.interceptor = interceptor;
         this.type = type;
    }

    public ExecutionInterceptor getInterceptor() {
        return interceptor;
    }

    public InterceptorType getType() {
        return type;
    }
}
