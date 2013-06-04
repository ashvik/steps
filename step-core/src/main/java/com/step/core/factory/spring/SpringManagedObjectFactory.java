package com.step.core.factory.spring;

import com.step.core.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/25/13
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpringManagedObjectFactory implements ObjectFactory {
    @Autowired
    private org.springframework.beans.factory.config.AutowireCapableBeanFactory beanFactory;

    @Override
    public <T> T fetch(Class<T> objClass) {
        return beanFactory.getBean(objClass);
    }

    @Override
    public Object fetch(String objName) {
        return beanFactory.getBean(objName);
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("init Unsupported");
    }
}
