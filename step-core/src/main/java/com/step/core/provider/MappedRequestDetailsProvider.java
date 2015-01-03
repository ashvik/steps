package com.step.core.provider;

import com.step.core.collector.MappedRequestDetailsHolder;

import java.util.Set;

/**
 * Created by amishra on 1/2/15.
 */
public interface MappedRequestDetailsProvider {
    void addMappedRequestDetails(String name, MappedRequestDetailsHolder mappedRequestDetailsHolder);
    MappedRequestDetailsHolder getMappedRequestDetails(String req);
    Set<String> getAllRequestNames();
}
