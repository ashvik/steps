package com.step.core.alias.impl;

import com.step.core.alias.RequestAliasProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amishra on 12/23/14.
 */
public class BasicRequestAliasProvider implements RequestAliasProvider {
    private Map<String, String> aliasToRequestMap = new HashMap<String, String>();
    private Map<String, String> requestToAliasMap = new HashMap<String, String>();

    @Override
    public void collectRequestAlias(String alias, String request) {
        aliasToRequestMap.put(alias, request);
        requestToAliasMap.put(request, alias);
    }

    @Override
    public String getRequestForAlias(String alias) {
        return aliasToRequestMap.get(alias);
    }

    @Override
    public String getAliasForRequest(String request) {
        return requestToAliasMap.get(request);
    }
}
