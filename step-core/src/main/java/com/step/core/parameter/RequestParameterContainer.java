package com.step.core.parameter;

import java.util.List;

/**
 * Created by amishra on 9/27/14.
 */
public interface RequestParameterContainer {
    void addRequestParameter(String name, List<String> values);
    RequestParameterValues getRequestParameterValues(String name);
}
