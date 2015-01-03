package com.step.core;

import com.step.core.io.ExecutionResult;

/**
 * Created by amishra on 12/30/14.
 */
public interface PluginGateway {
    ExecutionResult runPlugin(Object... inputs) throws Exception;
}
