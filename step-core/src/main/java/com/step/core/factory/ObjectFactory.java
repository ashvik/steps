package com.step.core.factory;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/19/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectFactory {
    <T> T fetch(Class<T> objClass);
    Object fetch(String objName);
    void init();
}
