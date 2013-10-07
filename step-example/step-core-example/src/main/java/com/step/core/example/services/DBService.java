package com.step.core.example.services;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/5/13
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DBService<T> {
    void save(T entity);
    void delete(T entity);
    T fetch(int id);
    void update(T entity);
    void connect();
    void disconnect();
}
