package com.step.core.utils.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/29/13
 * Time: 11:45 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxySupport {
    public static <T> T getProxy(Class<T> input, ClassLoader classLoader, InvocationHandler handler){
        return (T) Proxy.newProxyInstance(classLoader, new Class<?>[]{input}, handler);
    }
}
