package com.step.core.example.services.impl;

import com.step.core.example.entity.BusinessObject;
import com.step.core.example.services.DBService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/5/13
 * Time: 7:22 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class DBServiceImpl implements DBService<BusinessObject> {
    private Map<Integer, BusinessObject> db = new HashMap();
    private Logger log = Logger.getLogger(this.getClass().getName());
    private boolean isConnected = false;

    @Override
    public void save(BusinessObject entity) {
        checkConnection();
        db.put(entity.getId(), entity);
        log.info("Saved entity: "+entity);
    }

    @Override
    public void delete(BusinessObject entity) {
        checkConnection();
        db.remove(entity.getId());
        log.info("Deleted entity: "+entity);
    }

    @Override
    public BusinessObject fetch(int id) {
        checkConnection();
        return db.get(id);
    }

    @Override
    public void update(BusinessObject entity) {
        checkConnection();
        BusinessObject bo = db.get(entity.getId());
        bo.setState(entity);
    }

    public void connect(){
        this.isConnected = true;
    }

    public void disconnect(){
        this.isConnected = false;
    }

    private void checkConnection(){
        if(!isConnected){
            throw new IllegalStateException("Not Connected to DB Service!!");
        }
    }
}
