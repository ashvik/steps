package com.step.spring.support.factory;

import com.step.core.factory.ObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/25/13
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpringManagedObjectFactory implements ObjectFactory, BeanFactoryAware {
    private BeanFactory beanFactory;

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

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
