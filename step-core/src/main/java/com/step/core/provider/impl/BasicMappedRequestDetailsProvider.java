package com.step.core.provider.impl;

import com.step.core.collector.MappedRequestDetailsHolder;
import com.step.core.provider.MappedRequestDetailsProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by amishra on 1/2/15.
 */
public class BasicMappedRequestDetailsProvider implements MappedRequestDetailsProvider {
    private Map<String, MappedRequestDetailsHolder> mappedRequestDetailsHolderMap = new HashMap<String, MappedRequestDetailsHolder>();

    @Override
    public void addMappedRequestDetails(String name, MappedRequestDetailsHolder mappedRequestDetailsHolder) {
        mappedRequestDetailsHolderMap.put(name, mappedRequestDetailsHolder);
    }

    @Override
    public MappedRequestDetailsHolder getMappedRequestDetails(String req) {
        return mappedRequestDetailsHolderMap.get(req);
    }

    @Override
    public Set<String> getAllRequestNames() {
        return mappedRequestDetailsHolderMap.keySet();
    }


}
