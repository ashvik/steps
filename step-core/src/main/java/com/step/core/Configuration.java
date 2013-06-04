package com.step.core;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/22/13
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Configuration {
    void setStepPackages(String... steps);
    String[] getStepPackages();
    void setStepConfigurationFiles(String... files);
    String[] getStepConfigurationFiles();
    void setExecutionInterceptorPackage(String pkg);
    String getExecutionInterceptorPackage();
}
