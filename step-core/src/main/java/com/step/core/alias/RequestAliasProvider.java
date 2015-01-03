package com.step.core.alias;

/**
 * Created by amishra on 12/23/14.
 */
public interface RequestAliasProvider {
    void collectRequestAlias(String alias, String request);
    String getRequestForAlias(String alias);
    String getAliasForRequest(String request);
}
