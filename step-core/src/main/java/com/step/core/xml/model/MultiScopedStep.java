package com.step.core.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/30/13
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiScopedStep {
    private String name;
    private List<Scope> scopes = new ArrayList<Scope>();

    public MultiScopedStep(String name){
        this.name = name;
    }

    public void addScope(Scope scope){
        this.scopes.add(scope);
    }

    public List<Scope> getScopes(){
        return this.scopes;
    }

    public String getName(){
        return this.name;
    }
}
