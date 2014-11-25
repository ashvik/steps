package com.step.core.executor.proxy;

import com.step.core.chain.StepChain;
import com.step.core.context.StepExecutionContext;
import com.step.core.enums.InterceptorType;
import com.step.core.exceptions.StepExecutionException;
import com.step.core.exceptions.handler.StepExceptionHandler;
import com.step.core.executor.StepExecutor;
import com.step.core.interceptor.ExecutionInterceptor;
import com.step.core.interceptor.ExecutionInterceptorHolder;
import com.step.core.io.ExecutionResult;
import com.step.core.utils.StepExecutionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
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
        StepExecutionContext context = (StepExecutionContext)args[1];
        List<String> ins = chain.getInputType();
        String expectedOutCome = chain.getExpectedOutComeClass();
        Class expectedOutComeClass = null;
        Class expectedCollectionType = null;
        boolean isCollectionTypeOutCome = false;
        ClassLoader classLoader = context.getClassLoader();


        if(expectedOutCome != null){
            String[] tokens = expectedOutCome.split(" of ");
            if(tokens.length == 2){
                try {
                    expectedOutComeClass = StepExecutionUtil.loadClass(tokens[0], classLoader);
                    expectedCollectionType = StepExecutionUtil.loadClass(tokens[1], classLoader);
                    isCollectionTypeOutCome = true;
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                    //TODO throw StepExecutionException
                }
            }else{
                try {
                    expectedOutComeClass = StepExecutionUtil.loadClass(expectedOutCome, classLoader);
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                    //TODO throw StepExecutionException
                }
            }
        }

        //TODO throw StepExecutionException if input not found
        if(ins != null && !ins.isEmpty()){
            for(String in : ins){
                String[] tokens = in.split(" of ");
                if(tokens.length == 2){
                    Class cls = StepExecutionUtil.loadClass(tokens[1], classLoader);
                    if(tokens[0].contains("List")){
                        context.getStepInput().getListTypeInput(cls);
                    }else{
                        context.getStepInput().getSetTypeInput(cls);
                    }
                }else {
                    context.getInput(StepExecutionUtil.loadClass(in, classLoader));
                }
            }
        }

        for(ExecutionInterceptorHolder interceptor : interceptors){
            if(interceptor.getType() == InterceptorType.BEFORE){
                try{
                    interceptor.getInterceptor().intercept(chain, context);
                }catch(Exception e){
                    if(chain.getStepExceptionHandler() != null){
                        StepExceptionHandler handler = chain.getStepExceptionHandler().newInstance();
                        handler.setStepExecutionContext(context);
                        handler.handleException(e);
                        if(expectedOutComeClass != null){
                            if(!isCollectionTypeOutCome){
                                return new ExecutionResult(context.getInput(expectedOutComeClass), null);
                            }else{
                                if(expectedOutComeClass == List.class){
                                    return new ExecutionResult(context.getStepInput().getListTypeInput(expectedCollectionType), null);
                                }else{
                                    return new ExecutionResult(context.getStepInput().getSetTypeInput(expectedCollectionType), null);
                                }
                            }
                        }else{
                            return null;
                        }
                    }else{
                        throw e;
                    }
                }
            }else{
                post.add(interceptor.getInterceptor());
            }
        }

        Object result;

        try{
            result = method.invoke(executor, args);
        }catch (InvocationTargetException e){
            e.printStackTrace();
            throw new StepExecutionException(null,e.getTargetException().getMessage());
        }

        /*if(context.hasStepChainExecutionBroken() && chain.getStepExceptionHandler() != null){
            return result;
        }else{*/
            for(ExecutionInterceptor interceptor : post){
                interceptor.intercept(chain, context);
            //}
        }

        return result;
    }
}
