package com.step.core.executor.impl;

import com.step.core.Configuration;
import com.step.core.annotations.builder.impl.InterceptorQualifierAnnotationDefinitionBuilder;
import com.step.core.annotations.collector.BasicAnnotationDefinitionCollector;
import com.step.core.chain.StepChain;
import com.step.core.context.StepExecutionContext;
import com.step.core.enums.InterceptorType;
import com.step.core.executor.StepExecutor;
import com.step.core.executor.StepExecutorProvider;
import com.step.core.executor.proxy.StepExecutorHandler;
import com.step.core.interceptor.ExecutionInterceptor;
import com.step.core.interceptor.ExecutionInterceptorHolder;
import com.step.core.interceptor.impl.PostStepExecutionInterceptor;
import com.step.core.interceptor.impl.PreStepExecutionInterceptor;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.AnnotatedDefinitionCollector;
import com.step.core.utils.proxy.ProxySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/29/13
 * Time: 11:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicStepExecutorProvider implements StepExecutorProvider {
    private List<ExecutionInterceptorHolder> interceptors = new ArrayList<ExecutionInterceptorHolder>();

    @Override
    public StepExecutor provide(StepChain chain, StepExecutionContext context) {
        StepExecutor exe = new BasicStepExecutor();
        return ProxySupport.getProxy(StepExecutor.class, this.getClass().getClassLoader(), new StepExecutorHandler(exe, interceptors));
    }

    @Override
    public void initInterceptors(Configuration conf) {
        String pkg = conf.getExecutionInterceptorPackage();
        AnnotatedDefinitionCollector collector = new BasicAnnotationDefinitionCollector();
        Set<AnnotatedDefinition> defs = collector.collect(pkg, new InterceptorQualifierAnnotationDefinitionBuilder());

        for(AnnotatedDefinition def : defs){
            try{
               Class<?> cls = def.getAnnotatedClass();
                ExecutionInterceptor interceptor = (ExecutionInterceptor)cls.newInstance();
                InterceptorType type = (InterceptorType)def.getDefinition("type");
                ExecutionInterceptorHolder exeHolder = new ExecutionInterceptorHolder(interceptor, type);
                interceptors.add(exeHolder);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        //Default interceptors
        interceptors.add(new ExecutionInterceptorHolder(new PreStepExecutionInterceptor(), InterceptorType.BEFORE));
        interceptors.add(new ExecutionInterceptorHolder(new PostStepExecutionInterceptor(), InterceptorType.AFTER));
    }
}
