package com.step.web.factory;

import com.step.core.factory.ObjectFactory;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/23/13
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectFactoryInitializer<T> {
    ObjectFactory initObjectFactory(T initHelper);
}
