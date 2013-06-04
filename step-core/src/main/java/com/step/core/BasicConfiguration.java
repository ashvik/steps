package com.step.core;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/26/13
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class BasicConfiguration implements Configuration{
    private String[] stepPackages;
    private String[] stepConfigurationFiles;
    private String executionInterceptorPackage;

    @Override
    public void setStepPackages(String... steps) {
        this.stepPackages = steps;
    }

    @Override
    public String[] getStepPackages() {
        return stepPackages;
    }

    @Override
    public void setStepConfigurationFiles(String... files) {
        this.stepConfigurationFiles = files;
    }

    @Override
    public String[] getStepConfigurationFiles() {
        return this.stepConfigurationFiles;
    }

    @Override
    public void setExecutionInterceptorPackage(String pkg) {
        this.executionInterceptorPackage = pkg;
    }

    @Override
    public String getExecutionInterceptorPackage() {
        return this.executionInterceptorPackage;
    }
}
