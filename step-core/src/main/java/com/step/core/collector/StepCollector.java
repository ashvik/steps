package com.step.core.collector;

import com.step.core.Configuration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
public interface StepCollector {
     List<StepDefinitionHolder> collect(Configuration conf);
}
