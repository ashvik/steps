package com.step.core.parameter;

import java.util.List;

/**
 * Created by amishra on 9/28/14.
 */
public interface GenericRequestParameterProvider {
    void addRequestParameterNameValue(String name, ParameterNameValueHolder requestParameterContainer);
    List<ParameterNameValueHolder> getRequestParameterNameValuePairs(String name);
}
